(ns oops.arena.ocall-static
  (:require [oops.core :refer [oget]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

; ----
; following code is expected to get elided

(testing "simple static ocall"
  (ocall #js {"f" (fn [] 42)} "f"))
