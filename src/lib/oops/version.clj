(ns oops.version)

(def current-version "0.4.1-SNAPSHOT")                                                                                        ; this should match our project.clj

(defmacro get-current-version []
  current-version)
