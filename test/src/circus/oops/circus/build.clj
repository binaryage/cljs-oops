(ns oops.circus.build
  (:require [clojure.string :as string]
            [clojure.test :refer [do-report]]
            [clojure.tools.logging :as log]
            [clojure.stacktrace :as stacktrace]
            [cljs.build.api :as compiler]
            [environ.core :refer [env]]
            [oops.circus.utils :refer :all]
            [oops.circus.config :as config]
            [cljs.util :as cljs-util])
  (:import (java.io File StringWriter)))

(def section-separator
  "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")

(defn post-process-build-options [options]
  (cond-> options
          (not= :advanced (:optimizations options)) (assoc :elide-asserts false)))

(defn build-options [main variant config & [overrides]]
  (assert main (str "main must be specified!"))
  (let [out (str (last (string/split main #"\.")) "-" variant)
        compiler-config (merge {:pseudo-names  true
                                :elide-asserts true
                                :optimizations :advanced
                                :main          (symbol main)
                                :output-dir    (str "test/resources/_compiled/" out "/_workdir")
                                :output-to     (str "test/resources/_compiled/" out "/main.js")}
                               (if-not (empty? config)
                                 {:external-config {:oops/config config}}))]
    (post-process-build-options (merge compiler-config overrides))))

(defn extract-filename-from-source-path [source]
  (let [group (re-find #"\/([^/]*)\.cljs$" source)]
    (assert group (str "unable to parse script name from '" source "'"))
    (second group)))

(defn get-main-ns-from-source [source]
  (if-let [m (re-matches #"test\/src\/arena\/(.*?)\.cljs" source)]
    (-> (second m)
        (string/replace #"\/" ".")
        (string/replace #"_" "-"))))

(defn make-build [file variant & [config overrides]]
  (let [source (str "test/src/arena/oops/arena/" file)]
    {:source  source
     :variant variant
     :options (build-options (get-main-ns-from-source source) variant config overrides)}))

(defn get-key-mode-options [mode]
  {:key-set mode
   :key-get mode})

(defn get-build-name [build]
  (let [{:keys [source variant]} build]
    (str (string/replace source #"test\/src\/arena\/oops\/" "") " [" variant "]")))

(defn make-build-info [build]
  (str (get-build-name build) "\n"
       (pprint-str (into (sorted-map) (:options build)))))

(defn make-build-variants [file]
  [(make-build file "core" (get-key-mode-options :core))
   (make-build file "goog" (get-key-mode-options :goog))])

(defn get-environment-info []
  (str "Clojure v" (clojure-version) ", "
       "ClojureScript v" (cljs-util/clojurescript-version)))

; -- transcripts ------------------------------------------------------------------------------------------------------------

(defn get-transcript-path [kind build]
  (let [{:keys [source variant]} build
        filename (extract-filename-from-source-path source)]
    (str "test/transcripts/" kind "/" filename "_" variant ".js")))

(defn get-actual-transcript-path [build]
  (get-transcript-path "actual" build))

(defn get-expected-transcript-path [build]
  (get-transcript-path "expected" build))

; -- top-level functionality ------------------------------------------------------------------------------------------------

(defn read-build-output [build]
  (let [output-path (get-in build [:options :output-to])]
    (try
      (slurp output-path)
      (catch Exception e
        (str "Error reading output: " (.getMessage e))))))

(defn clean-build! [build]
  (let [output-dir (get-in build [:options :output-dir])]
    (assert (not (empty? output-dir)))
    (log/debug (str "Cleaning by deleting '" output-dir "'"))
    (recursive-delete (File. output-dir)))
  (let [output-to (get-in build [:options :output-to])]
    (assert (not (empty? output-to)))
    (log/debug (str "Cleaning by deleting '" output-to "'"))
    (.delete (File. output-to))))

(defn write-build-transcript! [compilation-result]
  (let [{:keys [build code out err]} compilation-result
        separator "----------------------------------------------------------------------------------------------------------"
        actual-transcript-path (get-actual-transcript-path build)
        post-processed-code (post-process-code code)
        build-info (make-build-info build)
        parts [(str "// " (get-environment-info))
               (str "// COMPILER CONFIG:\n" (comment-out-text build-info "  "))
               (if-not (empty? out) (str "// COMPILER STDOUT:\n" (comment-out-text out "  ")))
               (if-not (empty? err) (str "// COMPILER STDERR:\n" (comment-out-text err "  ")))
               post-processed-code]
        transcript (string/join "\n" (interpose (comment-out-text separator) (remove nil? parts)))
        canonical-transcript (get-canonical-transcript transcript)]
    (log/debug (str "Writing build transcript to '" actual-transcript-path "' (" (count canonical-transcript) " chars)"))
    (safe-spit actual-transcript-path canonical-transcript)
    (beautify-js! actual-transcript-path)))

(defn compare-transcripts! [build]
  (log/debug (str "Comparing build transcript with expected output..."))
  (try
    (let [expected-path (get-expected-transcript-path build)
          expected-transcript (silent-slurp expected-path)
          actual-path (get-actual-transcript-path build)
          actual-transcript (silent-slurp actual-path)]
      (when-not (= actual-transcript expected-transcript)
        (println)
        (println section-separator)
        (println (str "! actual transcript differs for '" (get-build-name build) "' build:"))
        (println (str "> cat " actual-path))
        (println (dim-text actual-transcript))
        (println section-separator)
        (println (produce-diff expected-path actual-path))
        (println section-separator)
        (println)
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

(defn compile! [build]
  (if-not (config/skip-clean? env)
    (clean-build! build))
  (log/info (str "Building '" (get-build-name build) "'"))
  (let [captured-out (new StringWriter)
        captured-err (new StringWriter)]
    (binding [*out* captured-out
              *err* captured-err]
      (try
        (compiler/build (:source build) (:options build))
        (catch Throwable e
          (let [causes (with-out-str
                         (print-cause-chain e))]
            (.write *err* (str "THROWN: " causes))))))
    {:build build
     :out   (str captured-out)
     :err   (str captured-err)
     :code  (or (extract-relevant-output (read-build-output build)) "// NO GENERATED CODE")}))

(defn process-build! [build]
  (let [compilation-result (compile! build)]
    (write-build-transcript! compilation-result)
    (compare-transcripts! build)))

(defn exercise-build! [build]
  (let [build-name (get-build-name build)]
    (if-let [reason (config/skip-build? env build-name)]
      (log/info (str "Skipping '" build-name "' because of " reason))
      (process-build! build))))

; -- entry point ------------------------------------------------------------------------------------------------------------

(defn exercise-builds! [builds]
  (doseq [build builds]
    (exercise-build! build)))
