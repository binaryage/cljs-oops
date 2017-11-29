(ns oops.schema
  "The code for runtime conversion of selectors to paths. Note: we prefer hand-written loops for performance reasons."
  (:require-macros [oops.schema]
                   [oops.constants :refer [get-dot-access get-soft-access get-punch-access
                                           gen-op-get gen-op-set]]
                   [oops.helpers :refer [unchecked-aget]]
                   [oops.debug :refer [debug-assert]]))

; implementation here should mimic static versions in schema.clj
; for performance reasons we don't reuse the same code on cljs side

; --- path utils ------------------------------------------------------------------------------------------------------------

(def escaped-dot-marker "####ESCAPED-DOT####")
(def re-all-escaped-dots (js/RegExp. "\\\\\\." "g"))
(def re-all-escaped-dot-markers (js/RegExp. "####ESCAPED-DOT####" "g"))

(defn unescape-modifiers [s]
  (.replace s #"^\\([?!])" "$1"))

(defn parse-selector-element! [element-str arr]
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
        (.push arr (unescape-modifiers element-str))))))

(defn unescape-dots [s]
  (.replace s re-all-escaped-dot-markers "."))

(defn parse-selector-string! [selector-str arr]
  (let [elements-arr (.split (.replace selector-str re-all-escaped-dots escaped-dot-marker) ".")]
    (loop [items (seq elements-arr)]
      (when items
        (parse-selector-element! (unescape-dots (first items)) arr)
        (recur (next items))))))

(defn coerce-key-dynamically! [key arr]
  (let [selector-str (name key)]
    (parse-selector-string! selector-str arr)))

(defn collect-coerced-keys-into-array! [coll arr]
  (loop [items (seq coll)]                                                                                                    ; note: items is either a seq or nil
    (if-not (nil? items)
      (let [item (-first items)]
        (if (sequential? item)
          (collect-coerced-keys-into-array! item arr)
          (coerce-key-dynamically! item arr))
        (recur (next items))))))

(defn standalone-modifier? [arr i]
  (and (pos? (unchecked-aget arr i))
       (= "" (unchecked-aget arr (inc i)))))

(defn merge-standalone-modifier! [arr i]
  (aset arr (+ i 2) (unchecked-aget arr i))                                                                                   ; transfer modifier
  (.splice arr i 2))                                                                                                          ; remove standalone item

(defn merge-standalone-modifiers! [arr]
  (let [len (alength arr)]
    (loop [i (- len 2)]                                                                                                       ; -2 because it makes no sense to potentially merge last item
      (let [finger (- i 2)]
        (if (neg? finger)
          arr
          (do
            (if (standalone-modifier? arr finger)
              (merge-standalone-modifier! arr finger))
            (recur finger)))))))

(defn prepare-path! [selector arr]
  (collect-coerced-keys-into-array! selector arr)
  (merge-standalone-modifiers! arr))

(defn prepare-simple-path! [key arr]
  (coerce-key-dynamically! key arr))

(defn has-invalid-path-access-mode? [path is-valid?]
  (loop [items (seq path)]
    (when items
      (if (is-valid? (first items))
        (recur (next (next items)))
        true))))

; we should mimic check-static-path! here
(defn check-dynamic-path! [path op]
  (debug-assert (= (gen-op-get) 0))
  (debug-assert (= (gen-op-set) 1))
  (if (empty? path)
    [:unexpected-empty-selector]
    (case op
      0 (if (has-invalid-path-access-mode? path #(not= % (get-punch-access)))
          [:unexpected-punching-selector])
      1 (if (has-invalid-path-access-mode? path #(not= % (get-soft-access)))
          [:unexpected-soft-selector]))))
