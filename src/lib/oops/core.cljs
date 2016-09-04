(ns oops.core
  (:require-macros [oops.core :refer [fetch-selector-dynamically-impl
                                      fetch-key-dynamically-impl
                                      dynamic-selector-reducer-impl
                                      coerce-key-dynamically-impl]])
  (:require [clojure.spec]
            [oops.sdefs]))

(declare fetch-key-dynamically)
(declare fetch-selector-dynamically)
(declare dynamic-selector-reducer)
(declare coerce-key-dynamically)

(defn fetch-key-dynamically [o key]
  (fetch-key-dynamically-impl o key))

(defn fetch-selector-dynamically [o & selector]
  (fetch-selector-dynamically-impl o selector))

(defn dynamic-selector-reducer [o key]
  (dynamic-selector-reducer-impl o key))

(defn coerce-key-dynamically [key]
  (coerce-key-dynamically-impl key))
