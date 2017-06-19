(ns oops.arena.ocall-dynamic
  (:require [oops.core :refer [ocall+]]
            [oops.tools :refer [init-arena-test! testing]]))

(init-arena-test!)

; we are compiling under advanced mode

(testing "simple dynamic ocall"
  (ocall+ #js {"f" (fn [] 42)} (identity "f") "p1" "p2"))

(testing "retageted dynamic ocall"
  (ocall+ #js {"a" #js {"f" (fn [] 42)}} (identity "a.f") "p1" "p2"))

(testing "threading macro with dynamic ocall, see issue #12"
  (let [o #js {"e" #js {"f" (fn [x] #js {"g" (fn [y z] (+ x y z))})}}]
    (-> o
        (ocall+ (identity "e.f") 1)
        (ocall+ (identity "g") 2 3))))
