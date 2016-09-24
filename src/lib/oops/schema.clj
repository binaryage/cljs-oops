(ns oops.schema
  "The code for compile-time conversion of selectors to paths. Uses clojure.spec to do the heavy-lifting."
  (:require [clojure.spec :as s]
            [clojure.walk :refer [postwalk]]
            [clojure.string :as string]
            [oops.sdefs :as sdefs]
            [oops.constants :refer [dot-access soft-access punch-access]]
            [oops.reporting :refer [report-if-needed! report-offending-selector-if-needed!]]
            [oops.debug :refer [debug-assert log]]))

; --- path utils ------------------------------------------------------------------------------------------------------------

(defn unescape-specials [s]
  (string/replace s #"^\\([?!])" "$1"))

(defn parse-selector-element [element-str]
  (case (first element-str)
    \? [soft-access (.substring element-str 1)]
    \! [punch-access (.substring element-str 1)]
    [dot-access (unescape-specials element-str)]))

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

(defn coerce-selector-keys [destured-selector]
  (postwalk coerce-key-node destured-selector))

(defn coerce-selector-node [node]
  (if (and (sequential? node)
           (= (first node) :selector))
    (vector (second node))
    node))

(defn coerce-nested-selectors [destured-selector]
  (postwalk coerce-selector-node destured-selector))

(defn build-selector-path [destructured-selector]
  {:post [(or (nil? %) (s/valid? ::sdefs/obj-path %))]}
  (let [path (if-not (= destructured-selector ::s/invalid)
               (->> destructured-selector
                    (coerce-selector-keys)
                    (coerce-nested-selectors)
                    (flatten)
                    (partition 2)
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
              (if (some offender-matcher modes)
                selector)))]
    (some * selector-list)))

(defn check-and-report-invalid-mode! [modes mode selector-list message-type]
  (if (some #{mode} modes)
    (let [offending-selector (find-offending-selector selector-list #{mode})]
      (report-offending-selector-if-needed! offending-selector message-type))))

(defn check-static-path! [path type selector-list]
  (if (empty? path)
    (report-if-needed! :static-empty-selector-access)
    (let [modes (get-access-modes path)]
      (case type
        :get (check-and-report-invalid-mode! modes punch-access selector-list :static-unexpected-punching-access)
        :set (check-and-report-invalid-mode! modes soft-access selector-list :static-unexpected-soft-access))))
  path)
