(ns oops.arena.oapply-dev
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oapply oapply+]]
            [oops.config :refer [without-diagnostics with-debug]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "static oapply expansion"
  (oapply js/window "method" ["p1" "p2"]))

(testing "dynamic oapply expansion"
  (oapply+ js/window (identity "method") ["p1" "p2"]))

(testing "oapply expansion with macro-generated method and params should be static"
  (oapply+ js/window (macro-identity "method") (macro-identity [(macro-identity "p1") "p2"])))

(testing "oapply expansion with disabled diagnostics"
  (without-diagnostics
    (oapply js/window "method" ["p1" "p2"])
    (oapply+ js/window (identity "method") ["p1" "p2"])))

(testing "oapply expansion with enabled debugging"
  (with-debug
    (oapply js/window "method" ["p1" "p2"])
    (oapply+ js/window (identity "method") ["p1" "p2"])))

(testing "static oapply expansion with retargeting"
  (oapply js/window "m1.m2" ["p1" "p2"]))

(testing "dynamic oapply expansion with retargeting"
  (oapply+ js/window (identity "m1.m2") ["p1" "p2"]))

(done-arena-test!)
