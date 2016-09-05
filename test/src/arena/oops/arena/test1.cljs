(ns oops.arena.test1
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-test!]]))

(init-test!)

(def o #js {"key"    "val"
            "nested" #js {"k" "v"}})

(def get1 (oget o "key"))
(def get2 (oget o "nested" "k"))
(def get3 (oget o ["nested" "k"]))

(.log js/console get1 get2 get3)
