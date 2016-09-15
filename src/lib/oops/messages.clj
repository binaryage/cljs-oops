(ns oops.messages
  (:require [cljs.analyzer :as ana]))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn register-messages! [table]
  (assoc table
    :dynamic-selector-usage true
    :static-nil-target-object true
    :static-empty-selector-access true))

(defn post-process-error-message [msg]
  (str "Oops, " msg))

; -- compile-time error/warning messages (in hooked cljs compiler) ----------------------------------------------------------

(defmethod ana/error-message :dynamic-selector-usage [_type _info]
  (post-process-error-message (str "Unexpected dynamic selector usage")))

(defmethod ana/error-message :static-nil-target-object [_type _info]
  (post-process-error-message (str "Unexpected nil target object")))

(defmethod ana/error-message :static-empty-selector-access [_type _info]
  (post-process-error-message (str "Accessing target object with empty selector")))

; WARNING: when adding a new method here, don't forget to update register-messages! as well
