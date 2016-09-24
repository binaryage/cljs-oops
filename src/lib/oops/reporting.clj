(ns oops.reporting
  (:refer-clojure :exclude [gensym])
  (:require [oops.schema :as schema]
            [oops.config :as config]
            [oops.helpers :refer [gensym]]
            [oops.compiler :as compiler :refer [with-compiler-context! with-compiler-opts!]]
            [oops.constants :refer [dot-access soft-access punch-access]]
            [oops.debug :refer [log debug-assert]]
            [oops.state :as state]))

; -- reporting --------------------------------------------------------------------------------------------------------------

(defn supress-reporting? [type]
  (boolean (get-in oops.state/*invocation-opts* [:suppress-reporting type])))

(defn report! [type & [info]]
  (case (config/get-config-key type)
    :warn (compiler/warn! type info)
    :error (compiler/error! type info)
    (false nil) nil))

(defn report-if-needed! [type & [info]]
  (if (config/diagnostics?)
    (if-not (supress-reporting? type)
      (report! type info))))

(defn find-first-dynamic-selector [selector-list]
  (first (remove schema/static-selector? selector-list)))

(defn report-dynamic-selector-usage-if-needed! [selector-list]
  (if (config/diagnostics?)
    (if-not (supress-reporting? :dynamic-selector-usage)
      (let [offending-selector (find-first-dynamic-selector selector-list)]
        (debug-assert offending-selector)
        (let [point-to-offending-selector (into {} (filter second (select-keys (meta offending-selector) [:line :column])))]
          ; note that sometimes param meta could be missing, we don't alter state/*invocation-env* in that case
          (binding [state/*invocation-env* (merge state/*invocation-env* point-to-offending-selector)]
            (report! :dynamic-selector-usage)))))))
