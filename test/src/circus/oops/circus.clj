(ns oops.circus
  (:require [clojure.test :as test]
            [cljs.util :as cljs-util]
            [clj-logging-config.log4j :as log4j-config]
            [environ.core :refer [env]]
            [oops.circus.config :as config]
            [oops.arena])
  (:import (org.apache.log4j Level)))

(defn setup-logging! []
  (let [level (Level/toLevel (config/get-log-level env) Level/INFO)]
    (log4j-config/set-loggers! :root {:out   :console
                                      :level level})))

(defn print-banner! []
  (let [banner (str "Running compiler output tests under "
                    "Clojure v" (clojure-version) " and "
                    "ClojureScript v" (cljs-util/clojurescript-version))]
    (println)
    (println banner)
    (println "====================================================================================================")))

; -- main entry point -------------------------------------------------------------------------------------------------------

(defn -main []
  (setup-logging!)
  (print-banner!)

  (let [summary (test/run-tests 'oops.arena)]
    (if-not (test/successful? summary)
      (System/exit 1)
      (System/exit 0))))
