(ns oops.arena.gcall-dev
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [gcall gcall+]]
            [oops.config :refer [without-diagnostics with-debug]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "static gcall expansion"
  (gcall "method" "p1" "p2"))

(testing "dynamic gcall expansion"
  (gcall+ (identity "method") "p1" "p2"))

(testing "gcall expansion with macro-generated method and params should be static"
  (gcall (macro-identity "method") (macro-identity "p1") "p2"))

(testing "gcall expansion with disabled diagnostics"
  (without-diagnostics
    (gcall "method" "p1" "p2")
    (gcall+ (identity "method") "p1" "p2")))

(testing "gcall expansion with enabled debugging"
  (with-debug
    (gcall "method" "p1" "p2")
    (gcall+ (identity "method") "p1" "p2")))

(testing "static gcall expansion with retargeting"
  (gcall "m1.m2" "p1" "p2"))

(testing "dynamic gcall expansion with retargeting"
  (gcall+ (identity "m1.m2") "p1" "p2"))

(done-arena-test!)
