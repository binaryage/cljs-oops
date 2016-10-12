(ns oops.config
  "Code supporting dynamic (run-time) configuration. See runtime-prefixed keys in config map in defaults.clj."
  (:require-macros [oops.config :refer [gen-runtime-config]]))

(defn get-initial-runtime-config []
  (gen-runtime-config))

; use oops.config/get-current-runtime-config to get currently effective config
(def ^:dynamic *runtime-config* (get-initial-runtime-config))

; -- public api -------------------------------------------------------------------------------------------------------------

(defn set-current-runtime-config! [new-config]
  {:pre [(map? new-config)]}
  (set! *runtime-config* new-config)
  new-config)

(defn get-current-runtime-config []
  *runtime-config*)

(defn update-current-runtime-config! [f-or-map & args]
  (if (map? f-or-map)
    (update-current-runtime-config! merge f-or-map)
    (set-current-runtime-config! (apply f-or-map (get-current-runtime-config) args))))

; -- icing ------------------------------------------------------------------------------------------------------------------

(defn get-config-key [key & [config]]
  (key (or config (get-current-runtime-config))))

(defn ^boolean has-config-key? [key & [config]]
  (not= ::not-found (get (or config (get-current-runtime-config)) key ::not-found)))

(defn get-error-reporting [& [config]]
  (get-config-key :error-reporting config))

(defn get-warning-reporting [& [config]]
  (get-config-key :warning-reporting config))

(defn get-suppress-reporting [& [config]]
  (get-config-key :suppress-reporting config))

(defn get-child-factory [& [config]]
  (get-config-key :child-factory config))

(defn set-child-factory! [new-factory-fn]
  (update-current-runtime-config! {:child-factory new-factory-fn}))

(defn ^boolean throw-errors-from-macro-call-sites? [& [config]]
  (true? (get-config-key :throw-errors-from-macro-call-sites config)))

(defn ^boolean use-envelope? [& [config]]
  (true? (get-config-key :use-envelope config)))
