(ns oops.config
  (:require [cljs.env]
            [oops.state]
            [oops.debug :refer [log]]))

(def default-config                                                                                                           ; falsy below means 'nil' or 'false'
  {:diagnostics               true                                                                                            ; #{true falsy}
   :key-get                   :core                                                                                           ; #{:core :goog}
   :key-set                   :core                                                                                           ; #{:core :goog}

   ; compiler warnigns/errors
   :dynamic-property-access   :warn                                                                                           ; #{:error :warn falsy}

   ; runtime config
   :runtime-error-reporting   :throw                                                                                          ; #{:throw :console falsy}
   :runtime-warning-reporting :console                                                                                        ; #{:throw :console falsy}
   })

(def advanced-mode-compiler-config-overrides
  {:diagnostics false})

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn advanced-mode? []
  (if cljs.env/*compiler*
    (= (get-in @cljs.env/*compiler* [:options :optimizations]) :advanced)))

(defn prepare-default-config []
  (merge default-config (if (advanced-mode?) advanced-mode-compiler-config-overrides)))

(defn read-project-config []
  (if cljs.env/*compiler*
    (get-in @cljs.env/*compiler* [:options :external-config :oops/config])))                                                  ; https://github.com/bhauman/lein-figwheel/commit/80f7306bf5e6bd1330287a6f3cc259ff645d899b

(defn read-env-config []
  {})                                                                                                                         ; TODO: write a library for this

(defn ^:dynamic get-compiler-config []
  {:post [(map? %)]}                                                                                                          ; TODO: validate config using spec or bhauman's tooling
  (merge (prepare-default-config) (read-project-config) (read-env-config)))

; -- public api--------------------------------------------------------------------------------------------------------------

(defn get-current-compiler-config []
  (get-compiler-config))                                                                                                      ; TODO: should we somehow cache this?

(defn get-runtime-config [& [config]]
  (let [* (fn [[key val]]
            (if-let [m (re-matches #"^runtime-(.*)$" (name key))]
              [(keyword (second m)) val]))]
    ; select all :runtime-something keys and drop :runtime- prefix
    (into {} (keep * (or config (get-current-compiler-config))))))

; -- runtime macros ---------------------------------------------------------------------------------------------------------

(defmacro with-runtime-config [config & body]
  `(binding [oops.state/*runtime-config* (merge (get-current-runtime-config) ~config)]
     ~@body))

(defmacro gen-runtime-config [& [config]]
  (get-runtime-config config))

; -- icing ------------------------------------------------------------------------------------------------------------------

(defn diagnostics? [& [config]]
  {:post [(contains? #{true false nil} %)]}
  (true? (:diagnostics (or config (get-current-compiler-config)))))

(defn key-get-mode [& [config]]
  {:post [(contains? #{:core :goog} %)]}
  (:key-get (or config (get-current-compiler-config))))

(defn key-set-mode [& [config]]
  {:post [(contains? #{:core :goog} %)]}
  (:key-set (or config (get-current-compiler-config))))

(defn dynamic-property-access-mode [& [config]]
  {:post [(contains? #{:error :warn nil false} %)]}
  (:dynamic-property-access (or config (get-current-compiler-config))))
