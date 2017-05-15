(ns oops.arena.oget-dev
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oget oget+]]
            [oops.config :refer [without-diagnostics with-debug]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we want to test generated code shape expansion under dev mode

(testing "static oget expansion"
  (oget js/window "k1" ["?k2" "k3"]))

(testing "dynamic oget expansion"
  (oget+ js/window (identity "k1.?k2.k3")))

(testing "oget expansion with macro-generated params should be static"
  (oget js/window (macro-identity "k1.?k2.k3") (macro-identity :k4)))

(testing "oget expansion with disabled diagnostics"
  (without-diagnostics
    (oget js/window "k1.?k2.k3")
    (oget+ js/window (identity "k1.?k2.k3"))))

(testing "oget expansion with enabled debugging"
  (with-debug
    (oget js/window "k1.?k2.k3")
    (oget+ js/window (identity "k1.?k2.k3"))))

(testing "nested static oget expansion"
  (oget (oget js/window "k1") ["?k2" "k3"]))

(testing "nested dynamic oget expansion"
  (oget+ (oget js/window "k1") (oget js/window "k2.k3")))
