(ns oops.core
  (:require-macros [oops.core :refer [dynamic-selector-fetch-impl
                                      dynamic-key-fetch-impl
                                      dynamic-selector-reducer-impl
                                      dynamic-key-coerce-impl]])
  (:require [clojure.spec]
            [oops.sdefs]))

(declare dynamic-selector-fetch)
(declare dynamic-key-fetch)
(declare dynamic-selector-reducer)
(declare dynamic-key-coerce)

(defn dynamic-key-fetch [o key]
  (dynamic-key-fetch-impl o key))

(defn dynamic-selector-fetch [o & selector]
  (dynamic-selector-fetch-impl o selector))

(defn dynamic-selector-reducer [o selector-segment]
  (dynamic-selector-reducer-impl o selector-segment))

(defn dynamic-key-coerce [key]
  (dynamic-key-coerce-impl key))
