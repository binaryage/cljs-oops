(ns oops.config
  (:require [oops.state]
            [oops.debug :refer [log]]))

(def default-runtime-config
  {; diagnostics...
   :error-reporting-mode   :throw                                                                                             ; #{:throw :console false}
   :warning-reporting-mode :console                                                                                           ; #{:throw :console false}
   })

(def default-compiler-config
  {:diagnostics    true
   :key-get-mode   :core                                                                                                      ; #{:core :goog}
   :key-set-mode   :core                                                                                                      ; #{:core :goog}
   :runtime-config default-runtime-config})

(def advanced-mode-compiler-config-overrides
  {:diagnostics false})

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn advanced-mode? []
  (if cljs.env/*compiler*
    (= (get-in @cljs.env/*compiler* [:options :optimizations]) :advanced)))

(defn prepare-default-config []
  (merge default-compiler-config (if (advanced-mode?) advanced-mode-compiler-config-overrides)))

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
  (get-compiler-config))

; -- runtime macros ---------------------------------------------------------------------------------------------------------

(defmacro with-runtime-config [config & body]
  `(binding [oops.state/*runtime-config* (merge (get-current-runtime-config) ~config)]
     ~@body))

(defmacro gen-runtime-config []
  (:runtime-config (get-current-compiler-config)))

; -- icing ------------------------------------------------------------------------------------------------------------------

(defn diagnostics? [& [config]]
  (true? (:diagnostics (or config (get-current-compiler-config)))))

(defn key-get-mode [& [config]]
  (:key-get-mode (or config (get-current-compiler-config))))

(defn key-set-mode [& [config]]
  (:key-set-mode (or config (get-current-compiler-config))))
