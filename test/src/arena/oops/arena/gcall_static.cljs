(ns oops.arena.gcall-static
  (:require [oops.core :refer [gcall]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "simple static gcall"
  (gcall "f" "p1" "p2"))

(testing "retargeted static gcall"
  (gcall "a.f" "p1" "p2"))

(done-arena-test!)
