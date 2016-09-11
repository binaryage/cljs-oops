(ns oops.circus.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [cuerdas.core :as cuerdas]
            [clansi]
            [clojure.java.shell :as shell]
            [clojure.tools.logging :as log]
            [clojure.pprint :refer [pprint]]
            [oops.tools :as tools]))

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

(defn append-nl [text]
  (str text "\n"))

(defn get-canonical-transcript [transcript]
  (let [s (->> transcript
               (cuerdas/lines)
               (map get-canonical-line)
               (cuerdas/unlines)
               (append-nl))]                                                                                                  ; we want to be compatible with "copy transcript!" button which copies to clipboard with extra new-line
    (string/replace s #"\n\n+" "\n\n")))

(defn extract-relevant-output [content]
  (let [separator (tools/get-arena-separator)]
    (if-let [separator-index (string/last-index-of content separator)]
      (let [semicolon-index (or (string/index-of content ";" separator-index) separator-index)
            relevant-content (.substring content (+ semicolon-index 1))]
        relevant-content))))

(defn make-empty-normalizer-state []
  {:counter  1
   :mappings {}})

(defn get-counter [normalizer-state]
  (:counter normalizer-state))

(defn register-mapping [normalizer-state name replacement]
  (-> normalizer-state
      (update :counter inc)
      (update :mappings assoc name replacement)))

(defn get-mapping [normalizer-state name]
  (get-in normalizer-state [:mappings name]))

(defn register-mapping-if-needed [normalizer-state name replacement]
  (if (get-mapping normalizer-state name)
    normalizer-state
    (register-mapping normalizer-state name replacement)))

(defn normalize-identifiers [[content normalizer-state]]
  "The goal here is to rename all generated $<number>$ identifiers with stable numbering."
  (let [* (fn [[content normalizer-state] match]
            (let [needle (first match)
                  replacement (str (get-counter normalizer-state) (nth match 2))
                  new-normalizer-state (register-mapping-if-needed normalizer-state needle replacement)
                  new-content (string/replace content needle (get-mapping new-normalizer-state needle))]
              [new-content new-normalizer-state]))]
    (reduce * [content normalizer-state] (re-seq #"(\d+)(\$|__)" content))))

(defn normalize-gensyms [[content normalizer-state]]
  "The goal here is to rename all generated name<NUM> identifiers with stable numbering."
  (let [* (fn [[content normalizer-state] match]
            (if (> (Long/parseLong (first match)) 1000)
              (let [needle (first match)
                    replacement (str (get-counter normalizer-state))
                    new-normalizer-state (register-mapping-if-needed normalizer-state needle replacement)
                    new-content (string/replace content needle (get-mapping new-normalizer-state needle))]
                [new-content new-normalizer-state])
              [content normalizer-state]))]
    (reduce * [content normalizer-state] (re-seq #"(\d+)" content))))

(defn normalize-twins [[content normalizer-state]]
  "The goal here is to rename all generated <NUM>_<NUM> identifiers with stable numbering."
  (let [* (fn [[content normalizer-state] needle]
            (let [replacement (str (get-counter normalizer-state))
                  new-normalizer-state (register-mapping-if-needed normalizer-state needle replacement)
                  new-content (string/replace content needle (get-mapping new-normalizer-state needle))]
              [new-content new-normalizer-state]))]
    (reduce * [content normalizer-state] (re-seq #"\d+_\d+" content))))

(defn safe-spit [path content]
  (io/make-parents path)
  (spit path content))

(defn pprint-str [v & [length level]]
  (with-out-str
    (binding [*print-level* (or level 10)
              *print-length* (or length 10)]
      (pprint v))))

(defn beautify-js! [path]
  (log/debug (str "Beautify JS at '" path "'"))
  (let [options-args ["-n" "-s" "2"]
        paths-args ["-f" path "-o" path]]
    (try
      (let [result (apply shell/sh "js-beautify" (concat options-args paths-args))]
        (if-not (empty? (:err result))
          (log/error (str "! " (:err result)))))
      (catch Throwable e
        (log/error (str "! " (.getMessage e)))))))

(defn comment-out-text [s & [stuffer]]
  (->> s
       (cuerdas/lines)
       (map #(str "// " stuffer %))
       (cuerdas/unlines)))

; taken from: https://github.com/macourtney/clojure-tools/blob/dcc9853514756f2f4fc3bfdfdba45abffd94c5dd/src/clojure/tools/file_utils.clj#L83
(defn recursive-delete [directory]
  (if (.isDirectory directory)
    (when (reduce #(and %1 (recursive-delete %2)) true (.listFiles directory))
      (.delete directory))
    (.delete directory)))

(defn unwrap-snippets [content]
  (let [counter (volatile! 0)
        * (fn [match]
            (vswap! counter inc)
            (let [raw (string/replace (tools/decode (second match)) "\\n" "\n")
                  commented (comment-out-text raw "  ")]
              (str "\n"
                   "\n"
                   "// SNIPPET #" @counter ":\n"
                   commented "\n"
                   "// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                   "\n"
                   "\n")))]
    (string/replace content #"console\.log\(['\"]-12345-SNIPPET:(.*?)-54321-['\"]\);" *)))

(defn replace-tagged-literals [content]
  (let [counter (volatile! 0)
        * (fn [match]
            (vswap! counter inc)
            (let [name (nth match 1)]
              (str "<" name "#" @counter ">")))]
    (string/replace content #"#object\[cljs\.tagged_literals\.(.*?) 0x(.*?) \"cljs\.tagged_literals\.(.*?)@(.*?)\"]" *)))

(defn post-process-code [code]
  (let [unwrapped-code (-> code
                           (unwrap-snippets)
                           (replace-tagged-literals))
        [stabilized-code normalizer-state] (-> [unwrapped-code (make-empty-normalizer-state)]
                                               (normalize-identifiers)
                                               (normalize-gensyms)
                                               (normalize-twins))]
    (log/debug "normalizer state:\n" (pprint-str normalizer-state 10000))
    stabilized-code))

(defn silent-slurp [path]
  (try
    (slurp path)
    (catch Throwable _
      "")))

(defn make-build-filter [filter]
  (if (empty? filter)
    (constantly false)
    (fn [name]
      (let [parts (string/split filter #"\s")
            * (fn [part]
                (re-find (re-pattern part) name))]
        (not (some * parts))))))

(def get-build-filter (memoize make-build-filter))

(defn print-cause-chain [tr]
  (clojure.stacktrace/print-throwable tr)
  (println)
  (when-let [cause (.getCause tr)]
    (print "Caused by: ")
    (recur cause)))
