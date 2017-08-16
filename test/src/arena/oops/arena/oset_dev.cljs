(ns oops.arena.oset-dev
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oset! oset!+]]
            [oops.config :refer [without-diagnostics with-debug]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "static oset! expansion"
  (oset! js/window "!k1" "!k2" "val"))

(testing "dynamic oset! expansion"
  (oset!+ js/window (identity "!k1.!k2") "val"))

(testing "oset! expansion with macro-generated params should be static"
  (oset! js/window (macro-identity "!k1.!k2") "val"))

(testing "oset! expansion with disabled diagnostics"
  (without-diagnostics
    (oset! js/window "!k1" "!k2" "val")
    (oset!+ js/window (identity "!k1.!k2") "val")))

(testing "oset! expansion with enabled debugging"
  (with-debug
    (oset! js/window "!k1" "!k2" "val")
    (oset!+ js/window (identity "!k1.!k2") "val")))

(done-arena-test!)
