(ns oops.compiler
  "Provides utils for interaction with cljs compiler. Beware! HACKS ahead!"
  (:refer-clojure :exclude [macroexpand])
  (:require [cljs.analyzer :as ana]
            [cljs.closure]
            [cljs.env]
            [oops.messages :refer [messages-registered? register-messages]]
            [oops.state :as state]
            [oops.debug :refer [log debug-assert]]))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn annotate-with-state [info]
  (assoc info :form oops.state/*invocation-form*))

(defn make-slug [type env]
  (list type (:file env) (:line env) (:column env)))

; -- cljs macro expanding ---------------------------------------------------------------------------------------------------

(defn macroexpand* [env form]
  (if-not (and (seq? form) (seq form))
    form
    (let [expanded-form (ana/macroexpand-1 env form)]
      (if (identical? form expanded-form)
        expanded-form
        (macroexpand* env expanded-form)))))

(defn macroexpand [form]
  (debug-assert oops.state/*invocation-env*)
  (macroexpand* oops.state/*invocation-env* form))

; -- compiler context -------------------------------------------------------------------------------------------------------

(defmacro with-hooked-compiler! [& body]
  `(do
     (if-not (messages-registered? ana/*cljs-warnings*)
       (set! ana/*cljs-warnings* (register-messages ana/*cljs-warnings*)))                                                    ; add our messages on first invocation
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

; -- subsystem for preventing duplicit warnings -----------------------------------------------------------------------------

(defonce original-build-fn (volatile! nil))

(defn build-wrapper [& args]
  (debug-assert @original-build-fn)
  (if cljs.env/*compiler*
    (swap! cljs.env/*compiler* dissoc ::issued-warnings))
  (apply @original-build-fn args))

(defn install-build-wrapper-if-needed! []
  ; this feels way too hacky, know a better way how to achieve this?
  (if (nil? @original-build-fn)
    (vreset! original-build-fn cljs.closure/build)
    (alter-var-root #'cljs.closure/build (constantly build-wrapper))))

(defn ensure-no-warnings-duplicity! [type env]
  (assert cljs.env/*compiler*)
  (install-build-wrapper-if-needed!)
  (let [slug (make-slug type env)
        issued-warnings (get @cljs.env/*compiler* ::issued-warnings)]
    (when-not (contains? issued-warnings slug)
      (swap! cljs.env/*compiler* update ::issued-warnings #(conj (or % #{}) slug))
      true)))

; -- compile-time warnings and errors ---------------------------------------------------------------------------------------

(defn warn! [type & [info]]
  (assert state/*invocation-env* "oops.state/*invocation-env* must be set via with-diagnostics-context! first!")
  (if (ensure-no-warnings-duplicity! type state/*invocation-env*)                                                             ; prevent issuing duplicated warnings (due to double macro analysis under some circumstances)
    (ana/warning type state/*invocation-env* (annotate-with-state info))))

(defn error! [type & [info]]
  (assert state/*invocation-env* "oops.state/*invocation-env* must be set via with-diagnostics-context! first!")
  (let [msg (ana/error-message type (annotate-with-state info))]
    (throw (ana/error state/*invocation-env* msg))))
