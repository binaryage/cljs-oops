(ns oops.arena.gcall-dynamic
  (:require [oops.core :refer [gcall+]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "simple dynamic gcall"
  (gcall+ (identity "f") "p1" "p2"))

(testing "retageted dynamic gcall"
  (gcall+ (identity "a.f") "p1" "p2"))
