(ns oops.arena.error-static-nil-object
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

(testing "static nil object"
  (oget nil "k1"))

(done-arena-test!)
