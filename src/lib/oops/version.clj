(ns oops.version)

(def current-version "0.6.4")                                                                                                 ; this should match our project.clj

(defmacro get-current-version []
  current-version)
