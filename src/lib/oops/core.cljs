(ns oops.core
  (:require-macros [oops.core :refer [report-runtime-error-impl
                                      report-runtime-warning-impl
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
            [oops.constants :refer-macros [get-dot-access get-soft-access get-punch-access]]))

; -- diagnostics reporting --------------------------------------------------------------------------------------------------

(defn ^:dynamic report-runtime-error [msg data]
  (report-runtime-error-impl msg data))

(defn ^:dynamic report-runtime-warning [msg data]
  (report-runtime-warning-impl msg data))

; -- runtime support for macros ---------------------------------------------------------------------------------------------

(defn parse-selector-element [element-str arr]
  (if-not (empty? element-str)
    (case (first element-str)
      "?" (do
            (.push arr (get-soft-access))
            (.push arr (.substring element-str 1)))
      "!" (do
            (.push arr (get-punch-access))
            (.push arr (.substring element-str 1)))
      (do
        (.push arr (get-dot-access))
        (.push arr element-str)))))

(defn parse-selector-string [selector-str arr]
  (let [elements-arr (.split selector-str #"\.")]                                                                             ; TODO: handle dot escaping somehow
    (loop [items (seq elements-arr)]
      (when items
        (parse-selector-element (first items) arr)
        (recur (next items))))))

(defn coerce-key-dynamically! [key arr]
  (let [selector-str (name key)]
    (parse-selector-string selector-str arr)))

(defn collect-coerced-keys-into-array! [coll arr]
  (loop [items (seq coll)]                                                                                                    ; note: items is either a seq or nil
    (if-not (nil? items)
      (let [item (-first items)]
        (if (sequential? item)
          (collect-coerced-keys-into-array! item arr)
          (coerce-key-dynamically! item arr))
        (recur (next items))))))

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
