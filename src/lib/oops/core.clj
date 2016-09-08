(ns oops.core
  (:require [oops.schema :as schema]
            [oops.config :as config]
            [oops.compiler :as compiler :refer [with-diagnostics-context!]]
            [oops.debug :refer [log]]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn supress-reporting? [type]
  (boolean (get-in oops.state/*invoked-opts* [:suppress-reporting type])))

(defn report-if-needed! [type & [info]]
  (if-not (supress-reporting? type)
    (case (config/get-config-key type)
      :warn (compiler/warn! type info)
      :error (compiler/error! type info)
      nil)))

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
  `(do
     (report-runtime-error (str "Unexpected object value (" ~flavor ")") {:obj ~obj-sym})
     false))

(defn gen-dynamic-object-access-validation [obj-sym]
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
  (case (config/key-get-mode)
    :core `(cljs.core/aget ~obj ~key)                                                                                         ; => `(~'js* "(~{}[~{}])" ~obj ~key)
    :goog `(goog.object/get ~obj ~key)))

(defn gen-key-set [obj key val]
  (case (config/key-set-mode)
    :core `(cljs.core/aset ~obj ~key ~val)                                                                                    ; => `(~'js* "(~{}[~{}] = ~{})" ~obj ~key ~val)
    :goog `(goog.object/set ~obj ~key ~val)))

(defn gen-dynamic-object-access-validation-wrapper [obj-sym body]
  {:pre [(symbol? obj-sym)]}
  (if (config/diagnostics?)
    `(if (validate-object-dynamically ~obj-sym)
       ~body)
    body))

(defn gen-instrumented-key-get [obj-sym key]
  {:pre [(symbol? obj-sym)]}
  (gen-dynamic-object-access-validation-wrapper obj-sym (gen-key-get obj-sym key)))

(defn gen-instrumented-key-set [obj-sym key val]
  {:pre [(symbol? obj-sym)]}
  (gen-dynamic-object-access-validation-wrapper obj-sym (gen-key-set obj-sym key val)))

(defn gen-static-path-get [obj path]
  (if (empty? path)
    obj
    (let [obj-sym (gensym "obj")]
      `(let [~obj-sym ~(gen-static-path-get obj (butlast path))]
         ~(gen-instrumented-key-get obj-sym (last path))))))

(defn gen-dynamic-selector-get [obj selector-list]
  (report-if-needed! :dynamic-property-access)
  (case (count selector-list)
    0 obj                                                                                                                     ; get-selector-dynamically passed emtpy selector would return obj
    1 `(get-selector-dynamically ~obj ~(first selector-list))                                                                 ; we want to unwrap selector wrapped in oget (in this case)
    `(get-selector-dynamically ~obj ~(gen-selector-list selector-list))))

(defn gen-dynamic-path-validation [path-sym]
  {:pre [(symbol? path-sym)]}
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-path ~path-sym)
     (let [explanation# (clojure.spec/explain-data ::oops.sdefs/obj-path ~path-sym)]
       (report-runtime-error "Invalid path" {:path        ~path-sym
                                             :explanation explanation#}))
     true))

(defn gen-dynamic-selector-validation [selector-sym]
  {:pre [(symbol? selector-sym)]}
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector-sym)
     (let [explanation# (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector-sym)]
       (report-runtime-error "Invalid selector" {:selector    ~selector-sym
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
  `(reduce get-key-dynamically ~obj-sym ~path))                                                                               ; TODO: this could be rewritten into a raw loop (optimization)

(defn gen-static-path-set [obj-sym path val]
  {:pre [(not (empty? path))
         (symbol? obj-sym)]}
  (let [parent-obj-path (butlast path)
        key (last path)
        parent-obj-sym (gensym "parent-obj")]
    `(let [~parent-obj-sym ~(gen-static-path-get obj-sym parent-obj-path)]
       ~(gen-instrumented-key-set parent-obj-sym key val))))

(defn gen-dynamic-selector-set [obj selector val]
  (report-if-needed! :dynamic-property-access)
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

(defn gen-runtime-diagnostics-context! [_form _env body]
  (if (config/diagnostics?)
    `(binding [oops.state/*console-reporter* (fn [method# & args#]                                                            ; it is imporant to keep this inline so we get proper call-site location and line number
                                               (.apply method# js/console (into-array args#)))]
       ~body)
    body))

(defn gen-enhanced-reported-message [msg]
  `(str "Oops, " ~msg))

(defn gen-enhanced-reported-data [data]
  data)

(defn gen-console-method [kind]
  (case kind
    :error `(.-error js/console)
    :warning `(.-warn js/console)))

(defn gen-report-runtime-message [kind msg data]
  {:pre [(contains? #{:error :warning} kind)]}
  (let [mode (case kind
               :error `(oops.config/error-reporting-mode)
               :warning `(oops.config/warning-reporting-mode))]
    `(case ~mode
       :throw (throw (ex-info ~(gen-enhanced-reported-message msg) ~(gen-enhanced-reported-data data)))
       :console (oops.state/*console-reporter* ~(gen-console-method kind)
                                               ~(gen-enhanced-reported-message msg)
                                               ~(gen-enhanced-reported-data data))
       false nil)))

(defn validate-object-statically [obj]
  ; here we can try to detect some pathological cases and warn user at compile-time
  (if (config/diagnostics?)
    (cond
      (nil? obj) (report-if-needed! :static-nil-object))))

; -- helper macros ----------------------------------------------------------------------------------------------------------

(defmacro report-runtime-error-impl [msg data]
  (gen-report-runtime-message :error msg data))

(defmacro report-runtime-warning-impl [msg data]
  (gen-report-runtime-message :warning msg data))

(defmacro coerce-key-dynamically-impl [key-sym]
  {:pre [(symbol? key-sym)]}
  `(name ~key-sym))

(defmacro validate-object-dynamically-impl [obj-sym]
  {:pre [(symbol? obj-sym)]}
  (gen-dynamic-object-access-validation obj-sym))

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

; -- raw implementations ----------------------------------------------------------------------------------------------------

(defn gen-oget [obj & selector]
  (validate-object-statically obj)
  (let [path (schema/selector->path selector)]
    (if-not (= path :invalid-path)
      (gen-static-path-get obj path)
      (gen-dynamic-selector-get obj selector))))

(defn gen-oset! [obj selector val]
  (validate-object-statically obj)
  (let [obj-sym (gensym "obj")
        path (schema/selector->path selector)]
    `(let [~obj-sym ~obj]
       ~(if-not (= path :invalid-path)
          (gen-static-path-set obj-sym path val)
          (gen-dynamic-selector-set obj-sym selector val))
       ~obj-sym)))

(defn gen-ocall [obj selector & args]
  (validate-object-statically obj)
  (let [obj-sym (gensym "obj")]
    `(let [~obj-sym ~obj]
       (.call ~(gen-oget obj-sym selector) ~obj-sym ~@args))))

(defn gen-oapply [obj selector args]
  (validate-object-statically obj)
  (let [obj-sym (gensym "obj")]
    `(let [~obj-sym ~obj]
       (.apply ~(gen-oget obj-sym selector) ~obj-sym (into-array ~args)))))

; -- public macros ----------------------------------------------------------------------------------------------------------

(defmacro oget [obj & selector]
  (with-diagnostics-context! &form &env {}
    (apply gen-oget obj selector)))

(defmacro oget+ [obj & selector]
  (with-diagnostics-context! &form &env {:suppress-reporting #{:dynamic-property-access}}
    (apply gen-oget obj selector)))

(defmacro oset! [obj selector val]
  (with-diagnostics-context! &form &env {}
    (gen-oset! obj selector val)))

(defmacro oset!+ [obj selector val]
  (with-diagnostics-context! &form &env {:suppress-reporting #{:dynamic-property-access}}
    (gen-oset! obj selector val)))

(defmacro ocall [obj selector & args]
  (with-diagnostics-context! &form &env {}
    (apply gen-ocall obj selector args)))

(defmacro ocall+ [obj selector & args]
  (with-diagnostics-context! &form &env {:suppress-reporting #{:dynamic-property-access}}
    (apply gen-ocall obj selector args)))

(defmacro oapply [obj selector args]
  (with-diagnostics-context! &form &env {}
    (gen-oapply obj selector args)))

(defmacro oapply+ [obj selector args]
  (with-diagnostics-context! &form &env {:suppress-reporting #{:dynamic-property-access}}
    (gen-oapply obj selector args)))

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-diagnostics-context! &form &env {}
    (apply gen-ocall obj selector args)))

(defmacro ocall!+
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-diagnostics-context! &form &env {:suppress-reporting #{:dynamic-property-access}}
    (apply gen-ocall obj selector args)))

(defmacro oapply!
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj selector args]
  (with-diagnostics-context! &form &env {}
    (gen-oapply obj selector args)))

(defmacro oapply!+
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj selector args]
  (with-diagnostics-context! &form &env {:suppress-reporting #{:dynamic-property-access}}
    (gen-oapply obj selector args)))
