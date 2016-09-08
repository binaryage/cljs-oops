(ns oops.arena.warnings
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-test!]]))

(init-test!)

; we are compiling under :optimizations :none mode
; we want to test compiler warnigns

(oget nil "k1" "k2" "k3")
