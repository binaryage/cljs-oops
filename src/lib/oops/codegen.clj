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

(defn gen-suppress-reporting? [msg-id]
  `(contains? (oops.config/get-suppress-reporting) ~msg-id))

(defn gen-report-if-needed [msg-id & [info]]
  (debug-assert (keyword? msg-id))
  `(oops.core/report-if-needed-dynamically ~msg-id ~info))

(defn gen-selector-list [items]
  `(cljs.core/array ~@items))

(defn gen-object-access-validation-error [obj-sym flavor soft?]
  (debug-assert (symbol? obj-sym))
  (debug-assert (string? flavor))
  (debug-assert (instance? Boolean soft?))
  (let [info `{:obj    (oops.state/get-target-object)
               :path   (oops.state/get-key-path-str)
               :flavor ~flavor}
        reporting-code (gen-report-if-needed :unexpected-object-value info)]
    `(if ~(gen-suppress-reporting? :unexpected-object-value)
       true                                                                                                                   ; see https://github.com/binaryage/cljs-oops/issues/13
       (do
         ~reporting-code
         ~soft?))))

(defn gen-dynamic-object-access-validation [obj-sym mode-sym]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  (let [dot-access? `(= ~mode-sym ~dot-access)]
    `(cond
       (and ~dot-access? (cljs.core/undefined? ~obj-sym)) ~(gen-object-access-validation-error obj-sym "undefined" false)
       (and ~dot-access? (cljs.core/nil? ~obj-sym)) ~(gen-object-access-validation-error obj-sym "nil" false)
       (goog/isBoolean ~obj-sym) ~(gen-object-access-validation-error obj-sym "boolean" false)
       (goog/isNumber ~obj-sym) ~(gen-object-access-validation-error obj-sym "number" false)
       (goog/isString ~obj-sym) ~(gen-object-access-validation-error obj-sym "string" false)
       (not (goog/isObject ~obj-sym)) ~(gen-object-access-validation-error obj-sym "non-object" false)
       (goog/isDateLike ~obj-sym) ~(gen-object-access-validation-error obj-sym "date-like" true)
       (oops.helpers/cljs-type? ~obj-sym) ~(gen-object-access-validation-error obj-sym "cljs type" true)
       (oops.helpers/cljs-instance? ~obj-sym) ~(gen-object-access-validation-error obj-sym "cljs instance" true)
       ; note: constructors are functions and sometimes it is handy to oget some stuff from them
       ; (goog/isFunction ~obj-sym) ~(gen-object-access-validation-error obj-sym "function")
       ; note: it makes sense to use arrays as target objects, selectors can use numeric indices
       :else true)))

(defn gen-key-get [obj key]
  (case (config/key-get-mode)
    :core `(~'js* "(~{}[~{}])" ~obj ~key)                                                                                     ; using aget could raise a warning, see CLJS-2148
    :goog `(goog.object/get ~obj ~key)))

(defn gen-key-set [obj key val]
  (case (config/key-set-mode)
    :core `(~'js* "(~{}[~{}] = ~{})" ~obj ~key ~val)                                                                          ; using aset could raise a warning, see CLJS-2148
    :goog `(goog.object/set ~obj ~key ~val)))

(defn gen-dynamic-object-access-validation-wrapper [obj-sym mode key push? check-key-read? check-key-write? body]
  (debug-assert (symbol? obj-sym))
  (if (config/diagnostics?)
    `(when (oops.core/validate-object-access-dynamically ~obj-sym ~mode ~key ~push? ~check-key-read? ~check-key-write?)
       ~body)
    body))

(defn gen-instrumented-key-get [obj-sym key mode]
  (debug-assert (symbol? obj-sym))
  (gen-dynamic-object-access-validation-wrapper obj-sym mode key true true false
                                                (gen-key-get obj-sym key)))

(defn gen-instrumented-key-set [obj-sym key val mode push?]
  (debug-assert (symbol? obj-sym))
  (gen-dynamic-object-access-validation-wrapper obj-sym mode key push? (config/strict-punching?) true
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
             (when (some? ~next-obj-sym)
               ~(gen-static-path-get next-obj-sym (rest path))))
        2 (let [ensured-obj-sym (gensym "ensured-obj")]
            `(let [~next-obj-sym ~(gen-instrumented-key-get obj-sym key mode)
                   ~ensured-obj-sym (if (nil? ~next-obj-sym)
                                      (oops.core/punch-key-dynamically! ~obj-sym ~key)
                                      ~next-obj-sym)]
               ~(gen-static-path-get ensured-obj-sym (rest path))))))))

(defn gen-static-path-call-info [obj-sym path]
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
           (let [~mode-sym (oops.helpers/unchecked-aget ~path-sym ~i-sym)
                 ~key-sym (oops.helpers/unchecked-aget ~path-sym (inc ~i-sym))
                 ~next-obj-sym (oops.core/get-key-dynamically ~obj-sym ~key-sym ~mode-sym)]
             (case ~mode-sym
               ~dot-access (recur ~next-i ~next-obj-sym)
               ~soft-access (when (some? ~next-obj-sym)
                              (recur ~next-i ~next-obj-sym))
               ~punch-access (if-not (nil? ~next-obj-sym)
                               (recur ~next-i ~next-obj-sym)
                               (recur ~next-i (oops.core/punch-key-dynamically! ~obj-sym ~key-sym)))))
           ~obj-sym)))))

(defn gen-dynamic-path-call-info [obj-sym path]
  ; this should mimic gen-static-path-call-info
  ; note that dynamic paths are flat arrays of [modifier, key] values
  (let [path-sym (gensym "path")
        len-sym (gensym "len")
        target-obj-sym (gensym "target-obj")
        target-obj-path-code `(.slice ~path-sym 0 (- ~len-sym 2))
        last-obj-path-code `(cljs.core/array
                              (oops.helpers/unchecked-aget ~path-sym (- ~len-sym 2))
                              (oops.helpers/unchecked-aget ~path-sym (- ~len-sym 1)))]                                        ; not sure if .slice would be faster here
    `(let [~path-sym ~path
           ~len-sym (cljs.core/alength ~path-sym)]
       (if (< ~len-sym 4)
         (cljs.core/array ~obj-sym ~(gen-dynamic-path-get obj-sym path-sym))
         (let [~target-obj-sym ~(gen-dynamic-path-get obj-sym target-obj-path-code)]
           (cljs.core/array ~target-obj-sym ~(gen-dynamic-path-get target-obj-sym last-obj-path-code)))))))

(defn gen-dynamic-selector-get* [obj selector-list api-sym]
  (debug-assert (symbol? api-sym))
  (report-dynamic-selector-usage-if-needed! selector-list)
  (debug-assert (pos? (count selector-list)) "empty selector list should take static path")
  (case (count selector-list)
    1 `(~api-sym ~obj ~(first selector-list))                                                                                 ; we want to unwrap selector wrapped in oget (in this case)
    `(~api-sym ~obj ~(gen-selector-list selector-list))))

(defn gen-dynamic-selector-get [obj selector-list]
  (gen-dynamic-selector-get* obj selector-list 'oops.core/get-selector-dynamically))

(defn gen-dynamic-selector-call-info [obj selector-list]
  (gen-dynamic-selector-get* obj selector-list 'oops.core/get-selector-call-info-dynamically))

(defn gen-dynamic-selector-validation [selector-sym]
  (debug-assert (symbol? selector-sym))
  (let [explanation-sym (gensym "explanation")]
    `(if-not (clojure.spec.alpha/valid? :oops.sdefs/obj-selector ~selector-sym)
       (let [~explanation-sym (clojure.spec.alpha/explain-data :oops.sdefs/obj-selector ~selector-sym)]
         ~(gen-report-if-needed :invalid-selector `{:selector    ~selector-sym
                                                    :explanation ~explanation-sym}))
       true)))

(defn gen-dynamic-selector-validation-wrapper [selector-sym body]
  (debug-assert (symbol? selector-sym))
  (if (config/diagnostics?)
    `(when ~(gen-dynamic-selector-validation selector-sym)
       ~body)
    body))

(defn gen-checked-build-path [selector-sym op]
  (debug-assert (symbol? selector-sym))
  (let [path-sym (gensym "path")]
    `(let [~path-sym (oops.core/build-path-dynamically ~selector-sym)]
       ~(when (config/diagnostics?)
          `(oops.core/check-path-dynamically ~path-sym ~op))
       ~path-sym)))

(defn gen-static-path-set [obj-sym path val]
  (debug-assert (symbol? obj-sym))
  (when-not (empty? path)
    (let [parent-obj-path (butlast path)
          [mode key] (last path)
          parent-obj-sym (gensym "parent-obj")]
      `(let [~parent-obj-sym ~(gen-static-path-get obj-sym parent-obj-path)]
         ~(gen-instrumented-key-set parent-obj-sym key val mode true)))))

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
           ~key-sym (oops.helpers/unchecked-aget ~path-sym (- ~len-sym 1))
           ~mode-sym (oops.helpers/unchecked-aget ~path-sym (- ~len-sym 2))
           ~parent-obj-sym ~(gen-dynamic-path-get obj-sym parent-obj-path-sym)]
       (oops.core/set-key-dynamically ~parent-obj-sym ~key-sym ~val ~mode-sym))))

(defn gen-reported-message [msg]
  msg)

(defn gen-reported-data [data]
  `(oops.helpers/wrap-data-in-enveloper-if-possible (oops.config/use-envelope?) ~data))

(defn gen-console-method [kind]
  (case kind
    :error `(oops.helpers/unchecked-aget js/console "error")
    :warning `(oops.helpers/unchecked-aget js/console "warn")))

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
  (when (config/diagnostics?)
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
; also to avoid busy-work generated by ClojureScript compiler handling varargs
(def console-reporter-template "function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))}")

(defn gen-runtime-diagnostics-context! [obj-sym & body]
  (debug-assert (symbol? obj-sym))
  (let [body-code `(do ~@body)]
    (if-not (config/diagnostics?)
      body-code
      (let [console-reporter (list 'js* console-reporter-template)
            call-site-error `(js/Error.)]
        ; it is important to keep console-reporter and call-site-error inline so we get proper call-site location and line number
        `(binding [oops.state/*runtime-state* (oops.state/prepare-state ~obj-sym ~call-site-error ~console-reporter)]
           ~(gen-debug-runtime-state-consistency-check body-code))))))

(defn gen-check-key-read-access [obj-sym mode-sym key]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  `(if (and (= ~mode-sym ~dot-access)
            (not (goog.object/containsKey ~obj-sym ~key)))
     ~(gen-report-if-needed :missing-object-key `{:obj  (oops.state/get-target-object)
                                                  :key  ~key
                                                  :path (oops.state/get-key-path-str)})
     true))

(defn gen-check-key-write-access [obj-sym mode-sym key]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  (let [descriptor-sym (gensym "descriptor")
        reason-sym (gensym "reason")]
    `(if-some [~descriptor-sym (oops.helpers/get-property-descriptor ~obj-sym ~key)]
       (if-some [~reason-sym (oops.helpers/determine-property-non-writable-reason ~descriptor-sym)]
         ~(gen-report-if-needed :object-key-not-writable `{:obj     (oops.state/get-target-object)
                                                           :key     ~key
                                                           :reason  ~reason-sym
                                                           :frozen? (oops.helpers/is-object-frozen? ~obj-sym)
                                                           :path    (oops.state/get-key-path-str)})
         true)
       (cond
         ; note that frozen object imply sealed
         (oops.helpers/is-object-frozen? ~obj-sym)
         ~(gen-report-if-needed :object-is-frozen `{:obj  (oops.state/get-target-object)
                                                    :key  ~key
                                                    :path (oops.state/get-key-path-str)})

         (oops.helpers/is-object-sealed? ~obj-sym)
         ~(gen-report-if-needed :object-is-sealed `{:obj  (oops.state/get-target-object)
                                                    :key  ~key
                                                    :path (oops.state/get-key-path-str)})

         :else true))))

(defn gen-dynamic-fn-call-validation-wrapper [fn-sym body]
  (debug-assert (symbol? fn-sym))
  (if (config/diagnostics?)
    `(when (oops.core/validate-fn-call-dynamically ~fn-sym (oops.state/get-last-access-modifier))                             ; we rely on previous oget to record last access modifier of the selector
       ~body)
    body))

; -- raw implementations ----------------------------------------------------------------------------------------------------

; we want to tag our symbol as a generic js/Function to prevent infer warnings
; see https://github.com/binaryage/cljs-oops/issues/21
(defn gen-fn-sym
  ([] (gen-fn-sym "fn"))
  ([name] (with-meta (gensym name) {:tag 'js/Function})))

(defn macroexpand-selector-list [selector-list]
  (if-not (config/macroexpand-selectors?)
    selector-list
    (map compiler/macroexpand selector-list)))

(defn gen-oget-impl [obj-sym selector-list]
  (debug-assert (symbol? obj-sym))
  (if-some [path (schema/selector->path selector-list)]
    (gen-static-path-get obj-sym (schema/check-static-path! path :get selector-list))
    (gen-dynamic-selector-get obj-sym selector-list)))

(defn gen-get-call-info-impl [obj-sym selector-list]
  (debug-assert (symbol? obj-sym))
  (if-some [path (schema/selector->path selector-list)]
    (gen-static-path-call-info obj-sym (schema/check-static-path! path :get selector-list))
    (gen-dynamic-selector-call-info obj-sym selector-list)))

(defn gen-oset-impl [obj-sym selector-list val]
  (debug-assert (symbol? obj-sym))
  (let [path (schema/selector->path selector-list)]
    `(do
       ~(if (some? path)
          (gen-static-path-set obj-sym (schema/check-static-path! path :set selector-list) val)
          (gen-dynamic-selector-set obj-sym selector-list val))
       ~obj-sym)))

(defn gen-callable [obj-sym selector-list fn-sym call-info-sym action]
  (debug-assert (symbol? obj-sym))
  `(let [~call-info-sym ~(gen-get-call-info-impl obj-sym selector-list)
         ~fn-sym (oops.helpers/unchecked-aget ~call-info-sym 1)]
     ~(gen-dynamic-fn-call-validation-wrapper fn-sym action)))

(defn gen-ocall-impl [obj-sym selector-list args]
  (let [fn-sym (gen-fn-sym)
        call-info-sym (gensym "call-info")
        action `(when (some? ~fn-sym)
                  (.call ~fn-sym (oops.helpers/unchecked-aget ~call-info-sym 0) ~@args))]
    (gen-callable obj-sym selector-list fn-sym call-info-sym action)))

(defn gen-oapply-impl [obj-sym selector-list args]
  (let [fn-sym (gen-fn-sym)
        call-info-sym (gensym "call-info")
        action `(when (some? ~fn-sym)
                  (.apply ~fn-sym (oops.helpers/unchecked-aget ~call-info-sym 0) (oops.helpers/to-native-array ~args)))]
    (gen-callable obj-sym selector-list fn-sym call-info-sym action)))

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
