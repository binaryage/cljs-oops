(ns oops.compiler
  "Provides some helper utils for interaction with cljs compiler."
  (:require [cljs.analyzer :as ana]
            [cljs.env]
            [oops.state :as state]
            [oops.debug :refer [log]]))

(def ^:dynamic *warnings-registered* false)

(defn register-warnings! [warnings-table]
  (assoc warnings-table
    :dynamic-property-access true))

(defmacro hook-compiler! [& body]
  (if *warnings-registered*
    `(do ~@body)
    `(binding [*warnings-registered* true
               ana/*cljs-warnings* (register-warnings! ana/*cljs-warnings*)]
       ~@body)))

(defmacro with-diagnostics-context! [form env opts & body]
  `(oops.compiler/hook-compiler!
     (binding [oops.state/*invoked-form* ~form
               oops.state/*invoked-env* ~env
               oops.state/*invoked-opts* ~opts]
       (oops.core/gen-diagnostics-context! ~form ~env ~@body))))

(defn annotate-with-state [info]
  (assoc info :form oops.state/*invoked-form*))

(defn warn! [type & [info]]
  (assert state/*invoked-env* "oops.state/*invoked-env* must be set via with-diagnostics-context! first!")
  (ana/warning type state/*invoked-env* (annotate-with-state info)))

(defn error! [type & [info]]
  (assert state/*invoked-env* "oops.state/*invoked-env* must be set via with-diagnostics-context! first!")
  (ana/error type state/*invoked-env* (annotate-with-state info)))

; -- error/warning messages -------------------------------------------------------------------------------------------------

(defmethod ana/error-message :dynamic-property-access [_type info]
  (str "Unexpected dynamic property access while calling " (:form info)))
