(ns oops.circus.config
  (:require [oops.circus.utils :as utils]))

(defn get-log-level [env]
  (or (:oops-log-level env) "INFO"))                                                                                          ; INFO, DEBUG, TRACE, ALL

(defn skip-clean? [env]
  (not (empty? (:oops-ft-skip-clean env))))

(defn skip-build? [env build-name]
  (let [filter-str (:oops-ft-filter env)
        filter-fn (utils/get-build-filter filter-str)]
    (if (filter-fn build-name)
      (str "env OOPS_FT_FILTER='" filter-str "'"))))
