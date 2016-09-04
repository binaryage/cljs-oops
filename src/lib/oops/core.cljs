(ns oops.core
  (:require-macros [oops.core :refer [coerce-key-dynamically-impl
                                      validate-object-dynamically-impl
                                      build-path-dynamically-impl
                                      get-key-dynamically-impl
                                      set-key-dynamically-impl
                                      get-selector-dynamically-impl
                                      set-selector-dynamically-impl]])
  (:require [clojure.spec]
            [oops.sdefs]))

(declare coerce-key-dynamically)
(declare validate-object-dynamically)
(declare build-path-dynamically)
(declare get-key-dynamically)
(declare set-key-dynamically)
(declare get-selector-dynamically)
(declare set-selector-dynamically)

(defn coerce-key-dynamically [key]
  (coerce-key-dynamically-impl key))

(defn validate-object-dynamically [obj]
  (validate-object-dynamically-impl obj))

(defn build-path-dynamically [selector]
  (build-path-dynamically-impl selector))

(defn get-key-dynamically [obj key]
  (get-key-dynamically-impl obj key))

(defn set-key-dynamically [obj key val]
  (set-key-dynamically-impl obj key val))

(defn get-selector-dynamically [obj & selector]
  (get-selector-dynamically-impl obj selector))

(defn set-selector-dynamically [obj selector val]
  (set-selector-dynamically-impl obj selector val))
