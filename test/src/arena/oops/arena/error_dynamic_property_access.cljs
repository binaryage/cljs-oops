(ns oops.arena.error-dynamic-property-access
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

(testing
  (oget #js {} (identity "key")))
