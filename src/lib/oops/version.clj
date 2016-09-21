(ns oops.version)

(def current-version "0.3.0")                                                                                        ; this should match our project.clj

(defmacro get-current-version []
  current-version)
