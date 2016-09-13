(ns oops.arena.warnings
  (:require [oops.core :refer [oget oget+]]
            [oops.tools :refer [init-arena-test! snippet]]))

(init-arena-test!)

; we are compiling under :optimizations :none mode
; we want to test compiler warnigns

(snippet "no warnings"
  (let [o #js {"key" "val"}]
    (oget o "key")))

(snippet "dynamic property access"
  (oget #js {} (identity "key")))

(snippet "static nil target object"
  (oget nil "k1" "k2" "k3"))

(snippet "static empty selector access in oget"
  (oget (js-obj))
  (oget (js-obj []))
  (oget (js-obj [[] []])))
