(ns oops.state
  "Here we gather runtime state. For performance/code-gen reasons we keep everything under one JS array."
  (:require-macros [oops.debug :refer [debug-assert]]
                   [oops.constants :as constants])
  (:require [oops.helpers :refer [repurpose-error unchecked-aget]]
            [oops.config :as config]))

(def ^:dynamic *runtime-state*)

; state is a javascript array with following slots:
(debug-assert (= (constants/target-object-idx) 0))
(debug-assert (= (constants/call-site-error-idx) 1))
(debug-assert (= (constants/console-reporter-idx) 2))
(debug-assert (= (constants/error-reported-idx) 3))
(debug-assert (= (constants/key-path-idx) 4))
(debug-assert (= (constants/last-access-modifier-idx) 5))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn make-empty-key-path []
  (array))

(defn prepare-state [target-object call-site-error console-reporter]
  (array target-object
         call-site-error
         console-reporter
         false
         (make-empty-key-path)
         (constants/get-dot-access)))

(defn get-target-object []
  (debug-assert *runtime-state*)
  (let [current-target-object (unchecked-aget *runtime-state* (constants/target-object-idx))]
    current-target-object))

(defn get-console-reporter []
  (debug-assert *runtime-state*)
  (let [console-reporter (unchecked-aget *runtime-state* (constants/console-reporter-idx))]
    (debug-assert (fn? console-reporter))
    console-reporter))

(defn get-call-site-error []
  (debug-assert *runtime-state*)
  (let [call-site-error (unchecked-aget *runtime-state* (constants/call-site-error-idx))]
    (debug-assert (instance? js/Error call-site-error))
    call-site-error))

(defn add-key-to-current-path! [key]
  (debug-assert (string? key))
  (debug-assert *runtime-state*)
  (let [current-key-path (unchecked-aget *runtime-state* (constants/key-path-idx))]
    (debug-assert (array? current-key-path))
    (.push current-key-path key)
    current-key-path))

(defn get-key-path []
  (debug-assert *runtime-state*)
  (let [current-key-path (unchecked-aget *runtime-state* (constants/key-path-idx))]
    (debug-assert (array? current-key-path))
    current-key-path))

(defn get-key-path-str []
  (debug-assert *runtime-state*)
  (.join (get-key-path) "."))

(defn get-last-access-modifier []
  (debug-assert *runtime-state*)
  (unchecked-aget *runtime-state* (constants/last-access-modifier-idx)))

(defn set-last-access-modifier! [mode]
  (debug-assert *runtime-state*)
  (aset *runtime-state* (constants/last-access-modifier-idx) mode))

(defn ^boolean was-error-reported? []
  (debug-assert *runtime-state*)
  (let [error-reported? (unchecked-aget *runtime-state* (constants/error-reported-idx))]
    (debug-assert (boolean? error-reported?))
    error-reported?))

(defn mark-error-reported! []
  (debug-assert *runtime-state*)
  (aset *runtime-state* (constants/error-reported-idx) true))

(defn prepare-error-from-call-site [msg data]
  (if (config/throw-errors-from-macro-call-sites?)
    (repurpose-error (get-call-site-error) msg data)
    (js/Error. msg)))                                                                                                         ; this is a fail-safe option for people with repurpose-error-related troubles, we don't attach data in this case
