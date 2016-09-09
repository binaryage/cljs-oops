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

(defn normalize-identifiers [[content starting-counter]]
  "The goal here is to rename all generated $<number>$ identifiers with stable numbering."
  (let [* (fn [[content counter] match]
            (let [needle (first match)
                  replacement (str counter (nth match 2))]
              [(string/replace content needle replacement) (inc counter)]))]
    (reduce * [content starting-counter] (re-seq #"(\d+)(\$|__)" content))))

(defn normalize-gensyms [[content starting-counter]]
  "The goal here is to rename all generated name<NUM> identifiers with stable numbering."
  (let [* (fn [[content counter] match]
            (if (> (Long/parseLong (first match)) 1000)
              (let [needle (first match)
                    stable-replacement (str counter)]
                [(string/replace content needle stable-replacement) (inc counter)])
              [content counter]))]
    (reduce * [content starting-counter] (re-seq #"(\d+)" content))))

(defn normalize-twins [[content starting-counter]]
  (let [counter (volatile! starting-counter)
        * (fn [_match]
            (vswap! counter inc)
            (str @counter))]
    [(string/replace content #"(\d+)_(\d+)" *) @counter]))

(defn safe-spit [path content]
  (io/make-parents path)
  (spit path content))

(defn pprint-str [v]
  (with-out-str
    (binding [*print-level* 10
              *print-length* 10]
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
        [stabilized-code] (-> [unwrapped-code 1]
                              (normalize-identifiers)
                              (normalize-gensyms)
                              (normalize-twins))]
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
