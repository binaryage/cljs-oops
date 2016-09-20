(ns oops.state
  (:require-macros [oops.state :refer [state-target-object-idx
                                       state-call-site-error-idx
                                       state-console-reporter-idx
                                       state-error-reported?-idx
                                       state-key-path-idx
                                       state-last-access-modifier-idx]]
                   [oops.debug :refer [debug-assert]]
                   [oops.constants :refer [get-dot-access]])
  (:require [oops.helpers :refer [repurpose-error]]))

(def ^:dynamic *runtime-state*)

; state is a javascript array with following slots:
(debug-assert (= (state-target-object-idx) 0))
(debug-assert (= (state-call-site-error-idx) 1))
(debug-assert (= (state-console-reporter-idx) 2))
(debug-assert (= (state-error-reported?-idx) 3))
(debug-assert (= (state-key-path-idx) 4))
(debug-assert (= (state-last-access-modifier-idx) 5))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn make-empty-key-path []
  (array))

(defn prepare-state [target-object call-site-error console-reporter]
  (array target-object
         call-site-error
         console-reporter
         false
         (make-empty-key-path)
         (get-dot-access)))

(defn get-target-object []
  (debug-assert *runtime-state*)
  (let [current-target-object (aget *runtime-state* (state-target-object-idx))]
    current-target-object))

(defn get-console-reporter []
  (debug-assert *runtime-state*)
  (let [console-reporter (aget *runtime-state* (state-console-reporter-idx))]
    (debug-assert (fn? console-reporter))
    console-reporter))

(defn get-call-site-error []
  (debug-assert *runtime-state*)
  (let [call-site-error (aget *runtime-state* (state-call-site-error-idx))]
    (debug-assert (instance? js/Error call-site-error))
    call-site-error))

(defn add-key-to-current-path! [key]
  (debug-assert (string? key))
  (debug-assert *runtime-state*)
  (let [current-key-path (aget *runtime-state* (state-key-path-idx))]
    (debug-assert (array? current-key-path))
    (.push current-key-path key)
    current-key-path))

(defn get-key-path []
  (debug-assert *runtime-state*)
  (let [current-key-path (aget *runtime-state* (state-key-path-idx))]
    (debug-assert (array? current-key-path))
    current-key-path))

(defn get-key-path-str []
  (debug-assert *runtime-state*)
  (.join (get-key-path) "."))

(defn get-last-access-modifier []
  (debug-assert *runtime-state*)
  (aget *runtime-state* (state-last-access-modifier-idx)))

(defn set-last-access-modifier! [mode]
  (debug-assert *runtime-state*)
  (aset *runtime-state* (state-last-access-modifier-idx) mode))

(defn ^boolean was-error-reported? []
  (debug-assert *runtime-state*)
  (let [error-reported? (aget *runtime-state* (state-error-reported?-idx))]
    (debug-assert (boolean? error-reported?))
    error-reported?))

(defn mark-error-reported! []
  (debug-assert *runtime-state*)
  (aset *runtime-state* (state-error-reported?-idx) true))

(defn prepare-error-from-call-site [msg data]
  (repurpose-error (get-call-site-error) msg data))
