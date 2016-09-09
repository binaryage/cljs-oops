(ns oops.arena.error-static-nil-object
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! snippet]]))

(init-arena-test!)

(snippet
  (oget nil "k1"))
