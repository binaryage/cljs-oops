(ns oops.schema
  (:require [clojure.spec :as s]
            [clojure.walk :as walk]
            [oops.sdefs :as sdefs]
            [oops.constants :refer [dot-access soft-access punch-access]]
            [clojure.string :as string]
            [oops.debug :refer [debug-assert log]]))

; --- path utils ------------------------------------------------------------------------------------------------------------

(defn unescape-specials [s]
  (string/replace s #"^\\([?!])" "$1"))

(defn parse-selector-element [element-str]
  (case (first element-str)
    \? [soft-access (.substring element-str 1)]
    \! [punch-access (.substring element-str 1)]
    [dot-access (unescape-specials element-str)]))

(defn unescape-dots [s]
  (string/replace s #"\\\." "."))

(defn parse-selector-string [selector-str]
  (let [elements (->> (string/split selector-str #"(?<!\\)\.")                                                                ; http://stackoverflow.com/a/820223/84283
                      (remove empty?)
                      (map unescape-dots))]
    (map parse-selector-element elements)))

(defn coerce-key [destructured-key]
  (let [value (second destructured-key)]
    (case (first destructured-key)
      :string (parse-selector-string value)
      :keyword (parse-selector-string (name value)))))

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
  (let [path (if-not (= destructured-selector ::s/invalid)
               (->> destructured-selector
                    (coerce-selector-keys)
                    (coerce-nested-selectors)
                    (flatten)
                    (partition 2)
                    (map vec)))]
    (debug-assert (or (nil? path) (s/valid? ::sdefs/obj-path path)))
    path))

(defn selector->path [selector]
  (->> selector
       (s/conform ::sdefs/obj-selector)
       (build-selector-path)))
