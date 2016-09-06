(ns oops.core
  (:require [oops.schema :as schema]
            [oops.config :as config]
            [oops.debug :refer [log]]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn gen-tagged-array [items]
  `(let [arr# (cljs.core/array ~@items)]
     (set! (.-oops-tag$ arr#) true)
     arr#))

(defn gen-is-tagged? [obj]
  `(.-oops-tag$ ~obj))

(defn gen-selector-list [items]
  (if (config/diagnostics?)
    `(cljs.core/list ~@items)                                                                                                 ; this is the slow path under diagnostics we want selector list be a valid selector
    (gen-tagged-array items)))                                                                                                ; this is the fast path for advanced optimizations without diagnostics

(defn gen-object-access-validation-error [obj-sym flavor]
  {:pre [(symbol? obj-sym)]}
  `(let [msg# (str "Unexpected object value (" ~flavor ")")
         data# {:obj ~obj-sym}]
     (report-runtime-error msg# data#)))

(defn gen-object-access-validation [obj-sym]
  {:pre [(symbol? obj-sym)]}
  `(if (config/error-reporting-mode)
     (cond
       (cljs.core/undefined? ~obj-sym) ~(gen-object-access-validation-error obj-sym "undefined")
       (cljs.core/nil? ~obj-sym) ~(gen-object-access-validation-error obj-sym "nil")
       (cljs.core/boolean? ~obj-sym) ~(gen-object-access-validation-error obj-sym "boolean")
       (cljs.core/number? ~obj-sym) ~(gen-object-access-validation-error obj-sym "number")
       (cljs.core/string? ~obj-sym) ~(gen-object-access-validation-error obj-sym "string")
       :else true)
     true))

(defn gen-key-get [obj key]
  (case (config/atomic-get-mode)
    :aget `(cljs.core/aget ~obj ~key)
    :raw `(~'js* "(~{}[~{}])" ~obj ~key)
    :goog `(goog.object/get ~obj ~key)))

(defn gen-key-set [obj key val]
  (case (config/atomic-set-mode)
    :aset `(cljs.core/aset ~obj ~key ~val)
    :raw `(~'js* "(~{}[~{}] = ~{})" ~obj ~key ~val)
    :goog `(goog.object/set ~obj ~key ~val)))

(defn gen-validate-object-wrapper [obj-sym body]
  {:pre [(symbol? obj-sym)]}
  (if (config/diagnostics?)
    `(if ~(gen-object-access-validation obj-sym)
       ~body)
    body))

(defn gen-instrumented-key-get [obj-sym key]
  {:pre [(symbol? obj-sym)]}
  (gen-validate-object-wrapper obj-sym (gen-key-get obj-sym key)))

(defn gen-instrumented-key-set [obj-sym key val]
  {:pre [(symbol? obj-sym)]}
  (gen-validate-object-wrapper obj-sym (gen-key-set obj-sym key val)))

(defn gen-static-path-get [obj path]
  (if (empty? path)
    obj
    (let [obj-sym (gensym "obj")]
      `(let [~obj-sym ~(gen-static-path-get obj (butlast path))]
         ~(gen-instrumented-key-get obj-sym (last path))))))

(defn gen-dynamic-selector-get [obj selector-list]
  (case (count selector-list)
    0 obj                                                                                                                     ; get-selector-dynamically passed emtpy selector would return obj
    1 `(get-selector-dynamically ~obj ~(first selector-list))                                                                 ; we want to unwrap selector wrapped in oget (in this case)
    `(get-selector-dynamically ~obj ~(gen-selector-list selector-list))))

(defn gen-dynamic-path-validation [path-sym]
  {:pre [(symbol? path-sym)]}
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-path ~path-sym)
     (let [explanation# (clojure.spec/explain-data ::oops.sdefs/obj-path ~path-sym)]
       (report-runtime-error "Invalid dynamic path" {:path        ~path-sym
                                                     :explanation explanation#}))
     true))

(defn gen-dynamic-selector-validation [selector-sym]
  {:pre [(symbol? selector-sym)]}
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector-sym)
     (let [explanation# (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector-sym)]
       (report-runtime-error "Invalid dynamic selector" {:selector    ~selector-sym
                                                         :explanation explanation#}))
     true))

(defn gen-dynamic-selector-or-path-validation [selector-or-path-sym]
  {:pre [(symbol? selector-or-path-sym)]}
  `(if (cljs.core/array? ~selector-or-path-sym)
     ~(gen-dynamic-path-validation selector-or-path-sym)
     ~(gen-dynamic-selector-validation selector-or-path-sym)))

(defn gen-dynamic-selector-validation-wrapper [selector-sym body]
  {:pre [(symbol? selector-sym)]}
  (if (config/diagnostics?)
    `(if ~(gen-dynamic-selector-or-path-validation selector-sym)
       ~body)
    body))

(defn gen-dynamic-path-get [obj-sym path]
  {:pre [(symbol? obj-sym)]}
  `(reduce get-key-dynamically ~obj-sym ~path))

(defn gen-static-path-set [obj-sym path val]
  {:pre [(not (empty? path))
         (symbol? obj-sym)]}
  (let [parent-obj-path (butlast path)
        key (last path)
        parent-obj-sym (gensym "parent-obj")]
    `(let [~parent-obj-sym ~(gen-static-path-get obj-sym parent-obj-path)]
       ~(gen-instrumented-key-set parent-obj-sym key val))))

(defn gen-dynamic-selector-set [obj selector val]
  `(set-selector-dynamically ~obj ~selector ~val))

(defn gen-dynamic-path-set [obj-sym path val]
  {:pre [(symbol? obj-sym)]}
  (let [path-sym (gensym "path")
        key-sym (gensym "key")
        parent-obj-path-sym (gensym "parent-obj-path")
        parent-obj-sym (gensym "parent-obj")]
    `(let [~path-sym ~path
           ~parent-obj-path-sym (butlast ~path-sym)
           ~key-sym (last ~path-sym)
           ~parent-obj-sym ~(gen-dynamic-path-get obj-sym parent-obj-path-sym)]
       (set-key-dynamically ~parent-obj-sym ~key-sym ~val))))

; -- helper macros ----------------------------------------------------------------------------------------------------------

(defmacro report-runtime-error-impl [msg data]
  `(do
     (case (config/error-reporting-mode)
       :throw (throw (ex-info ~msg ~data))
       :console (print-error-to-console ~msg ~data)
       false nil)
     nil))

(defmacro coerce-key-dynamically-impl [key-sym]
  {:pre [(symbol? key-sym)]}
  `(name ~key-sym))

(defmacro build-path-dynamically-impl [selector-sym]
  {:pre [(symbol? selector-sym)]}
  (let [atomic-case `(cljs.core/array (coerce-key-dynamically ~selector-sym))
        array-case selector-sym                                                                                               ; we assume native arrays are already paths
        collection-case (let [path-sym (gensym "selector-path")]
                          `(let [~path-sym (cljs.core/array)]
                             (collect-coerced-keys-into-array! ~selector-sym ~path-sym)
                             ~path-sym))]
    `(cond
       (or (string? ~selector-sym) (keyword? ~selector-sym)) ~atomic-case
       ~(gen-is-tagged? selector-sym) ~collection-case
       (cljs.core/array? ~selector-sym) ~array-case
       :else ~collection-case)))

(defmacro get-key-dynamically-impl [obj-sym key-sym]
  {:pre [(symbol? obj-sym)
         (symbol? key-sym)]}
  (gen-instrumented-key-get obj-sym key-sym))

(defmacro set-key-dynamically-impl [obj-sym key-sym val-sym]
  {:pre [(symbol? obj-sym)
         (symbol? key-sym)
         (symbol? val-sym)]}
  (gen-instrumented-key-set obj-sym key-sym val-sym))

(defmacro get-selector-dynamically-impl [obj-sym selector-sym]
  {:pre [(symbol? obj-sym)
         (symbol? selector-sym)]}
  (let [path `(build-path-dynamically ~selector-sym)]
    (gen-dynamic-selector-validation-wrapper selector-sym (gen-dynamic-path-get obj-sym path))))

(defmacro set-selector-dynamically-impl [obj-sym selector-sym val-sym]
  {:pre [(symbol? obj-sym)
         (symbol? selector-sym)
         (symbol? val-sym)]}
  (let [path `(build-path-dynamically ~selector-sym)]
    (gen-dynamic-selector-validation-wrapper selector-sym (gen-dynamic-path-set obj-sym path val-sym))))

; -- public macros ----------------------------------------------------------------------------------------------------------

(defmacro oget [obj & selector]
  (let [path (schema/selector->path selector)]
    (if-not (= path :invalid-path)
      (gen-static-path-get obj path)
      (gen-dynamic-selector-get obj selector))))

(defmacro oset! [obj selector val]
  (let [obj-sym (gensym "obj")
        path (schema/selector->path selector)]
    `(let [~obj-sym ~obj]
       ~(if-not (= path :invalid-path)
          (gen-static-path-set obj-sym path val)
          (gen-dynamic-selector-set obj-sym selector val))
       ~obj-sym)))

(defmacro ocall [obj selector & args]
  (let [obj-sym (gensym "obj")]
    `(let [~obj-sym ~obj]
       (.call (oget ~obj-sym ~selector) ~obj-sym ~@args))))

(defmacro oapply [obj selector args]
  (let [obj-sym (gensym "obj")]
    `(let [~obj-sym ~obj]
       (.apply (oget ~obj-sym ~selector) ~obj-sym (into-array ~args)))))

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [& args]
  `(ocall ~@args))

(defmacro oapply!
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [& args]
  `(oapply ~@args))
