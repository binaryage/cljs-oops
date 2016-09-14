(ns oops.state
  (:require-macros [oops.state]
                   [oops.debug :refer [debug-assert]]))

(def ^:dynamic *console-reporter*)
(def ^:dynamic *error-reported?*)
(def ^:dynamic *current-obj*)
(def ^:dynamic *current-key-path*)

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn get-current-obj []
  *current-obj*)

(defn add-key-to-current-path! [key]
  (debug-assert *current-key-path*)
  (debug-assert (string? key))
  (.push *current-key-path* key))

(defn get-current-key-path-str []
  (debug-assert *current-key-path*)
  (.join *current-key-path* "."))

(defn was-error-reported? []
  (debug-assert (boolean? *error-reported?*))
  *error-reported?*)

(defn mark-error-reported! []
  (debug-assert (boolean? *error-reported?*))
  (set! *error-reported?* true))
