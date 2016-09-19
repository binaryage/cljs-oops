(ns oops.arena.exercise-oapply
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oapply oapply+]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "dev oapply expansion"
  (oapply js/window "method" ["p1" "p2"]))

(testing "dev oapply+ expansion"
  (oapply+ js/window (identity "method") ["p1" "p2"]))

(testing "dev oapply+ expansion with macro-generated method"
  (oapply+ js/window (macro-identity "method") ["p1" "p2"]))
