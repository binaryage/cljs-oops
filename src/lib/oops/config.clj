(ns oops.config
  (:require [cljs.env]
            [oops.state]
            [oops.defaults :as defaults]))

; this is for testing, see with-compiler-config macro below
(def adhoc-config-overrides (volatile! {}))

; -- helpers ----------------------------------------------------------------------------------------------------------------

(defn get-adhoc-config-overrides []
  @adhoc-config-overrides)

(defn advanced-mode? []
  (if cljs.env/*compiler*
    (= (get-in @cljs.env/*compiler* [:options :optimizations]) :advanced)))

(defn prepare-default-config []
  (merge defaults/config (if (advanced-mode?) defaults/advanced-mode-config-overrides)))

(defn read-project-config []
  (if cljs.env/*compiler*
    (get-in @cljs.env/*compiler* [:options :external-config :oops/config])))                                                  ; https://github.com/bhauman/lein-figwheel/commit/80f7306bf5e6bd1330287a6f3cc259ff645d899b

(defn read-env-config []
  {})                                                                                                                         ; TODO: write a library for this

(defn ^:dynamic get-compiler-config []
  {:post [(map? %)]}                                                                                                          ; TODO: validate config using spec or bhauman's tooling
  (merge (prepare-default-config)
         (read-project-config)
         (read-env-config)
         (get-adhoc-config-overrides)))

; -- compiler config access -------------------------------------------------------------------------------------------------

(defn get-current-compiler-config []
  (get-compiler-config))                                                                                                      ; TODO: should we somehow cache this?

(defn get-config-key [key & [config]]
  (key (or config (get-current-compiler-config))))

(defn get-runtime-config [& [config]]
  (let [* (fn [[key val]]
            (if-let [m (re-matches #"^runtime-(.*)$" (name key))]
              [(keyword (second m)) val]))]
    ; select all :runtime-something keys and drop :runtime- prefix
    (into {} (keep * (or config (get-current-compiler-config))))))

; -- updates to compiler config during compilation --------------------------------------------------------------------------

(def adhoc-config-overrides-stack (volatile! []))

(defmacro push-config-overrides! [config]
  (let [current-adhoc-config-overrides @adhoc-config-overrides]
    (vreset! adhoc-config-overrides-stack (conj @adhoc-config-overrides-stack current-adhoc-config-overrides))
    (vreset! adhoc-config-overrides (merge current-adhoc-config-overrides config))))

(defmacro pop-config-overrides! []
  (let [stack @adhoc-config-overrides-stack]
    (assert (pos? (count stack)))
    (vreset! adhoc-config-overrides (last stack))
    (vreset! adhoc-config-overrides-stack (butlast stack))))

; note: we rely on macroexpand behaviour here, it will expand
; push-compiler-config first, then macros in body and then pop-compiler-config
; this way we can tweak compiler config for an isolated code piece, which is handy for testing
(defmacro with-compiler-config [config & body]
  `(do
     (oops.config/push-config-overrides! ~config)
     ~@body
     (oops.config/pop-config-overrides!)))

; -- runtime macros ---------------------------------------------------------------------------------------------------------

(defmacro with-runtime-config [config & body]
  `(binding [*runtime-config* (merge (get-current-runtime-config) ~config)]
     ~@body))

(defmacro with-child-factory [factory-fn & body]
  `(with-runtime-config {:child-factory ~factory-fn}
     ~@body))

(defmacro gen-runtime-config [& [config]]
  (get-runtime-config config))

; -- icing ------------------------------------------------------------------------------------------------------------------

(defn diagnostics? [& [config]]
  {:post [(contains? #{true false nil} %)]}
  (true? (get-config-key :diagnostics config)))

(defn key-get-mode [& [config]]
  {:post [(contains? #{:core :goog} %)]}
  (get-config-key :key-get config))

(defn key-set-mode [& [config]]
  {:post [(contains? #{:core :goog} %)]}
  (get-config-key :key-set config))

(defn strict-punching? [& [config]]
  {:post [(contains? #{true false nil} %)]}
  (get-config-key :strict-punching config))

(defn debug? [& [config]]
  {:post [(contains? #{true false nil} %)]}
  (true? (get-config-key :debug config)))
