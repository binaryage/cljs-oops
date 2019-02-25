; License: BSD 2-Clause
; Copyright (c) 2014-2015 Andrey Antukh <niwi@niwi.nz>
; https://github.com/funcool/cuerdas
;
(ns oops.cuerdas
  "This is our stripped down version of the cuerdas library to avoid bringing in an extra dependency."
  (:refer-clojure :exclude [repeat regexp?])
  (:require [clojure.string :as str])
  (:import (java.util.regex Pattern)))

(defn escape
  "Escapes characters in the string that are not safe
   to use in a RegExp."
  [s]
  (Pattern/quote ^String s))

(defn regexp?
  "Return `true` if `x` is a regexp pattern
  instance."
  [x]
  (instance? Pattern x))

(defn join
  "Joins strings together with given separator."
  ([coll]
   (apply str coll))
  ([separator coll]
   (apply str (interpose separator coll))))

(defn repeat
  "Repeats string n times."
  ([s] (repeat s 1))
  ([s n]
   (when (string? s)
     (join (clojure.core/repeat n s)))))

(defn split
  "Splits a string on a separator a limited
  number of times. The separator can be a string,
  character or Pattern (clj) / RegExp (cljs) instance."
  ([s] (split s #"\s+"))
  ([s ^Object sep]
   (cond
     (nil? s) s
     (regexp? sep) (str/split s sep)
     (string? sep) (str/split s (re-pattern (escape sep)))
     (char? sep) (str/split s (re-pattern (escape (.toString sep))))
     :else (throw (ex-info "Invalid arguments" {:sep sep}))))
  ([s ^Object sep num]
   (cond
     (nil? s) s
     (regexp? sep) (str/split s sep num)
     (string? sep) (str/split s (re-pattern (escape sep)) num)
     (char? sep) (str/split s (re-pattern (escape (.toString sep))) num)
     :else (throw (ex-info "Invalid arguments" {:sep sep})))))

(defn lines
  "Return a list of the lines in the string."
  [s]
  (split s #"\n|\r\n"))

(defn unlines
  "Returns a new string joining a list of strings with a newline char (\\n)."
  [s]
  (when (sequential? s)
    (str/join "\n" s)))