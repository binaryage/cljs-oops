(ns oops.core
  (:require-macros [oops.core :refer [dynamic-selector-fetch-impl dynamic-key-fetch-impl]]))

(declare dynamic-selector-fetch)
(declare dynamic-key-fetch)

(defn dynamic-key-fetch [o key]
  {:pre [(or (string? key) (keyword? key) (sequential? key))]}                                                                ; TODO: this should be validated by specs on cljs side
  (dynamic-key-fetch-impl o key))

(defn dynamic-selector-fetch [o & selector]
  {:pre [(sequential? selector)]}                                                                                             ; TODO: this should be validated by specs on cljs side
  (dynamic-selector-fetch-impl o selector))
