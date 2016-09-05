(ns oops.arena.basic-oget
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-test!]]))

(init-test!)

; we are compiling under advanced mode
; all this code is expected to collapse to constant or get elided

; simple get
(oget #js {"key" "val"} "key")

; simple miss
(oget #js {"key" "val"} "xxx")


; simple get from refd-object
(def o1 #js {"key"    "val"
             "nested" #js {"nested-key" "nested-val"}})
(oget o1 "key")

; nested get
(def o2 #js {"key"    "val"
             "nested" #js {"nested-key" "nested-val"}})
(oget o2 "nested" "nested-key")

; nested keyword selector
(def o3 #js {"key"    "val"
             "nested" #js {"nested-key" "nested-val"}})
(oget o3 [:nested [:nested-key]])

; some edge cases
(oget nil)
(def o4 nil)
(oget o4)
(oget o4 :a :b :c)
