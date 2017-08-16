(ns oops.circus.build
  (:require [clojure.string :as string]
            [clojure.test :refer [do-report]]
            [clojure.tools.logging :as log]
            [clojure.stacktrace :as stacktrace]
            [cljs.build.api :as compiler]
            [environ.core :refer [env]]
            [oops.circus.utils :refer :all]
            [oops.circus.config :as config])
  (:import (java.io File StringWriter)))

(def section-separator
  "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")

(defn post-process-build-options [options]
  (cond-> options
          (not= :advanced (:optimizations options)) (assoc :elide-asserts false)
          true (assoc-in [:external-config :devtools/config :silence-optimizations-warning] true)))

(defn build-options [main variant config & [overrides]]
  (assert main (str "main must be specified!"))
  (let [out (str (last (string/split main #"\.")) (if-not (empty? variant) (str "-" variant)))
        compiler-config (merge {:pseudo-names  true
                                :elide-asserts true
                                :optimizations :advanced
                                :main          (symbol main)
                                :output-dir    (str "test/resources/.compiled/" out "/_workdir")
                                :output-to     (str "test/resources/.compiled/" out "/main.js")}
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
    (str (string/replace source #"test\/src\/arena\/oops\/" "") (if-not (empty? variant) (str " [" variant "]")))))

(defn make-build-info [build]
  (str (get-build-name build) "\n"
       (pprint-str (into (sorted-map) (:options build)))))

(defn get-environment-info []
  (str "js-beautify v" (js-beautify-version)))

; -- transcripts ------------------------------------------------------------------------------------------------------------

(defn get-transcript-path [kind build]
  (let [{:keys [source variant]} build
        filename (extract-filename-from-source-path source)]
    (str "test/transcripts/" kind "/" filename (if-not (empty? variant) (str "_" variant)) ".js")))

(defn get-actual-transcript-path [build]
  (get-transcript-path "_actual_" build))

(defn get-expected-transcript-path [build]
  (get-transcript-path "expected" build))

; -- top-level functionality ------------------------------------------------------------------------------------------------

(defn read-build-output [build]
  (let [output-path (get-in build [:options :output-to])]
    (try
      ; for some reason cljs compilations do not provide stable output - depends on tests order/filtering
      ;
      ; my post to #cljs-dev:
      ;   just wondering if there is some other state outside cljs.env to be addressed to get reproducible builds,
      ;   I have some tests checking cljs compiler output in different compilation modes:
      ;     https://github.com/binaryage/cljs-oops/tree/master/test/transcripts/expected
      ;   it works pretty well, the problem is when I decide to change order of the tests or filter some out,
      ;   I get slightly different results, the generated code is still correct, but js-beautify will format/insert
      ;   linebreaks to some other places for some reason
      ;
      ;   I just ran directory diff for two builds which happen to be different, and it looks that `gensym` and similar do not
      ;   reset when calling new `compiler/build`, this is probably causing flakiness of my tests, because build depends on
      ;   state of the system left from previous builds
      ; ---
      ;
      ;   ah, nevermind, found the problem, because `gensym` and similar are not reset for each new build, we may end up with
      ;   generated identifiers of different length, e.g. my-sym10 vs. my-sym12345, and this affects linebreaks of cljs
      ;   output, the solution for me was to remove all linebreaks to normalize the output before processing it further
      ;   that alone is not enough to get stable output, I also have to “reindex” all auto-gen and gensym’d symbols with
      ;   stable indexing, but that I had to do anyways
      ;
      ; so here I canoninze file by removing all linebreaks and let js-beautify do its job later
      (let [raw-output (slurp output-path)]
        (string/replace raw-output #"\n" " "))
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
               (if-not (empty? out) (str "// COMPILER STDOUT:\n" (comment-out-text (post-process-compiler-output out) "  ")))
               (if-not (empty? err) (str "// COMPILER STDERR:\n" (comment-out-text (post-process-compiler-output err) "  ")))
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
