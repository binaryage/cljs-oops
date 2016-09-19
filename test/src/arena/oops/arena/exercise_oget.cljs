(ns oops.arena.exercise-oget
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oget oget+]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "dev oget expansion"
  (oget js/window "k1" ["?k2" "k3"]))

(testing "dev oget+ expansion"
  (oget+ js/window (identity "k1.?k2.k3")))

(testing "dev ocall+ expansion with macro-generated params"
  (oget+ js/window (macro-identity "k1.?k2.k3")))
