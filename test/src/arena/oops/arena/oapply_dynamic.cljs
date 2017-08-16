(ns oops.arena.oapply-dynamic
  (:require [oops.core :refer [oapply+]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "simple dynamic oapply"
  (oapply+ #js {"f" (fn [] 42)} (identity "f") ["p1" "p2"]))

(testing "retageted dynamic oapply"
  (oapply+ #js {"a" #js {"f" (fn [] 42)}} (identity "a.f") ["p1" "p2"]))

(done-arena-test!)
