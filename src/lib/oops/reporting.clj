(ns oops.reporting
  "A subsystem for reporting compile-time issues depending on current config."
  (:refer-clojure :exclude [gensym])
  (:require [oops.config :as config]
            [oops.compiler :as compiler]
            [oops.debug :refer [log debug-assert]]
            [oops.state :as state]))

; -- reporting --------------------------------------------------------------------------------------------------------------

(defn suppress-reporting? [type]
  (boolean (get-in oops.state/*invocation-opts* [:suppress-reporting type])))

(defn report! [type & [info]]
  (case (config/get-config-key type)
    :warn (compiler/warn! type info)
    :error (compiler/error! type info)
    (false nil) nil))

(defn report-if-needed! [type & [info]]
  (when (config/diagnostics?)
    (when-not (suppress-reporting? type)
      (report! type info))))

(defn report-offending-selector-if-needed! [offending-selector type & [info]]
  (debug-assert offending-selector)
  (when (config/diagnostics?)
    (when-not (suppress-reporting? type)
      (let [point-to-offending-selector (into {} (filter second (select-keys (meta offending-selector) [:line :column])))]
        ; note that sometimes param meta could be missing, we don't alter state/*invocation-env* in that case
        (binding [state/*invocation-env* (merge state/*invocation-env* point-to-offending-selector)]
          (report! type info))))))
