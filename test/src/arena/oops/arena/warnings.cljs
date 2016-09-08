(ns oops.arena.warnings
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-test!]]))

(println "XXX")

(init-test!)

; we are compiling under :optimizations :none mode
; we want to test compiler warnigns

(def o #js {"key" "val"})

(oget o (identity "key"))

(oget nil "k1" "k2" "k3")
