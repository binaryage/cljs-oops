(ns oops.state
  (:require-macros [oops.state :refer [state-console-reporter-idx
                                       state-error-reported?-idx
                                       state-current-key-path-idx
                                       state-current-target-object-idx
                                       state-last-access-modifier-idx]]
                   [oops.debug :refer [debug-assert]]
                   [oops.constants :refer [get-dot-access]]))

(def ^:dynamic *runtime-state*)

; state is a javascript array with following slots:
(debug-assert (= (state-console-reporter-idx) 0))
(debug-assert (= (state-error-reported?-idx) 1))
(debug-assert (= (state-current-key-path-idx) 2))
(debug-assert (= (state-current-target-object-idx) 3))
(debug-assert (= (state-last-access-modifier-idx) 4))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn prepare-state [console-reporter target-object]
  (array console-reporter false (array) target-object (get-dot-access)))

(defn get-console-reporter []
  (debug-assert *runtime-state*)
  (let [console-reporter (aget *runtime-state* (state-console-reporter-idx))]
    (debug-assert (fn? console-reporter))
    console-reporter))

(defn get-current-target-object []
  (debug-assert *runtime-state*)
  (let [current-target-object (aget *runtime-state* (state-current-target-object-idx))]
    current-target-object))

(defn add-key-to-current-path! [key]
  (debug-assert (string? key))
  (debug-assert *runtime-state*)
  (let [current-key-path (aget *runtime-state* (state-current-key-path-idx))]
    (debug-assert (array? current-key-path))
    (.push current-key-path key)
    current-key-path))

(defn get-current-key-path []
  (debug-assert *runtime-state*)
  (let [current-key-path (aget *runtime-state* (state-current-key-path-idx))]
    (debug-assert (array? current-key-path))
    current-key-path))

(defn get-current-key-path-str []
  (debug-assert *runtime-state*)
  (.join (get-current-key-path) "."))

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
