(ns oops.circus.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [cuerdas.core :as cuerdas]
            [clansi]
            [clojure.java.shell :as shell]
            [clojure.tools.logging :as log]
            [clojure.pprint :refer [pprint]]
            [oops.tools :as tools])
  (:import (java.util.regex Matcher Pattern)))

(defn produce-diff [path1 path2]
  (let [options-args ["-U" "5"]
        paths-args [path1 path2]]
    (try
      (let [result (apply shell/sh "colordiff" (concat options-args paths-args))]
        (if-not (empty? (:err result))
          (clansi/style (str "! " (:err result)) :red)
          (cuerdas/rtrim (:out result))))
      (catch Throwable e
        (clansi/style (str "! " (.getMessage e)) :red)))))

(defn js-beautify-version []
  (let [options-args ["--version"]]
    (try
      (let [result (apply shell/sh "js-beautify" options-args)]
        (if-not (empty? (:err result))
          (clansi/style (str "! " (:err result)) :red)
          (cuerdas/rtrim (:out result))))
      (catch Throwable e
        (clansi/style (str "! " (.getMessage e)) :red)))))

(defn dim-text [text]
  (clansi/style text :black))                                                                                                 ; black on black background should be displayed as gray

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
  (let [start-separator (tools/get-arena-start-separator)
        end-separator (tools/get-arena-end-separator)]
    (if-some [start-separator-index (string/last-index-of content start-separator)]
      (let [semicolon-index (or (string/index-of content ";" start-separator-index) start-separator-index)
            content (.substring content (inc semicolon-index))]
        (if-some [end-separator-index (string/index-of content end-separator)]
          (let [content (.substring content 0 end-separator-index)
                semicolon-index (or (string/last-index-of content "console") 0)]                                              ; see gen-marker
            (.substring content 0 semicolon-index)))))))

(defn make-empty-normalizer-state []
  {:counter     1
   :counters    {}
   :identifiers {}
   :mappings    {}})

(defn get-counter [normalizer-state]
  (:counter normalizer-state))

(defn get-mapping [normalizer-state name]
  (get-in normalizer-state [:mappings name]))

(defn get-counter-for-name [normalizer-state name]
  (get-in normalizer-state [:counters name]))

(defn register-mapping [normalizer-state name replacement]
  (-> normalizer-state
      (update :counter inc)
      (update :mappings assoc name replacement)))

(defn register-counter [normalizer-state name identifier]
  (let [x (fn [state]
            (let [assigned-number (get-in state [:identifiers identifier])]
              (assoc-in state [:counters name] assigned-number)))]
    (-> normalizer-state
        (update-in [:identifiers identifier] (fn [c] (inc (or c 0))))
        (x))))

(defn register-mapping-if-needed [normalizer-state name replacement]
  (if (get-mapping normalizer-state name)
    normalizer-state
    (register-mapping normalizer-state name replacement)))

(defn register-counter-if-needed [normalizer-state name identifier]
  (if (get-counter-for-name normalizer-state name)
    normalizer-state
    (register-counter normalizer-state name identifier)))

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

(defn normalize-inlines [[content normalizer-state]]
  "The goal here is to rename all generated <NUM>$$inline_<NUM> identifiers with stable numbering."
  (let [* (fn [[content normalizer-state] needle]
            (let [replacement (str (get-counter normalizer-state))
                  new-normalizer-state (register-mapping-if-needed normalizer-state needle replacement)
                  new-content (string/replace content needle (get-mapping new-normalizer-state needle))]
              [new-content new-normalizer-state]))]
    (reduce * [content normalizer-state] (re-seq #"\d+\$\$inline_\d+" content))))

(defn linearize-numbering [[content normalizer-state]]
  "The goal here is to rename all generated <IDENTIFIER>_<NUM> to linear numbering for each distinct identifier."
  (let [names (distinct (re-seq #"[a-zA-Z0-9_$]+_\d+" content))
        * (fn [[content normalizer-state] name]
            (let [group (re-matches #"([a-zA-Z0-9_$]+_)(\d+)" name)
                  identifier (nth group 1)
                  new-normalizer-state (register-counter-if-needed normalizer-state name identifier)
                  massaged-name (str identifier "$$$" (get-counter-for-name new-normalizer-state name))                       ; $$$ is a marker so we don't conflict with name candidates for future replacement
                  pattern (re-pattern (str (Pattern/quote name) "([^0-9]?)"))                                                 ; we want to prevent replacing partial matches, eg. id_1 to mess with id_100
                  replacement (str (Matcher/quoteReplacement massaged-name) "$1")
                  new-content (string/replace content pattern replacement)]
              [new-content new-normalizer-state]))
        + (fn [[content normalizer-state] name]
            (let [group (re-matches #"([a-zA-Z0-9_$]+_)(\d+)" name)
                  identifier (nth group 1)
                  new-content (string/replace content (str identifier "$$$") identifier)]
              [new-content normalizer-state]))]
    (reduce + (reduce * [content normalizer-state] names) names)))

(defn drop-multi-dollars [[content normalizer-state]]
  [(string/replace content #"\$\$+" "") normalizer-state])

(defn drop-leading-dollars [[content normalizer-state]]
  [(string/replace content #"([^0-9a-zA-Z_])\$" "$1") normalizer-state])

(defn humanize-oops-ns [[content normalizer-state]]
  [(string/replace content #"oops\$(.*?)\$" "oops.$1.") normalizer-state])

(defn humanize-cljs-core-ns [[content normalizer-state]]
  [(string/replace content #"cljs\$core\$" "cljs.core.") normalizer-state])

(defn humanize-goog-ns [[content normalizer-state]]
  [(string/replace content #"goog\$(.*?)\$" "goog.$1.") normalizer-state])

(defn replace-jscomp [[content normalizer-state]]
  [(string/replace content #"\$jscomp\$" (string/re-quote-replacement "$$")) normalizer-state])

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
                                               (replace-jscomp)
                                               (normalize-identifiers)
                                               (normalize-gensyms)
                                               (normalize-twins)
                                               (normalize-inlines)
                                               (linearize-numbering)
                                               (drop-multi-dollars)
                                               (drop-leading-dollars)
                                               (humanize-oops-ns)
                                               (humanize-goog-ns)
                                               (humanize-cljs-core-ns))]
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

(defn print-throwable-without-ex-data [tr]
  (printf "%s: %s" (.getName (class tr)) (.getMessage tr)))

(defn print-cause-chain [tr]
  (print-throwable-without-ex-data tr)
  (println)
  (when-let [cause (.getCause tr)]
    (print "Caused by: ")
    (recur cause)))

(defn replace-absolute-paths [text]
  (-> text
      (string/replace #"(\s|:)/.*?/test/src/" "$1<absolute-path>/test/src/")
      (string/replace #"(\s|:)/.*?/src/lib/" "$1<absolute-path>/src/lib/")))

(defn post-process-compiler-output [text]
  (-> text
      (replace-absolute-paths)))
