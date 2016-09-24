(ns oops.messages
  "A subsystem for printing compile-time warnings and errors. Piggybacks on cljs.analyzer."
  (:require [cljs.analyzer :as ana]
            [oops.debug :refer [debug-assert]]))

(def ^:dynamic *oops-message-prefix* "Oops")

; -- helpers ----------------------------------------------------------------------------------------------------------------

(def message-ids
  [:dynamic-selector-usage
   :static-nil-target-object
   :static-empty-selector-access])

(defn messages-registered? [table]
  (debug-assert (map? table))
  (let [result (contains? table (first message-ids))]
    (debug-assert (or (not result) (every? #(contains? table %) (rest message-ids))))
    result))

(defn register-messages [table]
  (debug-assert (map? table))
  (merge table (zipmap message-ids (repeat (count message-ids) true))))

(defn ^:dynamic post-process-message [msg]
  (str *oops-message-prefix* ", " msg))

(defmacro gen-oops-message-prefix []
  *oops-message-prefix*)

(defn static-macro? [command]
  (contains? #{'oget 'oset! 'ocall 'oapply 'ocall! 'oapply!} command))

; -- compile-time error/warning messages (in hooked cljs compiler) ----------------------------------------------------------

(defmethod ana/error-message :dynamic-selector-usage [type info]
  (debug-assert (some #{type} message-ids))
  (let [command (first (:form info))]
    (post-process-message (str "Unexpected dynamic selector usage"
                               (if (static-macro? command)
                                 (str " (consider using " command "+)"))))))

(defmethod ana/error-message :static-nil-target-object [type _info]
  (debug-assert (some #{type} message-ids))
  (post-process-message (str "Unexpected nil target object")))

(defmethod ana/error-message :static-empty-selector-access [type _info]
  (debug-assert (some #{type} message-ids))
  (post-process-message (str "Accessing target object with empty selector")))

; WARNING: when adding a new method here, don't forget to update register-messages as well
