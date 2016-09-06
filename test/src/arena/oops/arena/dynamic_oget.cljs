(ns oops.arena.dynamic-oget
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-test!]]))

(init-test!)

; we are compiling under advanced mode

(defn return-this-key [name]
  name)

(defn return-this-key-with-side-effect [name]
  (set! (.-x js/window) "dirty")
  name)

; simple get
(oget #js {"key" "val"} (return-this-key "key"))
(oget #js {"key" "val"} (identity "key"))
(oget #js {"key" "val"} (return-this-key-with-side-effect "key"))

; simple miss
(oget #js {"key" "val"} (return-this-key "xxx"))

; nested get
(def o1 #js {"key"    "val"
             "nested" #js {"nested-key" "nested-val"}})
(oget o1 (return-this-key "key") (return-this-key "nested"))

(def o2 #js {"key"    "val"
             "nested" #js {"nested-key" "nested-val"}})
(oget o2 [(return-this-key "key") (return-this-key "nested")])

(def o3 #js {"key"    "val"
             "nested" #js {"nested-key" "nested-val"}})
(oget o3 (return-this-key "key") [(return-this-key "nested")])


(def o4 #js {"key"    "val"
             "nested" #js {"nested-key" "nested-val"}})
(oget o4 #js ["key" "nested"])
