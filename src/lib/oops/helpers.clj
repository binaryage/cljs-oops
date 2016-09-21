(ns oops.helpers
  (:refer-clojure :exclude [gensym])
  (:require [cuerdas.core :as cuerdas]
            [clojure.pprint :refer [pprint]]))

(defn indent-text [s count]
  (let [prefix (cuerdas/repeat " " count)]
    (->> s
         (cuerdas/lines)
         (map #(str prefix %))
         (cuerdas/unlines))))

(defn pprint-code-str [code]
  (with-out-str
    (binding [clojure.pprint/*print-right-margin* 200
              *print-length* 100
              *print-level* 5]
      (pprint code))))

(defmacro gensym [name]
  `(clojure.core/gensym (str ~name "-")))

