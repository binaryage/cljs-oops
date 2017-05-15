(ns oops.arena.oget-static
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

; ----
; following code is expected to get elided

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

(testing "oget expansion with macro-generated params should be static"
  (oget js/window (macro-identity "k1.?k2.k3") (macro-identity :k4)))

; ----
; following code is expected get collapsed to simple console.log calls

(testing "simple get with usage"
  (.log js/console (oget #js {"key" "val"} "key")))

(testing "simple miss with usage"
  (.log js/console (oget #js {"key" "val"} "xxx")))
