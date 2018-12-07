(ns oops.arena.issue-21
  "https://github.com/binaryage/cljs-oops/issues/21"
  (:require [oops.core :refer [oget oset! ocall! oapply!]]
            [oops.tools :refer [init-arena-test! done-arena-test! testing]]))

(init-arena-test!)

(set! *warn-on-infer* true)

(testing "exercise oget with *warn-on-infer* enabled"
  (oget js/document :foo))

(testing "exercise oset! with *warn-on-infer* enabled"
  (oset! js/document :test-issue-21 "foo"))

(testing "exercise ocall! with *warn-on-infer* enabled"
  (ocall! js/document :getElementById "foo"))

(testing "exercise oapply! with *warn-on-infer* enabled"
  (oapply! js/document :getElementById ["foo"]))

(done-arena-test!)
