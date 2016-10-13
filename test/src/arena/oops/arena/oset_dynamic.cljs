(ns oops.arena.oset-dynamic
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oset! oset!+]]
            [oops.config :refer [without-diagnostics with-debug]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "dynamic oset! expansion"
  (oset!+ js/window (identity "!k1.!k2") "val"))

(testing "dynamic oset! without punching"
  (oset!+ js/window (identity "k1.k2.k3") "val"))
