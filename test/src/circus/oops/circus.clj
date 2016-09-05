(ns oops.circus
  (:require [clojure.test :refer :all]
            [cljs.build.api :as api]
            [cljs.util :as cljs-util]
            [environ.core :refer [env]]
            [clj-logging-config.log4j :as config]
            [clojure.tools.logging :as log]
            [oops.tools :refer [get-arena-separator]]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [cuerdas.core :as cuerdas]
            [clojure.java.shell :as shell]
            [clojure.stacktrace :as stacktrace]
            [clansi])
  (:import (org.apache.log4j Level)))

(defn report-error [& args]
  (binding [*out* *err*]
    (apply println args)))

(def log-level (or (env :oops-log-level) "INFO"))                                                                             ; INFO, DEBUG, TRACE, ALL

(defn setup-logging! []
  (config/set-loggers! :root {:out   :console
                              :level (Level/toLevel log-level Level/INFO)}))

(def common-options
  {:optimizations :advanced})

(defn build-options [main]
  (let [out (last (string/split main #"\."))]
    (merge common-options {:pseudo-names  true
                           :elide-asserts true
                           :main          (symbol main)
                           :output-dir    (str "test/resources/_compiled/" out "/_workdir")
                           :output-to     (str "test/resources/_compiled/" out "/main.js")})))

(def builds
  [{:source  "test/src/arena/oops/arena/test1.cljs"
    :options (build-options "oops.arena.test1")}])

(defn get-build-name [build]
  (let [{:keys [source]} build]
    (string/replace source #"test\/src\/arena\/oops\/" "")))

(defn get-actual-transcript-path [build]
  (let [{:keys [source]} build]
    (string/replace source #"\/([^/]*)\.cljs$" "/.actual/$1.txt")))

(defn get-expected-transcript-path [build]
  (let [{:keys [source]} build]
    (string/replace source #"\.cljs$" ".txt")))

(defn produce-diff [path1 path2]
  (let [options-args ["-U" "5"]
        paths-args [path1 path2]]
    (try
      (let [result (apply shell/sh "colordiff" (concat options-args paths-args))]
        (if-not (empty? (:err result))
          (clansi/style (str "! " (:err result)) :red)
          (:out result)))
      (catch Throwable e
        (clansi/style (str "! " (.getMessage e)) :red)))))

(defn get-canonical-line [line]
  (string/trimr line))

(defn significant-line? [line]
  (not (empty? line)))

(defn append-nl [text]
  (str text "\n"))

(defn get-canonical-transcript [transcript]
  (->> transcript
       (cuerdas/lines)
       (map get-canonical-line)
       (filter significant-line?)                                                                                             ; filter empty lines to work around end-of-the-file new-line issue
       (cuerdas/unlines)
       (append-nl)))                                                                                                          ; we want to be compatible with "copy transcript!" button which copies to clipboard with extra new-line

(defn read-build-output [build]
  (let [output-path (get-in build [:options :output-to])]
    (try
      (slurp output-path)
      (catch Exception e
        (str "Error reading output: " (.getMessage e))))))

(defn extract-relevant-output [content]
  (let [separator (get-arena-separator)]
    (if-let [separator-index (string/last-index-of content separator)]
      (let [nl-index (or (string/index-of content "\n" separator-index) separator-index)
            relevant-content (.substring content (+ nl-index 1))]
        relevant-content)
      content)))

(defn safe-spit [path content]
  (io/make-parents path)
  (spit path content))

(defn print-banner! []
  (println)
  (println (str "Running compiler output tests under "
                "Clojure v" (clojure-version) " and "
                "ClojureScript v" (cljs-util/clojurescript-version)))
  (println "===================================================================================================="))

; -- building ---------------------------------------------------------------------------------------------------------------

(defn build! [build]
  (log/info (str "Building '" (get-build-name build) "'"))
  (api/build (:source build) (:options build)))

(defn write-build-transcript! [build]
  (let [actual-transcript-path (get-actual-transcript-path build)
        relevant-output (-> (read-build-output build)
                            (extract-relevant-output)
                            (get-canonical-transcript))]
    (log/debug (str "Writing build transcript to '" actual-transcript-path "' (" (count relevant-output) " chars)"))
    (safe-spit actual-transcript-path relevant-output)))

(defn compare-transcripts! [build]
  (log/debug (str "Comparing build transcript with expected output..."))
  (try
    (let [expected-path (get-expected-transcript-path build)
          actual-path (get-actual-transcript-path build)
          expected-transcript (slurp expected-path)
          actual-transcript (slurp actual-path)]
      (when-not (= actual-transcript expected-transcript)
        (println)
        (println "-----------------------------------------------------------------------------------------------------")
        (println (str "! actual transcript differs for '" (get-build-name build) "' build:"))
        (println)
        (println (produce-diff expected-path actual-path))
        (println "-----------------------------------------------------------------------------------------------------")
        (println (str "> cat " actual-path))
        (println)
        (println actual-transcript)
        (do-report {:type     :fail
                    :message  (str "Build '" (get-build-name build) "' failed to match expected transcript.")
                    :expected (str "to match expected transcript " expected-path)
                    :actual   (str "didn't match, see " actual-path)}))
      (do-report {:type    :pass
                  :message (str "Build '" (get-build-name build) "' passed.")}))
    (catch Throwable e
      (do-report {:type     :fail
                  :message  (str "Build '" (get-build-name build) "' failed with an exception.")
                  :expected "no exception"
                  :actual   (str e)})
      (stacktrace/print-stack-trace e))))

(defn exercise-build! [build]
  (build! build)
  (write-build-transcript! build)
  (compare-transcripts! build))

(defn exercise-builds! [builds]
  (doseq [build builds]
    (exercise-build! build)))

; -- tests ------------------------------------------------------------------------------------------------------------------

(deftest exercise-all-builds
  (exercise-builds! builds))

; -- main entry point -------------------------------------------------------------------------------------------------------

(defn -main []
  (setup-logging!)
  (print-banner!)

  (let [summary (run-tests 'oops.circus)]
    (if-not (successful? summary)
      (System/exit 1)
      (System/exit 0))))
