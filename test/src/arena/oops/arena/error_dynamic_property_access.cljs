(ns oops.arena.error-dynamic-property-access
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! snippet]]))

(init-arena-test!)

(snippet
  (oget #js {} (identity "key")))
