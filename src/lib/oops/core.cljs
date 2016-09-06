(ns oops.core
  (:require-macros [oops.core :refer [report-runtime-error-impl
                                      coerce-key-dynamically-impl
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
            [oops.config]))

(defn ^:dynamic print-error-to-console [& args]
  (.apply (.-error js/console) js/console (into-array args))
  nil)

(defn ^:dynamic report-runtime-error [msg data]
  (report-runtime-error-impl msg data))

(defn coerce-key-dynamically [key]
  (coerce-key-dynamically-impl key))

(defn collect-coerced-keys-into-array! [coll arr]
  (loop [items (seq coll)]                                                                                                    ; note: items is either a seq or nil
    (if-not (nil? items)
      (let [item (-first items)]
        (if (sequential? item)
          (collect-coerced-keys-into-array! item arr)
          (.push arr (coerce-key-dynamically item)))
        (recur (next items))))))

(defn validate-object-dynamically [obj]
  (validate-object-dynamically-impl obj))

(defn build-path-dynamically [selector]
  (build-path-dynamically-impl selector))

(defn get-key-dynamically [obj key]
  (get-key-dynamically-impl obj key))

(defn set-key-dynamically [obj key val]
  (set-key-dynamically-impl obj key val))

(defn get-selector-dynamically [obj selector]
  (get-selector-dynamically-impl obj selector))

(defn set-selector-dynamically [obj selector val]
  (set-selector-dynamically-impl obj selector val))
