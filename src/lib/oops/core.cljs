(ns oops.core
  (:require-macros [oops.core :refer [build-path-dynamically-impl
                                      fetch-selector-dynamically-impl
                                      store-selector-dynamically-impl
                                      fetch-key-dynamically-impl
                                      coerce-key-dynamically-impl]])
  (:require [clojure.spec]
            [oops.sdefs]))

(declare build-path-dynamically)
(declare fetch-key-dynamically)
(declare fetch-selector-dynamically)
(declare store-selector-dynamically)
(declare coerce-key-dynamically)

(defn build-path-dynamically [selector]
  (build-path-dynamically-impl selector))

(defn fetch-key-dynamically [o key]
  (fetch-key-dynamically-impl o key))

(defn fetch-selector-dynamically [o & selector]
  (fetch-selector-dynamically-impl o selector))

(defn store-selector-dynamically [o selector val]
  (store-selector-dynamically-impl o selector val))

(defn coerce-key-dynamically [key]
  (coerce-key-dynamically-impl key))
