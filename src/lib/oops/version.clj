(ns oops.version)

(def current-version "0.5.2")                                                                                        ; this should match our project.clj

(defmacro get-current-version []
  current-version)
