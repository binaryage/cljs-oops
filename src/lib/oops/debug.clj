(ns oops.debug
  (:require [clojure.pprint]))

(defn pprint [v]
  (with-out-str
    (clojure.pprint/pprint v)))

(defn log [& args]
  (let [msg (apply str (interpose " " (map pprint args)))]
    (.print System/out (str msg "\n"))))
