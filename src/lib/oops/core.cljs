(ns oops.core
  "The main namespace to be consumed by library users from ClojureScript.
  Provides core macros via core.clj and runtime support (see runtime.clj).

    (ns your-project.namespace
        (:require [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                                     oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+
                                     gget gset! gcall gapply gcall! gapply!
                                     gget+ gset!+ gcall+ gapply+ gcall!+ gapply!+]]))

    (oset! (js-obj) :mood \"a happy camper\")

  Read about usage: https://github.com/binaryage/cljs-oops"
  (:require-macros [oops.core]
                   [oops.runtime :as runtime])
  (:require [clojure.spec.alpha]
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

(defn ^:dynamic report-if-needed-dynamically [msg-id & [info]]
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

(defn check-path-dynamically [path op]
  (runtime/check-path-dynamically path op))

(defn get-key-dynamically [obj key mode]
  (runtime/get-key-dynamically obj key mode))

(defn set-key-dynamically [obj key val mode]
  (runtime/set-key-dynamically obj key val mode))

(defn get-selector-dynamically [obj selector]
  (runtime/get-selector-dynamically obj selector))

(defn get-selector-call-info-dynamically [obj selector]
  (runtime/get-selector-call-info-dynamically obj selector))

(defn set-selector-dynamically [obj selector val]
  (runtime/set-selector-dynamically obj selector val))
