(ns oops.core
  (:require-macros [oops.core :refer [build-path-dynamically-impl
                                      get-selector-dynamically-impl
                                      set-selector-dynamically-impl
                                      get-key-dynamically-impl
                                      coerce-key-dynamically-impl]])
  (:require [clojure.spec]
            [oops.sdefs]))

(declare build-path-dynamically)
(declare get-key-dynamically)
(declare get-selector-dynamically)
(declare set-selector-dynamically)
(declare coerce-key-dynamically)

(defn build-path-dynamically [selector]
  (build-path-dynamically-impl selector))

(defn get-key-dynamically [o key]
  (get-key-dynamically-impl o key))

(defn get-selector-dynamically [o & selector]
  (get-selector-dynamically-impl o selector))

(defn set-selector-dynamically [o selector val]
  (set-selector-dynamically-impl o selector val))

(defn coerce-key-dynamically [key]
  (coerce-key-dynamically-impl key))
