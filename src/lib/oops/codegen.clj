(ns oops.codegen
  "Helpers for generating code for core macros.
  The generated code greatly depends on effective oops configuration (see defaults.clj). E.g. diagnostics, debugging and
  other settings."
  (:refer-clojure :exclude [gensym])
  (:require [oops.config :as config]
            [oops.schema :as schema]
            [oops.helpers :refer [gensym]]
            [oops.compiler :as compiler]
            [oops.constants :refer [dot-access soft-access punch-access]]
            [oops.reporting :refer [report-if-needed! report-offending-selector-if-needed!]]
            [oops.debug :refer [log debug-assert]]))

(defn find-first-dynamic-selector [selector-list]
  (first (remove schema/static-selector? selector-list)))

(defn report-dynamic-selector-usage-if-needed! [selector-list]
  (report-offending-selector-if-needed! (find-first-dynamic-selector selector-list) :dynamic-selector-usage))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn gen-report-if-needed [msg-id & [info]]
  (debug-assert (keyword? msg-id))
  `(oops.core/report-if-needed-dynamically ~msg-id ~info))

(defn gen-selector-list [items]
  `(cljs.core/array ~@items))

(defn gen-object-access-validation-error [obj-sym flavor]
  (debug-assert (symbol? obj-sym))
  (gen-report-if-needed :unexpected-object-value `{:obj    (oops.state/get-target-object)
                                                   :path   (oops.state/get-key-path-str)
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
     ; note: constructors are functions and sometimes it is handy to oget some stuff from them
     ; (goog/isFunction ~obj-sym) ~(gen-object-access-validation-error obj-sym "function")
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
    `(if (oops.core/validate-object-access-dynamically ~obj-sym ~mode ~key ~check-key?)
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

(defn gen-static-path-get [obj-sym path]
  (debug-assert (symbol? obj-sym))
  (if (empty? path)
    obj-sym
    (let [[mode key] (first path)
          next-obj-sym (gensym "next-obj")]
      (debug-assert (string? key))
      ; http://stackoverflow.com/questions/32300269/make-vars-constant-for-use-in-case-statements-in-clojure
      (debug-assert (= dot-access 0))
      (debug-assert (= soft-access 1))
      (debug-assert (= punch-access 2))
      (case mode
        0 `(let [~next-obj-sym ~(gen-instrumented-key-get obj-sym key mode)]
             ~(gen-static-path-get next-obj-sym (rest path)))
        1 `(let [~next-obj-sym ~(gen-instrumented-key-get obj-sym key mode)]
             (if-not (nil? ~next-obj-sym)
               ~(gen-static-path-get next-obj-sym (rest path))))
        2 (let [ensured-obj-sym (gensym "ensured-obj")]
            `(let [~next-obj-sym ~(gen-instrumented-key-get obj-sym key mode)
                   ~ensured-obj-sym (if (nil? ~next-obj-sym)
                                      (oops.core/punch-key-dynamically! ~obj-sym ~key)
                                      ~next-obj-sym)]
               ~(gen-static-path-get ensured-obj-sym (rest path))))))))

(defn gen-static-path-get2 [obj-sym path]
  (if (< (count path) 2)
    `(cljs.core/array ~obj-sym ~(gen-static-path-get obj-sym path))
    (let [last-obj-path [(last path)]
          target-obj-path (butlast path)
          target-obj-sym (gensym "target-obj")
          target-obj-get-code (gen-static-path-get obj-sym target-obj-path)
          last-obj-get-code (gen-static-path-get target-obj-sym last-obj-path)]
      `(let [~target-obj-sym ~target-obj-get-code]
         (cljs.core/array ~target-obj-sym ~last-obj-get-code)))))

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
                 ~next-obj-sym (oops.core/get-key-dynamically ~obj-sym ~key-sym ~mode-sym)]
             (case ~mode-sym
               ~dot-access (recur ~next-i ~next-obj-sym)
               ~soft-access (if-not (nil? ~next-obj-sym)
                              (recur ~next-i ~next-obj-sym))
               ~punch-access (if-not (nil? ~next-obj-sym)
                               (recur ~next-i ~next-obj-sym)
                               (recur ~next-i (oops.core/punch-key-dynamically! ~obj-sym ~key-sym)))))
           ~obj-sym)))))

(defn gen-dynamic-path-get2 [obj-sym path]
  ; this should mimic gen-static-path-get2
  ; note that dynamic paths are flat arrays of [modifier, key] values
  (let [path-sym (gensym "path")
        len-sym (gensym "len")
        target-obj-sym (gensym "target-obj")
        target-obj-path-code `(.slice ~path-sym 0 (- ~len-sym 2))
        last-obj-path-code `(cljs.core/array (aget ~path-sym (- ~len-sym 2)) (aget ~path-sym (- ~len-sym 1)))]                ; not sure if .slice would be faster here
    `(let [~path-sym ~path
           ~len-sym (cljs.core/alength ~path-sym)]
       (if (< ~len-sym 4)
         (cljs.core/array ~obj-sym ~(gen-dynamic-path-get obj-sym path-sym))
         (let [~target-obj-sym ~(gen-dynamic-path-get obj-sym target-obj-path-code)]
           (cljs.core/array ~target-obj-sym ~(gen-dynamic-path-get target-obj-sym last-obj-path-code)))))))

(defn gen-dynamic-selector-get [obj selector-list]
  (report-dynamic-selector-usage-if-needed! selector-list)
  (debug-assert (pos? (count selector-list)) "empty selector list should take static path")
  (case (count selector-list)
    1 `(oops.core/get-selector-dynamically ~obj ~(first selector-list))                                                       ; we want to unwrap selector wrapped in oget (in this case)
    `(oops.core/get-selector-dynamically ~obj ~(gen-selector-list selector-list))))

(defn gen-dynamic-selector-get2 [obj selector-list]
  (report-dynamic-selector-usage-if-needed! selector-list)
  (debug-assert (pos? (count selector-list)) "empty selector list should take static path")
  (case (count selector-list)
    1 `(oops.core/get2-selector-dynamically ~obj ~(first selector-list))                                                      ; we want to unwrap selector wrapped in oget (in this case)
    `(oops.core/get2-selector-dynamically ~obj ~(gen-selector-list selector-list))))

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

(defn gen-checked-build-path [selector-sym op]
  (debug-assert (symbol? selector-sym))
  (let [path-sym (gensym "path")]
    `(let [~path-sym (oops.core/build-path-dynamically ~selector-sym)]
       ~(if (config/diagnostics?)
          `(oops.core/check-path-dynamically ~path-sym ~op))
       ~path-sym)))

(defn gen-static-path-set [obj-sym path val]
  (debug-assert (symbol? obj-sym))
  (if-not (empty? path)
    (let [parent-obj-path (butlast path)
          [mode key] (last path)
          parent-obj-sym (gensym "parent-obj")]
      `(let [~parent-obj-sym ~(gen-static-path-get obj-sym parent-obj-path)]
         ~(gen-instrumented-key-set parent-obj-sym key val mode)))))

(defn gen-dynamic-selector-set [obj selector-list val]
  (report-dynamic-selector-usage-if-needed! selector-list)
  (debug-assert (pos? (count selector-list)) "empty selector list should take static path")
  (case (count selector-list)
    1 `(oops.core/set-selector-dynamically ~obj ~(first selector-list) ~val)                                                  ; we want to unwrap selector wrapped in oset! (in this case)
    `(oops.core/set-selector-dynamically ~obj ~(gen-selector-list selector-list) ~val)))

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
       (oops.core/set-key-dynamically ~parent-obj-sym ~key-sym ~val ~mode-sym))))

(defn gen-reported-message [msg]
  msg)

(defn gen-reported-data [data]
  `(let [data# ~data]
     (or (if (oops.config/use-envelope?)
           (if-let [devtools# (cljs.core/aget js/window "devtools")]
             (if-let [toolbox# (cljs.core/aget devtools# "toolbox")]
               (if-let [envelope# (cljs.core/aget toolbox# "envelope")]
                 (if (cljs.core/fn? envelope#)
                   (envelope# data# "details"))))))
         data#)))

(defn gen-console-method [kind]
  (case kind
    :error `(aget js/console "error")
    :warning `(aget js/console "warn")))

(defn gen-report-runtime-message [kind msg data]
  (debug-assert (contains? #{:error :warning} kind))
  (let [mode (case kind
               :error `(oops.config/get-error-reporting)
               :warning `(oops.config/get-warning-reporting))]
    `(case ~mode
       :throw (throw (oops.state/prepare-error-from-call-site ~(gen-reported-message msg) ~(gen-reported-data data)))
       :console ((oops.state/get-console-reporter) ~(gen-console-method kind) ~(gen-reported-message msg) ~(gen-reported-data data))
       false nil)))

(defn validate-object-statically [obj]
  ; here we can try to detect some pathological cases and warn user at compile-time
  ; TODO: to be strict we should allow just symbols, lists (other than data lists) and JSValue objects
  (if (config/diagnostics?)
    (cond
      (nil? obj) (report-if-needed! :static-nil-target-object))))

(defn gen-debug-runtime-state-consistency-check [body]
  (if-not (config/debug?)
    body
    (let [captured-runtime-state-sym (gensym "captured-runtime-state")
          result-sym (gensym "result")]
      ; we don't want body to change *runtime-state* without restoring
      ; this could theoretically happen with code-rewriting when using go-macros
      `(let [~captured-runtime-state-sym oops.state/*runtime-state*
             ~result-sym ~body]
         (assert (identical? ~captured-runtime-state-sym oops.state/*runtime-state*))
         ~result-sym))))

; this method is hand-written to reduce space (it will be emitted on each macro call-site)
; also to avoid busy-work generated by clojuscript compiler handling varargs
(def console-reporter-template "function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))}")

(defn gen-runtime-diagnostics-context! [obj-sym & body]
  (debug-assert (symbol? obj-sym))
  (let [body-code `(do ~@body)]
    (if-not (config/diagnostics?)
      body-code
      (let [console-reporter (list 'js* console-reporter-template)
            call-site-error `(js/Error.)]
        ; it is imporant to keep console-reporter and call-site-error inline so we get proper call-site location and line number
        `(binding [oops.state/*runtime-state* (oops.state/prepare-state ~obj-sym ~call-site-error ~console-reporter)]
           ~(gen-debug-runtime-state-consistency-check body-code))))))

(defn gen-supress-reporting? [msg-id]
  `(contains? (oops.config/get-suppress-reporting) ~msg-id))

(defn gen-check-key-access [obj-sym mode-sym key]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  `(if (and (= ~mode-sym ~dot-access)
            (not (goog.object/containsKey ~obj-sym ~key)))
     ~(gen-report-if-needed :missing-object-key `{:obj  (oops.state/get-target-object)
                                                  :key  ~key
                                                  :path (oops.state/get-key-path-str)})
     true))

(defn gen-dynamic-fn-call-validation-wrapper [fn-sym body]
  (debug-assert (symbol? fn-sym))
  (if (config/diagnostics?)
    `(if (oops.core/validate-fn-call-dynamically ~fn-sym (oops.state/get-last-access-modifier))                               ; we rely on previous oget to record last access modifier of the selector
       ~body)
    body))

; -- raw implementations ----------------------------------------------------------------------------------------------------

(defn macroexpand-selector-list [selector-list]
  (if-not (config/macroexpand-selectors?)
    selector-list
    (map compiler/macroexpand selector-list)))

(defn gen-oget-impl [obj-sym selector-list]
  (debug-assert (symbol? obj-sym))
  (if-let [path (schema/selector->path selector-list)]
    (gen-static-path-get obj-sym (schema/check-static-path! path :get selector-list))
    (gen-dynamic-selector-get obj-sym selector-list)))

(defn gen-oget2-impl [obj-sym selector-list]
  (debug-assert (symbol? obj-sym))
  (if-let [path (schema/selector->path selector-list)]
    (gen-static-path-get2 obj-sym (schema/check-static-path! path :get selector-list))
    (gen-dynamic-selector-get2 obj-sym selector-list)))

(defn gen-oset-impl [obj-sym selector-list val]
  (debug-assert (symbol? obj-sym))
  (let [path (schema/selector->path selector-list)]
    `(do
       ~(if path
          (gen-static-path-set obj-sym (schema/check-static-path! path :set selector-list) val)
          (gen-dynamic-selector-set obj-sym selector-list val))
       ~obj-sym)))

(defn gen-ocall-impl [obj-sym selector-list args]
  (debug-assert (symbol? obj-sym))
  (let [fn-sym (gensym "fn")
        call-info-sym (gensym "call-info")
        action `(if-not (nil? ~fn-sym)
                  (.call ~fn-sym (aget ~call-info-sym 0) ~@args))]
    `(let [~call-info-sym ~(gen-oget2-impl obj-sym selector-list)
           ~fn-sym (aget ~call-info-sym 1)]
       ~(gen-dynamic-fn-call-validation-wrapper fn-sym action))))

(defn gen-oapply-impl [obj-sym selector-list args]
  (debug-assert (symbol? obj-sym))
  (let [fn-sym (gensym "fn")
        call-info-sym (gensym "call-info")
        action `(if-not (nil? ~fn-sym)
                  (.apply ~fn-sym (aget ~call-info-sym 0) (oops.helpers/to-native-array ~args)))]
    `(let [~call-info-sym ~(gen-oget2-impl obj-sym selector-list)
           ~fn-sym (aget ~call-info-sym 1)]
       ~(gen-dynamic-fn-call-validation-wrapper fn-sym action))))

; -- shared macro bodies ----------------------------------------------------------------------------------------------------

(defn gen-oget [obj selector-list]
  (validate-object-statically obj)
  (let [target-obj-sym (gensym "target-obj")
        expanded-selector-list (macroexpand-selector-list selector-list)]
    `(let [~target-obj-sym ~obj]
       ~(gen-runtime-diagnostics-context! target-obj-sym
                                          (gen-oget-impl target-obj-sym expanded-selector-list)))))

(defn gen-oset [obj selector+val]
  (validate-object-statically obj)
  (let [selector-list (butlast selector+val)
        expanded-selector-list (macroexpand-selector-list selector-list)
        val (last selector+val)
        target-obj-sym (gensym "target-obj")]
    `(let [~target-obj-sym ~obj]
       ~(gen-runtime-diagnostics-context! target-obj-sym
                                          (gen-oset-impl target-obj-sym expanded-selector-list val)))))

(defn gen-ocall [obj selector args]
  (validate-object-statically obj)
  (let [selector-list [selector]
        expanded-selector-list (macroexpand-selector-list selector-list)
        target-obj-sym (gensym "target-obj")]
    `(let [~target-obj-sym ~obj]
       ~(gen-runtime-diagnostics-context! target-obj-sym
                                          (gen-ocall-impl target-obj-sym expanded-selector-list args)))))

(defn gen-oapply [obj selector+args]
  (validate-object-statically obj)
  (let [selector-list (butlast selector+args)
        expanded-selector-list (macroexpand-selector-list selector-list)
        args (last selector+args)
        target-obj-sym (gensym "target-obj")]
    `(let [~target-obj-sym ~obj]
       ~(gen-runtime-diagnostics-context! target-obj-sym
                                          (gen-oapply-impl target-obj-sym expanded-selector-list args)))))
