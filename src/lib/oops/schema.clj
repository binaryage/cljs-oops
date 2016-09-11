(ns oops.schema
  (:require [clojure.spec :as s]
            [clojure.walk :as walk]
            [oops.sdefs :as sdefs]))

; --- path utils ------------------------------------------------------------------------------------------------------------

(defn coerce-key [destructured-key]
  (let [value (second destructured-key)]
    (case (first destructured-key)
      :string [:dot value]
      :keyword [:dot (name value)])))

(defn coerce-key-node [node]
  (if (and (sequential? node)
           (= (first node) :key))
    [(coerce-key (second node))]
    node))

(defn coerce-selector-keys [destured-selector]
  (walk/postwalk coerce-key-node destured-selector))

(defn coerce-selector-node [node]
  (if (and (sequential? node)
           (= (first node) :selector))
    (vector (second node))
    node))

(defn coerce-nested-selectors [destured-selector]
  (walk/postwalk coerce-selector-node destured-selector))

(defn build-selector-path [destructured-selector]
  {:post [(or (nil? %) (s/valid? ::sdefs/obj-path %))]}
  (if-not (= destructured-selector ::s/invalid)
    (->> destructured-selector
         (coerce-selector-keys)
         (coerce-nested-selectors)
         (flatten)
         (partition 2)
         (map vec))))

(defn selector->path [selector]
  (->> selector
       (s/conform ::sdefs/obj-selector)
       (build-selector-path)))
