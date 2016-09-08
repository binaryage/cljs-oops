(ns oops.config
  (:require-macros [oops.config :refer [gen-runtime-config]])
  (:require [oops.state :as state]))

(defn get-initial-runtime-config []
  (gen-runtime-config))

; -- public api -------------------------------------------------------------------------------------------------------------

(defn set-current-runtime-config! [new-config]
  {:pre [(map? new-config)]}
  (set! state/*runtime-config* new-config)
  new-config)

(defn get-current-runtime-config []
  (if (nil? state/*runtime-config*)
    (set-current-runtime-config! (get-initial-runtime-config))
    state/*runtime-config*))

(defn update-current-runtime-config! [f-or-map & args]
  (if (map? f-or-map)
    (update-current-runtime-config! merge f-or-map)
    (set-current-runtime-config! (apply f-or-map (get-current-runtime-config) args))))

; -- icing ------------------------------------------------------------------------------------------------------------------

(defn error-reporting-mode [& [config]]
  (:error-reporting (or config (get-current-runtime-config))))

(defn warning-reporting-mode [& [config]]
  (:warning-reporting (or config (get-current-runtime-config))))
