(ns oops.arena.oapply-static
  (:require [oops.core :refer [oapply]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "simple static oapply"
  (oapply #js {"f" (fn [] 42)} "f" ["p1" "p2"]))

(testing "retargeted static oapply"
  (oapply #js {"a" #js {"f" (fn [] 42)}} "a.f" ["p1" "p2"]))

(done-arena-test!)
