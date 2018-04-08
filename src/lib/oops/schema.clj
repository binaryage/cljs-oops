(ns oops.schema
  "The code for compile-time conversion of selectors to paths. Uses clojure.spec to do the heavy-lifting."
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :refer [postwalk]]
            [clojure.string :as string]
            [oops.config :as config]
            [oops.sdefs :as sdefs]
            [oops.constants :refer [dot-access soft-access punch-access]]
            [oops.reporting :refer [report-if-needed! report-offending-selector-if-needed!]]
            [oops.debug :refer [debug-assert log]]))

; --- path utils ------------------------------------------------------------------------------------------------------------

(defn unescape-modifiers [s]
  (string/replace s #"^\\([?!])" "$1"))

(defn parse-selector-element [element-str]
  (case (first element-str)
    \? [soft-access (.substring element-str 1)]
    \! [punch-access (.substring element-str 1)]
    [dot-access (unescape-modifiers element-str)]))

(defn unescape-dots [s]
  (string/replace s #"\\\." "."))

(defn parse-selector-string [selector-str]
  (let [elements (->> (string/split selector-str #"(?<!\\)\.")                                                                ; http://stackoverflow.com/a/820223/84283
                      (remove empty?)
                      (map unescape-dots))]
    (map parse-selector-element elements)))

(defn coerce-key [destructured-key]
  (let [value (second destructured-key)]
    (case (first destructured-key)
      :string (parse-selector-string value)
      :keyword (parse-selector-string (name value)))))

(defn coerce-key-node [node]
  (if (and (sequential? node)
           (= (first node) :key))
    [(coerce-key (second node))]
    node))

(defn coerce-selector-keys [destructured-selector]
  (postwalk coerce-key-node destructured-selector))

(defn coerce-selector-node [node]
  (if (and (sequential? node)
           (= (first node) :selector))
    (vector (second node))
    node))

(defn coerce-nested-selectors [destructured-selector]
  (postwalk coerce-selector-node destructured-selector))

(defn standalone-modifier? [item]
  (and (pos? (first item))
       (empty? (second item))))

(defn detect-standalone-modifier [state item]
  (if (standalone-modifier? item)
    (update state :pending-modifier #(or % item))                                                                             ; in case of multiple standalone modifiers in a row, the left-most one wins
    (update state :result conj item)))

(defn merge-standalone-modifier [modifier-item following-item]
  (list (first modifier-item) (second following-item)))

(defn merge-standalone-modifiers [items]
  (let [* (fn [state item]
            (if-some [pending-modifier (:pending-modifier state)]
              (let [merged-item (merge-standalone-modifier pending-modifier item)
                    state (assoc state :pending-modifier nil)]
                (detect-standalone-modifier state merged-item))
              (detect-standalone-modifier state item)))
        init-state {:result           []
                    :pending-modifier nil}
        processed-items (reduce * init-state items)]
    (:result processed-items)))

(defn build-selector-path [destructured-selector]
  {:post [(or (nil? %) (s/valid? ::sdefs/obj-path %))]}
  (let [path (when-not (= destructured-selector ::s/invalid)
               (->> destructured-selector
                    (coerce-selector-keys)
                    (coerce-nested-selectors)
                    (flatten)
                    (partition 2)
                    (merge-standalone-modifiers)
                    (map vec)))]
    (debug-assert (or (nil? path) (s/valid? ::sdefs/obj-path path)))
    path))

(defn selector->path [selector]
  (->> selector
       (s/conform ::sdefs/obj-selector)
       (build-selector-path)))

(defn static-selector? [selector]
  (s/valid? ::sdefs/obj-selector selector))

(defn get-access-modes [path]
  (map first path))

(defn find-offending-selector [selector-list offender-matcher]
  (let [* (fn [selector]
            (let [path (selector->path selector)
                  modes (get-access-modes path)]
              (when (some offender-matcher modes)
                selector)))]
    (some * selector-list)))

(defn check-and-report-invalid-mode! [modes mode selector-list message-type]
  (when (some #{mode} modes)
    (let [offending-selector (find-offending-selector selector-list #{mode})]
      (report-offending-selector-if-needed! offending-selector message-type))))

(defn check-static-path! [path op selector-list]
  (when (config/diagnostics?)
    (if (empty? path)
      (report-if-needed! :static-unexpected-empty-selector)
      (let [modes (get-access-modes path)]
        (case op
          :get (check-and-report-invalid-mode! modes punch-access selector-list :static-unexpected-punching-selector)
          :set (check-and-report-invalid-mode! modes soft-access selector-list :static-unexpected-soft-selector)))))
  path)
