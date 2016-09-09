(ns oops.arena.warnings
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! snippet]]))

(init-arena-test!)

; we are compiling under :optimizations :none mode
; we want to test compiler warnigns

(snippet
  (oget #js {} (identity "key")))

(snippet
  (oget nil "k1" "k2" "k3"))

(snippet
  (let [o #js {"key" "val"}]
    (oget o "key")))
