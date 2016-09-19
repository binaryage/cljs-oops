(ns oops.arena.exercise-oset
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oset! oset!+]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "dev oset! expansion"
  (oset! js/window "!k1" "!k2" "val"))

(testing "dev oset!+ expansion"
  (oset!+ js/window (identity "!k1.!k2") "val"))

(testing "dev oset!+ expansion with macro-generated params"
  (oset!+ js/window (macro-identity "!k1.!k2") "val"))
