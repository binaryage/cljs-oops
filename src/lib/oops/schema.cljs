(ns oops.schema
  (:require-macros [oops.schema]
                   [oops.constants :refer [get-dot-access get-soft-access get-punch-access]]))

; implementation here should mimic static versions in schema.clj
; for perfomance reasons we don't reuse the same code on cljs side

; --- path utils ------------------------------------------------------------------------------------------------------------

(defn parse-selector-element [element-str arr]
  (if-not (empty? element-str)
    (case (first element-str)
      "?" (do
            (.push arr (get-soft-access))
            (.push arr (.substring element-str 1)))
      "!" (do
            (.push arr (get-punch-access))
            (.push arr (.substring element-str 1)))
      (do
        (.push arr (get-dot-access))
        (.push arr element-str)))))

(defn parse-selector-string [selector-str arr]
  (let [elements-arr (.split selector-str #"\.")]                                                                             ; TODO: handle dot escaping somehow
    (loop [items (seq elements-arr)]
      (when items
        (parse-selector-element (first items) arr)
        (recur (next items))))))

(defn coerce-key-dynamically! [key arr]
  (let [selector-str (name key)]
    (parse-selector-string selector-str arr)))

(defn collect-coerced-keys-into-array! [coll arr]
  (loop [items (seq coll)]                                                                                                    ; note: items is either a seq or nil
    (if-not (nil? items)
      (let [item (-first items)]
        (if (sequential? item)
          (collect-coerced-keys-into-array! item arr)
          (coerce-key-dynamically! item arr))
        (recur (next items))))))
