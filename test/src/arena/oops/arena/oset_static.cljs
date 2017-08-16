(ns oops.arena.oset-static
  (:require-macros [oops.arena.macros :refer [macro-identity]])
  (:require [oops.core :refer [oset! oset!+]]
            [oops.config :refer [without-diagnostics with-debug]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "static oset! expansion"
  (oset! js/window "!k1" "!k2" "val"))

(testing "oset! expansion with macro-generated params should be static"
  (oset! js/window (macro-identity "!k1.!k2") "val"))

(testing "static oset! expansion without punching"
  (oset! js/window "k1.k2" "val"))

(done-arena-test!)
