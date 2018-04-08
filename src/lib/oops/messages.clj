(ns oops.messages
  "A subsystem for printing compile-time warnings and errors. Piggybacks on cljs.analyzer."
  (:require [cljs.analyzer :as ana]
            [oops.debug :refer [debug-assert]]))

(def ^:dynamic *oops-message-prefix* "Oops")

; -- helpers ----------------------------------------------------------------------------------------------------------------

(def message-ids
  [:dynamic-selector-usage
   :static-nil-target-object
   :static-unexpected-empty-selector
   :static-unexpected-punching-selector
   :static-unexpected-soft-selector])

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

(def static-macros
  '#{oget oset! ocall oapply ocall! oapply!
     gget gset! gcall gapply gcall! gapply!})

(defn static-macro? [command]
  (contains? static-macros command))

; -- compile-time error/warning messages (in hooked cljs compiler) ----------------------------------------------------------

(defmethod ana/error-message :dynamic-selector-usage [type info]
  (debug-assert (some #{type} message-ids))
  (let [command (first (:form info))]
    (post-process-message (str "Unexpected dynamic selector usage"
                               (when (static-macro? command)
                                 (str " (consider using " command "+)"))))))

(defmethod ana/error-message :static-nil-target-object [type _info]
  (debug-assert (some #{type} message-ids))
  (post-process-message (str "Unexpected nil target object")))

(defmethod ana/error-message :static-unexpected-empty-selector [type _info]
  (debug-assert (some #{type} message-ids))
  (post-process-message (str "Unexpected empty selector")))

(defmethod ana/error-message :static-unexpected-punching-selector [type _info]
  (debug-assert (some #{type} message-ids))
  (post-process-message (str "Unexpected punching selector (\"!\" makes sense only with oset!)")))

(defmethod ana/error-message :static-unexpected-soft-selector [type _info]
  (debug-assert (some #{type} message-ids))
  (post-process-message (str "Unexpected soft selector (\"?\" does not make sense with oset!)")))

; WARNING: when adding a new method here, don't forget to update register-messages as well
