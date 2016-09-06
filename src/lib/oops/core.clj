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

(defn gen-throw-validation-error [obj-sym flavor]
  {:pre [(symbol? obj-sym)]}
  `(throw (ex-info (str "Unexpected object value (" ~flavor ")") {:obj ~obj-sym})))

(defn gen-report-validation-error [obj-sym flavor]
  {:pre [(symbol? obj-sym)]}
  `(do
     (report-runtime-error (str "Unexpected object value (" ~flavor ")") ~obj-sym)
     ::validation-error))

(defn gen-validation-error [mode obj-sym flavor]
  {:pre [(symbol? obj-sym)]}
  (case mode
    :throw (gen-throw-validation-error obj-sym flavor)
    :report (gen-report-validation-error obj-sym flavor)
    :sanitize ::validation-error
    false nil))

(defn gen-object-access-validation-check-mode [mode obj-sym]
  {:pre [(symbol? obj-sym)]}
  `(cond
     (cljs.core/undefined? ~obj-sym) ~(gen-validation-error mode obj-sym "undefined")
     (cljs.core/nil? ~obj-sym) ~(gen-validation-error mode obj-sym "nil")
     (cljs.core/boolean? ~obj-sym) ~(gen-validation-error mode obj-sym "boolean")
     (cljs.core/number? ~obj-sym) ~(gen-validation-error mode obj-sym "number")
     (cljs.core/string? ~obj-sym) ~(gen-validation-error mode obj-sym "string")))

(defn gen-object-access-validation-check [obj-sym]
  {:pre [(symbol? obj-sym)]}
  `(case (config/object-access-validation-mode)
     :throw ~(gen-object-access-validation-check-mode :throw obj-sym)
     :report ~(gen-object-access-validation-check-mode :report obj-sym)
     :sanitize ~(gen-object-access-validation-check-mode :sanitize obj-sym)
     false nil))

(defn gen-atomic-key-get [obj key]
  (case (config/atomic-get-mode)
    :aget `(cljs.core/aget ~obj ~key)
    :raw `(~'js* "(~{}[~{}])" ~obj ~key)
    :goog `(goog.object/get ~obj ~key)))

(defn gen-atomic-key-set [obj key val]
  (case (config/atomic-set-mode)
    :aset `(cljs.core/aset ~obj ~key ~val)
    :raw `(~'js* "(~{}[~{}] = ~{})" ~obj ~key ~val)
    :goog `(goog.object/set ~obj ~key ~val)))

(defn gen-key-get [obj key]
  (gen-atomic-key-get obj key))

(defn gen-key-set [obj key val]
  (gen-atomic-key-set obj key val))

(defn gen-validate-object-wrapper [obj-sym body & [error-body]]
  {:pre [(symbol? obj-sym)]}
  (if (config/diagnostics?)
    `(if (= ::validation-error (validate-object-dynamically ~obj-sym))
       ~error-body
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

(defn gen-validate-selector-wrapper [selector-sym body]
  {:pre [(symbol? selector-sym)]}
  (if (config/diagnostics?)
    `(if (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector-sym)
       (do ~body)
       (throw (ex-info "Invalid dynamic selector"                                                                             ; TODO: allow error reporting here
                       {:explain (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector-sym)})))
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

(defmacro coerce-key-dynamically-impl [key-sym]
  {:pre [(symbol? key-sym)]}
  `(name ~key-sym))

(defmacro validate-object-dynamically-impl [obj-sym]
  {:pre [(symbol? obj-sym)]}
  (if (config/diagnostics?)
    (gen-object-access-validation-check obj-sym)))

(defmacro build-path-dynamically-impl [selector-sym]
  {:pre [(symbol? selector-sym)]}
  (let [array-sym (gensym "array")
        atomic-case `(cljs.core/array (coerce-key-dynamically ~selector-sym))
        collection-case `(let [~array-sym (cljs.core/array)]
                           (collect-coerced-items-into-array! ~selector-sym ~array-sym)
                           ~array-sym)]
    `(cond
       (or (string? ~selector-sym) (keyword? ~selector-sym)) ~atomic-case
       ~(gen-is-tagged? selector-sym) ~collection-case
       (cljs.core/array? ~selector-sym) ~selector-sym                                                                         ; we assume native arrays are already paths, TODO: implement diagnostics checks
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
    (gen-validate-selector-wrapper selector-sym (gen-dynamic-path-get obj-sym path))))

(defmacro set-selector-dynamically-impl [obj-sym selector-sym val-sym]
  {:pre [(symbol? obj-sym)
         (symbol? selector-sym)
         (symbol? val-sym)]}
  (let [path `(build-path-dynamically ~selector-sym)]
    (gen-validate-selector-wrapper selector-sym (gen-dynamic-path-set obj-sym path val-sym))))

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
