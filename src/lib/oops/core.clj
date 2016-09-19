(ns oops.core
  (:refer-clojure :exclude [gensym])
  (:require [oops.schema :as schema]
            [oops.config :as config]
            [oops.compiler :as compiler :refer [gensym with-diagnostics-context! with-compilation-opts!]]
            [oops.constants :refer [dot-access soft-access punch-access]]
            [oops.debug :refer [log debug-assert]]
            [oops.state :as state]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn supress-reporting? [type]
  (boolean (get-in oops.state/*invocation-opts* [:suppress-reporting type])))

(defn report! [type & [info]]
  (case (config/get-config-key type)
    :warn (compiler/warn! type info)
    :error (compiler/error! type info)
    (false nil) nil))

(defn report-if-needed! [type & [info]]
  (if (config/diagnostics?)
    (if-not (supress-reporting? type)
      (report! type info))))

(defn find-first-dynamic-selector [selector-list]
  (first (remove schema/static-selector? selector-list)))

(defn report-dynamic-selector-usage-if-needed! [selector-list]
  (if (config/diagnostics?)
    (if-not (supress-reporting? :dynamic-selector-usage)
      (let [offending-selector (find-first-dynamic-selector selector-list)]
        (debug-assert offending-selector)
        (let [point-to-offending-selector (into {} (filter second (select-keys (meta offending-selector) [:line :column])))]
          ; note that sometimes param meta could be missing, we don't alter state/*invocation-env* in that case
          (binding [state/*invocation-env* (merge state/*invocation-env* point-to-offending-selector)]
            (report! :dynamic-selector-usage)))))))

(defn gen-report-if-needed [msg-id & [info]]
  (debug-assert (keyword? msg-id))
  `(report-if-needed-dynamically ~msg-id ~info))

(defn gen-selector-list [items]
  `(cljs.core/array ~@items))

(defn gen-object-access-validation-error [obj-sym flavor]
  (debug-assert (symbol? obj-sym))
  (gen-report-if-needed :unexpected-object-value `{:obj    (oops.state/get-current-target-object)
                                                   :path   (oops.state/get-current-key-path-str)
                                                   :flavor ~flavor}))

(defn gen-dynamic-object-access-validation [obj-sym mode-sym]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  `(cond
     (and (= ~mode-sym ~dot-access) (cljs.core/undefined? ~obj-sym)) ~(gen-object-access-validation-error obj-sym "undefined")
     (and (= ~mode-sym ~dot-access) (cljs.core/nil? ~obj-sym)) ~(gen-object-access-validation-error obj-sym "nil")
     (goog/isBoolean ~obj-sym) ~(gen-object-access-validation-error obj-sym "boolean")
     (goog/isNumber ~obj-sym) ~(gen-object-access-validation-error obj-sym "number")
     (goog/isString ~obj-sym) ~(gen-object-access-validation-error obj-sym "string")
     (not (goog/isObject ~obj-sym)) ~(gen-object-access-validation-error obj-sym "non-object")
     (goog/isDateLike ~obj-sym) ~(gen-object-access-validation-error obj-sym "date-like")
     (oops.helpers/cljs-type? ~obj-sym) ~(gen-object-access-validation-error obj-sym "cljs type")
     (oops.helpers/cljs-instance? ~obj-sym) ~(gen-object-access-validation-error obj-sym "cljs instance")
     (goog/isFunction ~obj-sym) ~(gen-object-access-validation-error obj-sym "function")
     ; note: it makes sense to use arrays as target objects, selectors can use numeric indices
     :else true))

(defn gen-key-get [obj key]
  (case (config/key-get-mode)
    :core `(cljs.core/aget ~obj ~key)                                                                                         ; => `(~'js* "(~{}[~{}])" ~obj ~key)
    :goog `(goog.object/get ~obj ~key)))

(defn gen-key-set [obj key val]
  (case (config/key-set-mode)
    :core `(cljs.core/aset ~obj ~key ~val)                                                                                    ; => `(~'js* "(~{}[~{}] = ~{})" ~obj ~key ~val)
    :goog `(goog.object/set ~obj ~key ~val)))

(defn gen-dynamic-object-access-validation-wrapper [obj-sym mode key check-key? body]
  (debug-assert (symbol? obj-sym))
  (if (config/diagnostics?)
    `(if (validate-object-access-dynamically ~obj-sym ~mode ~key ~check-key?)
       ~body)
    body))

(defn gen-instrumented-key-get [obj-sym key mode]
  (debug-assert (symbol? obj-sym))
  (gen-dynamic-object-access-validation-wrapper obj-sym mode key true
    (gen-key-get obj-sym key)))

(defn gen-instrumented-key-set [obj-sym key val mode]
  (debug-assert (symbol? obj-sym))
  (gen-dynamic-object-access-validation-wrapper obj-sym mode key (config/strict-punching?)
    (gen-key-set obj-sym key val)))

(defn gen-static-path-get [obj path]
  (if (empty? path)
    obj
    (let [[mode key] (first path)
          obj-sym (gensym "obj")
          next-obj-sym (gensym "next-obj")]
      (debug-assert (string? key))
      ; http://stackoverflow.com/questions/32300269/make-vars-constant-for-use-in-case-statements-in-clojure
      (debug-assert (= dot-access 0))
      (debug-assert (= soft-access 1))
      (debug-assert (= punch-access 2))
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
               ~(gen-static-path-get `(punch-key-dynamically! ~obj-sym ~key) (rest path))))))))

(defn gen-dynamic-path-get [initial-obj-sym path]
  (debug-assert (symbol? initial-obj-sym))
  (let [path-sym (gensym "path")
        len-sym (gensym "len")
        i-sym (gensym "i")
        obj-sym (gensym "obj")
        mode-sym (gensym "mode")
        key-sym (gensym "key")
        next-obj-sym (gensym "next-obj")
        next-i `(+ ~i-sym 2)]
    `(let [~path-sym ~path
           ~len-sym (cljs.core/alength ~path-sym)]
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
                               (recur ~next-i (punch-key-dynamically! ~obj-sym ~key-sym)))))
           ~obj-sym)))))

(defn gen-dynamic-selector-get [obj selector-list]
  (report-dynamic-selector-usage-if-needed! selector-list)
  (debug-assert (pos? (count selector-list)) "empty selector list should take static path")
  (case (count selector-list)
    1 `(get-selector-dynamically ~obj ~(first selector-list))                                                                 ; we want to unwrap selector wrapped in oget (in this case)
    `(get-selector-dynamically ~obj ~(gen-selector-list selector-list))))

(defn gen-dynamic-selector-validation [selector-sym]
  (debug-assert (symbol? selector-sym))
  (let [explanation-sym (gensym "explanation")]
    `(if-not (clojure.spec/valid? :oops.sdefs/obj-selector ~selector-sym)
       (let [~explanation-sym (clojure.spec/explain-data :oops.sdefs/obj-selector ~selector-sym)]
         ~(gen-report-if-needed :invalid-selector `{:selector    ~selector-sym
                                                    :explanation ~explanation-sym}))
       true)))

(defn gen-dynamic-selector-validation-wrapper [selector-sym body]
  (debug-assert (symbol? selector-sym))
  (if (config/diagnostics?)
    `(if ~(gen-dynamic-selector-validation selector-sym)
       ~body)
    body))

(defn gen-dynamic-path-check [path-sym]
  (debug-assert (symbol? path-sym))
  (if (config/diagnostics?)
    `(cond
       (cljs.core/empty? ~path-sym) ~(gen-report-if-needed :empty-selector-access))))

(defn gen-checked-build-path [selector-sym]
  (debug-assert (symbol? selector-sym))
  (let [path-sym (gensym "path")]
    `(let [~path-sym (build-path-dynamically ~selector-sym)]
       ~(gen-dynamic-path-check path-sym)
       ~path-sym)))

(defn gen-static-path-set [obj-sym path val]
  (debug-assert (not (empty? path)))
  (debug-assert (symbol? obj-sym))
  (let [parent-obj-path (butlast path)
        [mode key] (last path)
        parent-obj-sym (gensym "parent-obj")]
    `(let [~parent-obj-sym ~(gen-static-path-get obj-sym parent-obj-path)]
       ~(gen-instrumented-key-set parent-obj-sym key val mode))))

(defn gen-dynamic-selector-set [obj selector-list val]
  (report-dynamic-selector-usage-if-needed! selector-list)
  (debug-assert (pos? (count selector-list)) "empty selector list should take static path")
  (case (count selector-list)
    1 `(set-selector-dynamically ~obj ~(first selector-list) ~val)                                                            ; we want to unwrap selector wrapped in oset! (in this case)
    `(set-selector-dynamically ~obj ~(gen-selector-list selector-list) ~val)))

(defn gen-dynamic-path-set [obj-sym path val]
  (debug-assert (symbol? obj-sym))
  (let [path-sym (gensym "path")
        key-sym (gensym "key")
        mode-sym (gensym "mode")
        len-sym (gensym "len")
        parent-obj-path-sym (gensym "parent-obj-path")
        parent-obj-sym (gensym "parent-obj")]
    `(let [~path-sym ~path
           ~len-sym (cljs.core/alength ~path-sym)
           ~parent-obj-path-sym (.slice ~path-sym 0 (- ~len-sym 2))
           ~key-sym (aget ~path-sym (- ~len-sym 1))
           ~mode-sym (aget ~path-sym (- ~len-sym 2))
           ~parent-obj-sym ~(gen-dynamic-path-get obj-sym parent-obj-path-sym)]
       (set-key-dynamically ~parent-obj-sym ~key-sym ~val ~mode-sym))))

(defn gen-reported-message [msg]
  msg)

(defn gen-reported-data [data]
  `(let [data# ~data]
     (or (if-let [devtools# (cljs.core/aget js/window "devtools")]
           (if-let [toolbox# (cljs.core/aget devtools# "toolbox")]
             (if-let [envelope# (cljs.core/aget toolbox# "envelope")]
               (if (cljs.core/fn? envelope#)
                 (envelope# data# "details")))))
         data#)))

(defn gen-console-method [kind]
  (case kind
    :error `(aget js/console "error")
    :warning `(aget js/console "warn")))

(defn gen-report-runtime-message [kind msg data]
  (debug-assert (contains? #{:error :warning} kind))
  (let [console-reporter-sym (gensym "console-reporter")
        mode (case kind
               :error `(oops.config/get-error-reporting)
               :warning `(oops.config/get-warning-reporting))]
    `(case ~mode
       :throw (throw (ex-info ~(gen-reported-message msg) ~(gen-reported-data data)))
       :console (let [~console-reporter-sym (oops.state/get-console-reporter)]
                  (~console-reporter-sym ~(gen-console-method kind) ~(gen-reported-message msg) ~(gen-reported-data data)))
       false nil)))

(defn validate-object-statically [obj]
  ; here we can try to detect some pathological cases and warn user at compile-time
  ; TODO: to be strict we should allow just symbols, lists (other than data lists) and JSValue objects
  (if (config/diagnostics?)
    (cond
      (nil? obj) (report-if-needed! :static-nil-target-object))))

; this method is hand-written to reduce space (it will be emitted on each macro call-site)
; also to avoid busy-work generated by clojuscript compiler handling varargs
(def console-reporter-template "function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))}")

(defn gen-runtime-diagnostics-context! [_form _env obj & body]
  (let [console-reporter (list 'js* console-reporter-template)]
    (if (config/diagnostics?)
      `(binding [oops.state/*runtime-state* (oops.state/prepare-state ~console-reporter ~obj)]                                ; it is imporant to keep console-reporter inline so we get proper call-site location and line number
         ~@body)
      `(do
         ~@body))))

(defn gen-supress-reporting? [msg-id]
  `(contains? (oops.config/get-suppress-reporting) ~msg-id))

(defn gen-check-key-access [obj-sym mode-sym key]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  `(if (and (= ~mode-sym ~dot-access)
            (not (goog.object/containsKey ~obj-sym ~key)))
     ~(gen-report-if-needed :missing-object-key `{:obj  (oops.state/get-current-target-object)
                                                  :key  ~key
                                                  :path (oops.state/get-current-key-path-str)})
     true))

(defn gen-dynamic-fn-call-validation-wrapper [fn-sym body]
  (debug-assert (symbol? fn-sym))
  (if (config/diagnostics?)
    `(if (validate-fn-call-dynamically ~fn-sym (oops.state/get-last-access-modifier))                                         ; we rely on previous oget to record last access modifier of the selector
       ~body)
    body))

; -- helper macros ----------------------------------------------------------------------------------------------------------

(defmacro report-runtime-error-impl [msg data]
  `(when-not (oops.state/was-error-reported?)                                                                                 ; we want to print only first error for single invocation
     (oops.state/mark-error-reported!)
     ~(gen-report-runtime-message :error msg data)))

(defmacro report-runtime-warning-impl [msg data]
  (gen-report-runtime-message :warning msg data))

(defmacro report-if-needed-dynamically-impl [msg-id info-sym]
  (debug-assert (symbol? info-sym))
  (if (config/diagnostics?)
    `(do
       (if-not ~(gen-supress-reporting? msg-id)
         (case (oops.config/get-config-key ~msg-id)
           :warn (report-runtime-warning (oops.messages/runtime-message ~msg-id ~info-sym) ~info-sym)
           :error (report-runtime-error (oops.messages/runtime-message ~msg-id ~info-sym) ~info-sym)
           (false nil) nil))
       nil)))

(defmacro validate-object-access-dynamically-impl [obj-sym mode-sym key-sym check-key?]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  (debug-assert (symbol? key-sym))
  `(when ~(gen-dynamic-object-access-validation obj-sym mode-sym)
     (oops.state/add-key-to-current-path! ~key-sym)
     (oops.state/set-last-access-modifier! ~mode-sym)
     (if ~check-key?
       ~(gen-check-key-access obj-sym mode-sym key-sym)
       true)))

(defmacro validate-fn-call-dynamically-impl [fn-sym mode-sym]
  (debug-assert (symbol? fn-sym))
  (debug-assert (symbol? mode-sym))
  `(cond
     (and (= ~mode-sym ~soft-access) (nil? ~fn-sym)) true
     (goog/isFunction ~fn-sym) true
     :else ~(gen-report-if-needed :expected-function `{:obj   (oops.state/get-current-target-object)
                                                       :path  (oops.state/get-current-key-path-str)
                                                       :soft? (= ~mode-sym ~soft-access)})))

(defmacro build-path-dynamically-impl [selector-sym]
  (debug-assert (symbol? selector-sym))
  (let [atomic-case (let [path-sym (gensym "selector-path")]
                      `(let [~path-sym (cljs.core/array)]
                         (oops.schema/coerce-key-dynamically! ~selector-sym ~path-sym)
                         ~path-sym))
        collection-case (let [path-sym (gensym "selector-path")]
                          `(let [~path-sym (cljs.core/array)]
                             (oops.schema/collect-coerced-keys-into-array! ~selector-sym ~path-sym)
                             ~path-sym))
        build-path-code `(cond
                           (or (string? ~selector-sym) (keyword? ~selector-sym)) ~atomic-case
                           :else ~collection-case)]
    (if (config/debug?)
      `(let [path# ~build-path-code]
         (assert (clojure.spec/valid? :oops.sdefs/obj-path path#))
         path#)
      build-path-code)))

(defmacro get-key-dynamically-impl [obj-sym key-sym mode]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? key-sym))
  (gen-instrumented-key-get obj-sym key-sym mode))

(defmacro set-key-dynamically-impl [obj-sym key-sym val-sym mode]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? key-sym))
  (debug-assert (symbol? val-sym))
  (gen-instrumented-key-set obj-sym key-sym val-sym mode))

(defmacro get-selector-dynamically-impl [obj-sym selector-sym]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? selector-sym))
  (let [path-code (gen-checked-build-path selector-sym)]
    (gen-dynamic-selector-validation-wrapper selector-sym (gen-dynamic-path-get obj-sym path-code))))

(defmacro set-selector-dynamically-impl [obj-sym selector-sym val-sym]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? selector-sym))
  (debug-assert (symbol? val-sym))
  (let [path-code (gen-checked-build-path selector-sym)]
    (gen-dynamic-selector-validation-wrapper selector-sym (gen-dynamic-path-set obj-sym path-code val-sym))))

(defmacro punch-key-dynamically-impl [obj-sym key-sym]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? key-sym))
  (let [child-obj-sym (gensym "child-obj")
        child-factory-sym (gensym "child-factory")]
    `(let [~child-factory-sym (oops.config/get-child-factory)
           ~child-factory-sym (case ~child-factory-sym
                                :js-obj #(cljs.core/js-obj)
                                :js-array #(cljs.core/array)
                                ~child-factory-sym)]
       (oops.debug/debug-assert (fn? ~child-factory-sym))
       (let [~child-obj-sym (~child-factory-sym ~obj-sym ~key-sym)]
         ~(gen-key-set obj-sym key-sym child-obj-sym)
         ~child-obj-sym))))

; -- raw implementations ----------------------------------------------------------------------------------------------------

(defn check-path! [path]
  (if (empty? path)
    (report-if-needed! :static-empty-selector-access))
  path)

(defn gen-oget [obj selector-list]
  (validate-object-statically obj)
  (if-let [path (schema/selector->path selector-list)]
    (gen-static-path-get obj (check-path! path))
    (gen-dynamic-selector-get obj selector-list)))

(defn gen-oset! [obj selector-list val]
  (validate-object-statically obj)
  (let [obj-sym (gensym "obj")
        path (schema/selector->path selector-list)]
    `(let [~obj-sym ~obj]
       ~(if path
          (gen-static-path-set obj-sym (check-path! path) val)
          (gen-dynamic-selector-set obj-sym selector-list val))
       ~obj-sym)))

(defn gen-ocall [obj selector-list args]
  (validate-object-statically obj)
  (let [obj-sym (gensym "obj")
        fn-sym (gensym "fn")
        action `(if-not (nil? ~fn-sym)
                  (.call ~fn-sym ~obj-sym ~@args))]
    `(let [~obj-sym ~obj
           ~fn-sym ~(gen-oget obj-sym selector-list)]
       ~(gen-dynamic-fn-call-validation-wrapper fn-sym action))))

(defn gen-oapply [obj selector-list args]
  (validate-object-statically obj)
  (let [obj-sym (gensym "obj")
        fn-sym (gensym "fn")
        action `(if-not (nil? ~fn-sym)
                  (.apply ~fn-sym ~obj-sym (into-array ~args)))]
    `(let [~obj-sym ~obj
           ~fn-sym ~(gen-oget obj-sym selector-list)]
       ~(gen-dynamic-fn-call-validation-wrapper fn-sym action))))

; -- public macros ----------------------------------------------------------------------------------------------------------

(defmacro oget [obj & selector]
  (with-diagnostics-context! &form &env obj
    (let [selector-list selector]
      (gen-oget obj selector-list))))

(defmacro oget+ [obj & selector]
  (with-diagnostics-context! &form &env obj
    (with-compilation-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (let [selector-list selector]
        (gen-oget obj selector-list)))))

(defmacro oset! [obj & selector+val]
  (with-diagnostics-context! &form &env obj
    (let [selector-list (butlast selector+val)
          val (last selector+val)]
      (gen-oset! obj selector-list val))))

(defmacro oset!+ [obj & selector+val]
  (with-diagnostics-context! &form &env obj
    (with-compilation-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (let [selector-list (butlast selector+val)
            val (last selector+val)]
        (gen-oset! obj selector-list val)))))

(defmacro ocall [obj selector & args]
  (with-diagnostics-context! &form &env obj
    (let [selector-list [selector]]
      (gen-ocall obj selector-list args))))

(defmacro ocall+ [obj selector & args]
  (with-diagnostics-context! &form &env obj
    (with-compilation-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (let [selector-list [selector]]
        (gen-ocall obj selector-list args)))))

(defmacro oapply [obj & selector+args]
  (with-diagnostics-context! &form &env obj
    (let [selector-list (butlast selector+args)
          args (last selector+args)]
      (gen-oapply obj selector-list args))))

(defmacro oapply+ [obj & selector+args]
  (with-diagnostics-context! &form &env obj
    (with-compilation-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (let [selector-list (butlast selector+args)
            args (last selector+args)]
        (gen-oapply obj selector-list args)))))

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-diagnostics-context! &form &env obj
    (let [selector-list [selector]]
      (gen-ocall obj selector-list args))))

(defmacro ocall!+
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-diagnostics-context! &form &env obj
    (with-compilation-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (let [selector-list [selector]]
        (gen-ocall obj selector-list args)))))

(defmacro oapply!
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj & selector+args]
  (with-diagnostics-context! &form &env obj
    (let [selector-list (butlast selector+args)
          args (last selector+args)]
      (gen-oapply obj selector-list args))))

(defmacro oapply!+
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj & selector+args]
  (with-diagnostics-context! &form &env obj
    (with-compilation-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (let [selector-list (butlast selector+args)
            args (last selector+args)]
        (gen-oapply obj selector-list args)))))
