(ns oops.core
  (:require-macros [oops.core]
                   [oops.runtime :as runtime])
  (:require [clojure.spec]
            [goog.object]
            [oops.sdefs]
            [oops.state]
            [oops.config]
            [oops.messages]
            [oops.helpers]
            [oops.schema]))

; -- diagnostics reporting --------------------------------------------------------------------------------------------------

(defn ^:dynamic report-error-dynamically [msg data]
  (runtime/report-error-dynamically msg data))

(defn ^:dynamic report-warning-dynamically [msg data]
  (runtime/report-warning-dynamically msg data))

(defn ^:dynnamic report-if-needed-dynamically [msg-id & [info]]
  (runtime/report-if-needed-dynamically msg-id info))

; -- runtime support for macros ---------------------------------------------------------------------------------------------

(defn ^:dynamic punch-key-dynamically! [obj key]
  (runtime/punch-key-dynamically obj key))

(defn ^boolean validate-object-access-dynamically [obj mode key check-key?]
  (runtime/validate-object-access-dynamically obj mode key check-key?))

(defn ^boolean validate-fn-call-dynamically [fn mode]
  (runtime/validate-fn-call-dynamically fn mode))

(defn build-path-dynamically [selector]
  (runtime/build-path-dynamically selector))

(defn get-key-dynamically [obj key mode]
  (runtime/get-key-dynamically obj key mode))

(defn set-key-dynamically [obj key val mode]
  (runtime/set-key-dynamically obj key val mode))

(defn get-selector-dynamically [obj selector]
  (runtime/get-selector-dynamically obj selector))

(defn set-selector-dynamically [obj selector val]
  (runtime/set-selector-dynamically obj selector val))
