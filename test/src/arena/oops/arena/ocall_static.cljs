(ns oops.arena.ocall-static
  (:require [oops.core :refer [ocall]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "simple static ocall"
  (ocall #js {"f" (fn [] 42)} "f" "p1" "p2"))

(testing "retargeted static ocall"
  (ocall #js {"a" #js {"f" (fn [] 42)}} "a.f" "p1" "p2"))

(testing "threading macro with static ocall, see issue #12"
  (let [o #js {"e" #js {"f" (fn [x] #js {"g" (fn [y z] (+ x y z))})}}]
    (-> o
        (ocall "e.f" 1)
        (ocall "g" 2 3))))

(done-arena-test!)
