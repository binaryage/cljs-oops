(ns oops.runtime
  (:refer-clojure :exclude [gensym])
  (:require [oops.config :as config]
            [oops.codegen :refer :all]
            [oops.helpers :refer [gensym]]
            [oops.constants :refer [dot-access soft-access punch-access]]
            [oops.debug :refer [log debug-assert]]))

(defmacro report-error-dynamically [msg data]
  `(when-not (oops.state/was-error-reported?)                                                                                 ; we want to print only first error for single invocation
     (oops.state/mark-error-reported!)
     ~(gen-report-runtime-message :error msg data)))

(defmacro report-warning-dynamically [msg data]
  (gen-report-runtime-message :warning msg data))

(defmacro report-if-needed-dynamically [msg-id info-sym]
  (debug-assert (symbol? info-sym))
  (if (config/diagnostics?)
    `(do
       (debug-assert (oops.config/has-config-key? ~msg-id) (str "runtime config has missing key: " ~msg-id))
       (if-not ~(gen-supress-reporting? msg-id)
         (case (oops.config/get-config-key ~msg-id)
           :warn (oops.core/report-warning-dynamically (oops.messages/runtime-message ~msg-id ~info-sym) ~info-sym)
           :error (oops.core/report-error-dynamically (oops.messages/runtime-message ~msg-id ~info-sym) ~info-sym)
           (false nil) nil))
       nil)))

(defmacro validate-object-access-dynamically [obj-sym mode-sym key-sym check-key?]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? mode-sym))
  (debug-assert (symbol? key-sym))
  `(when ~(gen-dynamic-object-access-validation obj-sym mode-sym)
     (oops.state/add-key-to-current-path! ~key-sym)
     (oops.state/set-last-access-modifier! ~mode-sym)
     (if ~check-key?
       ~(gen-check-key-access obj-sym mode-sym key-sym)
       true)))

(defmacro validate-fn-call-dynamically [fn-sym mode-sym]
  (debug-assert (symbol? fn-sym))
  (debug-assert (symbol? mode-sym))
  `(cond
     (and (= ~mode-sym ~soft-access) (nil? ~fn-sym)) true
     (goog/isFunction ~fn-sym) true
     :else ~(gen-report-if-needed :expected-function-value `{:obj   (oops.state/get-target-object)
                                                             :path  (oops.state/get-key-path-str)
                                                             :fn    ~fn-sym
                                                             :soft? (= ~mode-sym ~soft-access)})))

(defmacro build-path-dynamically [selector-sym]
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

(defmacro get-key-dynamically [obj-sym key-sym mode]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? key-sym))
  (gen-instrumented-key-get obj-sym key-sym mode))

(defmacro set-key-dynamically [obj-sym key-sym val-sym mode]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? key-sym))
  (debug-assert (symbol? val-sym))
  (gen-instrumented-key-set obj-sym key-sym val-sym mode))

(defmacro get-selector-dynamically [obj-sym selector-sym]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? selector-sym))
  (let [path-code (gen-checked-build-path selector-sym)]
    (gen-dynamic-selector-validation-wrapper selector-sym (gen-dynamic-path-get obj-sym path-code))))

(defmacro set-selector-dynamically [obj-sym selector-sym val-sym]
  (debug-assert (symbol? obj-sym))
  (debug-assert (symbol? selector-sym))
  (debug-assert (symbol? val-sym))
  (let [path-code (gen-checked-build-path selector-sym)]
    (gen-dynamic-selector-validation-wrapper selector-sym (gen-dynamic-path-set obj-sym path-code val-sym))))

(defmacro punch-key-dynamically [obj-sym key-sym]
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
