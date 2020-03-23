(ns oops.runner
  (:require [environ.core :refer [env]]))

(defmacro ansi-enabled? []
  (not (:oops-disable-test-runner-ansi env)))

(defmacro emit-clojure-version []
  (clojure-version))
