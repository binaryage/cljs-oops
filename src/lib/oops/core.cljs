(ns oops.core
  (:require-macros [oops.core :refer [dynamic-selector-fetch-impl
                                      dynamic-key-fetch-impl
                                      dynamic-selector-reducer-impl]])
  (:require [clojure.spec]
            [oops.sdefs]))

(declare dynamic-selector-fetch)
(declare dynamic-key-fetch)
(declare dynamic-selector-reducer)

(defn dynamic-key-fetch [o key]
  {:pre [(or (string? key) (keyword? key) (sequential? key))]}                                                                ; TODO: this should be validated by specs on cljs side
  (dynamic-key-fetch-impl o key))

(defn dynamic-selector-fetch [o & selector]
  (dynamic-selector-fetch-impl o selector))

(defn dynamic-selector-reducer [o selector-segment]
  (dynamic-selector-reducer-impl o selector-segment))
