(ns oops.messages
  (:require [cljs.analyzer :as ana]))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn register-messages! [table]
  (assoc table
    :dynamic-property-access true))

(defn post-process-error-message [msg]
  (str "Oops, " msg))

; -- runtime error/warning messages -----------------------------------------------------------------------------------------

(defmulti runtime-message (fn [type & _] type))

(defmethod runtime-message :unexpected-object-value [_type flavor]
  (post-process-error-message (str "Unexpected object value (" flavor ")")))

(defmethod runtime-message :invalid-path [_type]
  (post-process-error-message "Invalid path"))

(defmethod runtime-message :invalid-selector [_type]
  (post-process-error-message "Invalid selector"))

; -- compile-time error/warning messages (in hooked cljs compiler) ----------------------------------------------------------

(defmethod ana/error-message :dynamic-property-access [_type _info]
  (post-process-error-message (str "Unexpected dynamic property access")))

(defmethod ana/error-message :static-nil-object [_type _info]
  (post-process-error-message (str "Unexpected nil object")))
