(ns oops.config
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

(defn get-error-reporting [& [config]]
  (:error-reporting (or config (get-current-runtime-config))))

(defn get-warning-reporting [& [config]]
  (:warning-reporting (or config (get-current-runtime-config))))

(defn get-child-factory [& [config]]
  (:child-factory (or config (get-current-runtime-config))))

(defn set-child-factory! [new-factory-fn]
  (update-current-runtime-config! {:child-factory new-factory-fn}))
