(ns oops.state
  (:require-macros [oops.state]))

; use oops.config/get-current-runtime-config to get currently effective config
(def ^:dynamic *runtime-config*)

(def ^:dynamic *console-reporter*)

(def ^:dynamic *punching-factory* js-obj)
