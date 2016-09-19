(ns oops.arena.exercise-ocall
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [ocall ocall+]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "dev ocall expansion"
  (ocall js/window "method" "p1" "p2"))

(testing "dev ocall+ expansion"
  (ocall+ js/window (identity "method") "p1" "p2"))

(testing "dev ocall+ expansion with macro-generated method"
  (ocall+ js/window (macro-identity "method") "p1" "p2"))
