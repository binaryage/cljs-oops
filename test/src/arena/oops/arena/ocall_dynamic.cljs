(ns oops.arena.ocall-dynamic
  (:require [oops.core :refer [ocall ocall+]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "simple dynamic ocall"
  (ocall+ #js {"f" (fn [] 42)} (identity "f")))
