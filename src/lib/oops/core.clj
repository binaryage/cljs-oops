(ns oops.core
  (:refer-clojure :exclude [gensym])
  (:require [oops.schema :as schema]
            [oops.config :as config]
            [oops.messages :refer [runtime-message]]
            [oops.compiler :as compiler :refer [gensym with-diagnostics-context! with-compilation-opts!]]
            [oops.constants :refer [dot-access soft-access punch-access get-dot-access get-soft-access get-punch-access]]
            [oops.debug :refer [log]]
            [clojure.spec :as s]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn supress-reporting? [type]
  (boolean (get-in oops.state/*invoked-opts* [:suppress-reporting type])))

(defn report-if-needed! [type & [info]]
  (if-not (supress-reporting? type)
    (case (config/get-config-key type)
      :warn (compiler/warn! type info)
      :error (compiler/error! type info)
      (false nil) nil)))

(defn gen-tagged-array [items]
  `(let [arr# (cljs.core/array ~@items)]
     (set! (.-oops-tag$ arr#) true)
     arr#))

(defn gen-is-tagged? [obj]
  `(.-oops-tag$ ~obj))

(defn gen-selector-list [items]                                                                                               ; TODO: flip tagging logic here, tag native array case and leave raw js arrays for cljs path
  (if (config/diagnostics?)
    `(cljs.core/list ~@items)                                                                                                 ; this is the slow path under diagnostics we want selector list be a valid selector
    (gen-tagged-array items)))                                                                                                ; this is the fast path for advanced optimizations without diagnostics

(defn gen-object-access-validation-error [obj-sym flavor]
  {:pre [(symbol? obj-sym)]}
  `(do
     (report-runtime-error ~(runtime-message :unexpected-object-value flavor) {:obj ~obj-sym})
     false))

(defn gen-dynamic-object-access-validation [obj-sym mode-sym]
  {:pre [(symbol? obj-sym)
         (symbol? mode-sym)]}
  `(if (config/error-reporting-mode)
     (cond
       (and (= ~mode-sym ~dot-access) (cljs.core/undefined? ~obj-sym)) ~(gen-object-access-validation-error obj-sym "undefined")
       (and (= ~mode-sym ~dot-access) (cljs.core/nil? ~obj-sym)) ~(gen-object-access-validation-error obj-sym "nil")
       (cljs.core/boolean? ~obj-sym) ~(gen-object-access-validation-error obj-sym "boolean")
       (cljs.core/number? ~obj-sym) ~(gen-object-access-validation-error obj-sym "number")
       (cljs.core/string? ~obj-sym) ~(gen-object-access-validation-error obj-sym "string")
       ; TODO: here we could possibly do additional checks
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

(defn gen-dynamic-object-access-validation-wrapper [obj-sym mode body]
  {:pre [(symbol? obj-sym)]}
  (if (config/diagnostics?)
    `(if (validate-object-dynamically ~obj-sym ~mode)
       ~body)
    body))

(defn gen-instrumented-key-get [obj-sym key mode]
  {:pre [(symbol? obj-sym)]}
  (gen-dynamic-object-access-validation-wrapper obj-sym mode (gen-key-get obj-sym key)))

(defn gen-instrumented-key-set [obj-sym key val mode]
  {:pre [(symbol? obj-sym)]}
  (gen-dynamic-object-access-validation-wrapper obj-sym mode (gen-key-set obj-sym key val)))

(defn gen-static-path-get [obj path]
  (if (empty? path)
    obj
    (let [[mode key] (first path)
          obj-sym (gensym "obj")
          next-obj-sym (gensym "next-obj")
          new-prop-sym (gensym "new-prop")]
      ; http://stackoverflow.com/questions/32300269/make-vars-constant-for-use-in-case-statements-in-clojure
      (assert (= dot-access 0))
      (assert (= soft-access 1))
      (assert (= punch-access 2))
      (case mode
        0 `(let [~obj-sym ~obj]
             ~(gen-static-path-get (gen-instrumented-key-get obj-sym key mode) (rest path)))
        1 `(let [~obj-sym ~obj
                 ~next-obj-sym ~(gen-instrumented-key-get obj-sym key mode)]
             (if (some? ~next-obj-sym)
               ~(gen-static-path-get next-obj-sym (rest path))))
        2 `(let [~obj-sym ~obj
                 ~next-obj-sym ~(gen-instrumented-key-get obj-sym key mode)]
             (if (some? ~next-obj-sym)
               ~(gen-static-path-get next-obj-sym (rest path))
               (let [~new-prop-sym (oops.state/*property-punching-factory*)]
                 ~(gen-key-set obj-sym key new-prop-sym)
                 ~(gen-static-path-get new-prop-sym (rest path)))))))))

(defn gen-dynamic-path-get [initial-obj-sym path]
  {:pre [(symbol? initial-obj-sym)]}
  (let [path-sym (gensym "path")
        len-sym (gensym "len")
        i-sym (gensym "i")
        obj-sym (gensym "obj")
        mode-sym (gensym "mode")
        key-sym (gensym "key")
        next-obj-sym (gensym "next-obj")
        new-prop-sym (gensym "new-prop")
        next-i `(+ ~i-sym 2)]
    `(let [~path-sym ~path
           ~len-sym (.-length ~path-sym)]
       (loop [~i-sym 0
              ~obj-sym ~initial-obj-sym]
         (if (< ~i-sym ~len-sym)
           (let [~mode-sym (aget ~path-sym ~i-sym)
                 ~key-sym (aget ~path-sym (inc ~i-sym))
                 ~next-obj-sym (get-key-dynamically ~obj-sym ~key-sym ~mode-sym)]
             (case ~mode-sym
               ~dot-access (recur ~next-i ~next-obj-sym)
               ~soft-access (if (some? ~next-obj-sym)
                              (recur ~next-i ~next-obj-sym))
               ~punch-access (if (some? ~next-obj-sym)
                               (recur ~next-i ~next-obj-sym)
                               (let [~new-prop-sym (oops.state/*property-punching-factory*)]
                                 ~(gen-key-set obj-sym key-sym new-prop-sym)
                                 (recur ~next-i ~new-prop-sym)))))
           ~obj-sym)))))

(defn gen-dynamic-selector-get [obj selector-list]
  (report-if-needed! :dynamic-property-access)
  (case (count selector-list)
    0 obj                                                                                                                     ; get-selector-dynamically passed emtpy selector would return obj, TODO: report warning here?
    1 `(get-selector-dynamically ~obj ~(first selector-list))                                                                 ; we want to unwrap selector wrapped in oget (in this case)
    `(get-selector-dynamically ~obj ~(gen-selector-list selector-list))))

(defn gen-dynamic-path-validation [path-sym]
  {:pre [(symbol? path-sym)]}
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-path ~path-sym)
     (let [explanation# (clojure.spec/explain-data ::oops.sdefs/obj-path ~path-sym)]
       (report-runtime-error ~(runtime-message :invalid-path) {:path        ~path-sym
                                                               :explanation explanation#}))
     true))

(defn gen-dynamic-selector-validation [selector-sym]
  {:pre [(symbol? selector-sym)]}
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector-sym)
     (let [explanation# (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector-sym)]
       (report-runtime-error ~(runtime-message :invalid-selector) {:selector    ~selector-sym
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

(defn gen-static-path-set [obj-sym path val]
  {:pre [(not (empty? path))
         (symbol? obj-sym)]}
  (let [parent-obj-path (butlast path)
        [_mode key] (last path)
        parent-obj-sym (gensym "parent-obj")]
    `(let [~parent-obj-sym ~(gen-static-path-get obj-sym parent-obj-path)]
       ~(gen-instrumented-key-set parent-obj-sym key val dot-access))))

(defn gen-dynamic-selector-set [obj selector-list val]
  (report-if-needed! :dynamic-property-access)
  (case (count selector-list)
    0 nil                                                                                                                     ; TODO: report warning here?
    1 `(set-selector-dynamically ~obj ~(first selector-list) ~val)                                                            ; we want to unwrap selector wrapped in oset! (in this case)
    `(set-selector-dynamically ~obj ~(gen-selector-list selector-list) ~val)))

(defn gen-dynamic-path-set [obj-sym path val]
  {:pre [(symbol? obj-sym)]}
  (let [path-sym (gensym "path")
        key-sym (gensym "key")
        mode-sym (gensym "mode")
        len-sym (gensym "len")
        parent-obj-path-sym (gensym "parent-obj-path")
        parent-obj-sym (gensym "parent-obj")]
    `(let [~path-sym ~path
           ~len-sym (.-length ~path-sym)
           ~parent-obj-path-sym (.slice ~path-sym 0 (- ~len-sym 2))
           ~key-sym (aget ~path-sym (- ~len-sym 1))
           ~mode-sym (aget ~path-sym (- ~len-sym 2))
           ~parent-obj-sym ~(gen-dynamic-path-get obj-sym parent-obj-path-sym)]
       (set-key-dynamically ~parent-obj-sym ~key-sym ~val ~mode-sym))))

(defn gen-reported-message [msg]
  msg)

(defn gen-reported-data [data]
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
       :throw (throw (ex-info ~(gen-reported-message msg) ~(gen-reported-data data)))
       :console (oops.state/*console-reporter* ~(gen-console-method kind)
                                               ~(gen-reported-message msg)
                                               ~(gen-reported-data data))
       false nil)))

(defn validate-object-statically [obj]
  ; here we can try to detect some pathological cases and warn user at compile-time
  (if (config/diagnostics?)
    (cond
      (nil? obj) (report-if-needed! :static-nil-object))))

; this method is hand-written to reduce space (it will be emitted on each macro call-site)
; also to avoid busy-work generated by clojuscript compiler handling varargs
(def console-reporter-fn-template "function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))}")

(defn gen-runtime-diagnostics-context! [_form _env & body]
  (if (config/diagnostics?)
    `(binding [oops.state/*console-reporter* ~(list 'js* console-reporter-fn-template)]                                       ; it is imporant to keep this inline so we get proper call-site location and line number
       ~@body)
    `(do
       ~@body)))

; -- helper macros ----------------------------------------------------------------------------------------------------------

(defmacro report-runtime-error-impl [msg data]
  (gen-report-runtime-message :error msg data))

(defmacro report-runtime-warning-impl [msg data]
  (gen-report-runtime-message :warning msg data))

(defmacro validate-object-dynamically-impl [obj-sym mode]
  {:pre [(symbol? obj-sym)]}
  (gen-dynamic-object-access-validation obj-sym mode))

; TODO: check result with dynamic spec (in debug mode)
; (s/valid? ::sdefs/obj-path %)
(defmacro build-path-dynamically-impl [selector-sym]
  {:pre [(symbol? selector-sym)]}
  (let [atomic-case (let [path-sym (gensym "selector-path")]
                      `(let [~path-sym (cljs.core/array)]
                         (oops.schema/coerce-key-dynamically! ~selector-sym ~path-sym)
                         ~path-sym))
        array-case selector-sym                                                                                               ; we assume native arrays are already paths
        collection-case (let [path-sym (gensym "selector-path")]
                          `(let [~path-sym (cljs.core/array)]
                             (oops.schema/collect-coerced-keys-into-array! ~selector-sym ~path-sym)
                             ~path-sym))]
    `(let [path# (cond
                   (or (string? ~selector-sym) (keyword? ~selector-sym)) ~atomic-case
                   ~(gen-is-tagged? selector-sym) ~collection-case
                   (cljs.core/array? ~selector-sym) ~array-case
                   :else ~collection-case)]
       (assert (clojure.spec/valid? :oops.sdefs/obj-path path#))
       path#)))

(defmacro get-key-dynamically-impl [obj-sym key-sym mode]
  {:pre [(symbol? obj-sym)
         (symbol? key-sym)]}
  (gen-instrumented-key-get obj-sym key-sym mode))

(defmacro set-key-dynamically-impl [obj-sym key-sym val-sym mode]
  {:pre [(symbol? obj-sym)
         (symbol? key-sym)
         (symbol? val-sym)]}
  (gen-instrumented-key-set obj-sym key-sym val-sym mode))

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
  (if-let [path (schema/selector->path selector)]
    (gen-static-path-get obj path)
    (gen-dynamic-selector-get obj selector)))

(defn gen-oset! [obj selector val]
  (validate-object-statically obj)
  (let [obj-sym (gensym "obj")
        path (schema/selector->path selector)]
    `(let [~obj-sym ~obj]
       ~(if path
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
  (with-diagnostics-context! &form &env
    (apply gen-oget obj selector)))

(defmacro oget+ [obj & selector]
  (with-diagnostics-context! &form &env
    (with-compilation-opts! {:suppress-reporting #{:dynamic-property-access}}
      (apply gen-oget obj selector))))

(defmacro oset! [obj & selector+val]
  (with-diagnostics-context! &form &env
    (let [val (last selector+val)
          selector (butlast selector+val)]
      (gen-oset! obj selector val))))

(defmacro oset!+ [obj & selector+val]
  (with-diagnostics-context! &form &env
    (with-compilation-opts! {:suppress-reporting #{:dynamic-property-access}}
      (let [val (last selector+val)
            selector (butlast selector+val)]
        (gen-oset! obj selector val)))))

(defmacro ocall [obj selector & args]
  (with-diagnostics-context! &form &env
    (apply gen-ocall obj selector args)))

(defmacro ocall+ [obj selector & args]
  (with-diagnostics-context! &form &env
    (with-compilation-opts! {:suppress-reporting #{:dynamic-property-access}}
      (apply gen-ocall obj selector args))))

(defmacro oapply [obj selector args]
  (with-diagnostics-context! &form &env
    (gen-oapply obj selector args)))

(defmacro oapply+ [obj selector args]
  (with-diagnostics-context! &form &env
    (with-compilation-opts! {:suppress-reporting #{:dynamic-property-access}}
      (gen-oapply obj selector args))))

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-diagnostics-context! &form &env
    (apply gen-ocall obj selector args)))

(defmacro ocall!+
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-diagnostics-context! &form &env
    (with-compilation-opts! {:suppress-reporting #{:dynamic-property-access}}
      (apply gen-ocall obj selector args))))

(defmacro oapply!
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj selector args]
  (with-diagnostics-context! &form &env
    (gen-oapply obj selector args)))

(defmacro oapply!+
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj selector args]
  (with-diagnostics-context! &form &env
    (with-compilation-opts! {:suppress-reporting #{:dynamic-property-access}}
      (gen-oapply obj selector args))))
