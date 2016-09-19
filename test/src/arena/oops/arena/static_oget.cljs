(ns oops.arena.static-oget
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode
; all this code is expected to collapse to constant or get elided

(testing "simple get"
  (oget #js {"key" "val"} "key"))

(testing "simple miss"
  (oget #js {"key" "val"} "xxx"))

(testing "simple get from refd-object"
  (def o1 #js {"key"    "val"
               "nested" #js {"nested-key" "nested-val"}})
  (oget o1 "key"))

(testing "nested get"
  (def o2 #js {"key"    "val"
               "nested" #js {"nested-key" "nested-val"}})
  (oget o2 "nested" "nested-key"))

(testing "nested keyword selector"
  (def o3 #js {"key"    "val"
               "nested" #js {"nested-key" "nested-val"}})
  (oget o3 [:nested [:nested-key]]))

(testing "some edge cases"
  (oget nil)
  (def o4 nil)
  (oget o4)
  (oget o4 :a :b :c))
