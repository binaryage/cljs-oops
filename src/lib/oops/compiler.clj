(ns oops.compiler
  "Provides some helper utils for interaction with cljs compiler."
  (:refer-clojure :exclude [gensym])
  (:require [cljs.analyzer :as ana]
            [cljs.env]
            [oops.messages :refer [register-messages!]]
            [oops.state :as state]
            [oops.debug :refer [log debug-assert]]))

(defmacro gensym [name]
  `(clojure.core/gensym (str ~name "-")))

(defmacro with-hooked-compiler! [& body]
  `(binding [ana/*cljs-warnings* (register-messages! ana/*cljs-warnings*)]
     ~@body))

(defmacro with-compiler-opts! [opts & body]
  `(binding [oops.state/*invocation-opts* (merge oops.state/*invocation-opts* ~opts)]
     ~@body))

(defmacro with-compiler-diagnostics-context! [form env & body]
  `(binding [oops.state/*invocation-form* ~form
             oops.state/*invocation-env* ~env]
     ~@body))

(defmacro with-compiler-context! [form env & body]
  `(oops.compiler/with-hooked-compiler!
     (oops.compiler/with-compiler-diagnostics-context! ~form ~env ~@body)))

(defn annotate-with-state [info]
  (assoc info :form oops.state/*invocation-form*))

(defn warn! [type & [info]]
  (assert state/*invocation-env* "oops.state/*invocation-env* must be set via with-diagnostics-context! first!")
  (ana/warning type state/*invocation-env* (annotate-with-state info)))

(defn error! [type & [info]]
  (assert state/*invocation-env* "oops.state/*invocation-env* must be set via with-diagnostics-context! first!")
  (let [msg (ana/error-message type (annotate-with-state info))]
    (throw (ana/error state/*invocation-env* msg))))
