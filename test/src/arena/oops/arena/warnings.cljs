(ns oops.arena.warnings
  (:require [oops.core :refer [oget oget+]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we are compiling under :optimizations :none mode
; we want to test compiler warnings

(testing "no warnings"
  (let [o #js {"key" "val"}]
    (oget o "key")))

(testing "dynamic property access"
  (oget #js {} (identity "key")))

(testing "static nil target object"
  (oget nil "k1" "k2" "k3"))

(testing "static empty selector access in oget"
  (oget (js-obj))
  (oget (js-obj []))
  (oget (js-obj [[] []])))

(done-arena-test!)
