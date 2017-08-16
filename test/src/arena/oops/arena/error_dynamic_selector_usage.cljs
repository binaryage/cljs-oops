(ns oops.arena.error-dynamic-selector-usage
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

(testing "dynamic selector usage"
  (oget #js {} (identity "key")))

(done-arena-test!)
