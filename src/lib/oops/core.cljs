(ns oops.core
  (:require-macros [oops.core :refer [report-runtime-error-impl
                                      report-runtime-warning-impl
                                      report-if-needed-dynamically-impl
                                      punch-key-dynamically-impl
                                      validate-object-dynamically-impl
                                      build-path-dynamically-impl
                                      get-key-dynamically-impl
                                      set-key-dynamically-impl
                                      get-selector-dynamically-impl
                                      set-selector-dynamically-impl]])
  (:require [clojure.spec]
            [goog.object]
            [oops.sdefs]
            [oops.state]
            [oops.config]
            [oops.helpers]
            [oops.schema]))

; -- diagnostics reporting --------------------------------------------------------------------------------------------------

(defn ^:dynamic report-runtime-error [msg data]
  (report-runtime-error-impl msg data))

(defn ^:dynamic report-runtime-warning [msg data]
  (report-runtime-warning-impl msg data))

(defn ^:dynnamic report-if-needed-dynamically [msg-id msg & [info]]
  (report-if-needed-dynamically-impl msg-id msg info))

; -- runtime support for macros ---------------------------------------------------------------------------------------------

(defn ^:dynamic punch-key-dynamically! [obj key]
  (punch-key-dynamically-impl obj key))

(defn ^boolean validate-object-dynamically [obj mode]
  (validate-object-dynamically-impl obj mode))

(defn build-path-dynamically [selector]
  (build-path-dynamically-impl selector))

(defn get-key-dynamically [obj key mode]
  (get-key-dynamically-impl obj key mode))

(defn set-key-dynamically [obj key val mode]
  (set-key-dynamically-impl obj key val mode))

(defn get-selector-dynamically [obj selector]
  (get-selector-dynamically-impl obj selector))

(defn set-selector-dynamically [obj selector val]
  (set-selector-dynamically-impl obj selector val))
