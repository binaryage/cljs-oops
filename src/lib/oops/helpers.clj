(ns oops.helpers
  "Various helpers for our Clojure code."
  (:refer-clojure :exclude [gensym])
  (:require [oops.cuerdas :as cuerdas]
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

; taken from cljs.core
; https://github.com/binaryage/cljs-oops/issues/14
(defmacro unchecked-aget
  ([array idx]
   (list 'js* "(~{}[~{}])" array idx))
  ([array idx & idxs]
   (let [astr (apply str (repeat (count idxs) "[~{}]"))]
     `(~'js* ~(str "(~{}[~{}]" astr ")") ~array ~idx ~@idxs))))

; taken from cljs.core
; https://github.com/binaryage/cljs-oops/issues/14
(defmacro unchecked-aset
  ([array idx val]
   (list 'js* "(~{}[~{}] = ~{})" array idx val))
  ([array idx idx2 & idxv]
   (let [n (dec (count idxv))
         astr (apply str (repeat n "[~{}]"))]
     `(~'js* ~(str "(~{}[~{}][~{}]" astr " = ~{})") ~array ~idx ~idx2 ~@idxv))))
