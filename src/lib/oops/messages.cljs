(ns oops.messages
  "A subsystem for printing runtime warnings and errors."
  (:require-macros [oops.messages]))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn ^:dynamic post-process-message [msg]
  (str (oops.messages/gen-oops-message-prefix) ", " msg))

; -- runtime error/warning messages -----------------------------------------------------------------------------------------

(defmulti runtime-message (fn [type & _] type))

(defmethod runtime-message :unexpected-object-value [_type info]
  (let [{:keys [flavor path]} info]
    (post-process-message (str "Unexpected object value (" flavor ")"
                               (if-not (empty? path) (str " on key path '" path "'"))))))

(defmethod runtime-message :expected-function-value [_type info]
  (let [{:keys [soft? path fn]} info]
    (post-process-message (str "Expected a function"
                               (if soft? (str " or nil"))
                               (if-not (empty? path) (str " on key path '" path "'"))
                               ", got <" (goog/typeOf fn) "> instead"))))

(defmethod runtime-message :missing-object-key [_type info]
  (let [{:keys [key path]} info]
    (post-process-message (str "Missing expected object key '" key "'"
                               (if-not (or (empty? path) (= path key))
                                 (str " on key path '" path "'"))))))

(defmethod runtime-message :invalid-selector [_type]
  (post-process-message "Invalid selector"))

(defmethod runtime-message :unexpected-empty-selector [_type]
  (post-process-message (str "Unexpected empty selector")))

(defmethod runtime-message :unexpected-punching-selector [_type]
  (post-process-message (str "Unexpected punching selector (\"!\" makes sense only with oset!)")))

(defmethod runtime-message :unexpected-soft-selector [_type]
  (post-process-message (str "Unexpected soft selector (\"?\" does not make sense with oset!)")))
