(ns oops.debug)

(defn log [& args]
  (let [msg (apply str (interpose " " args))]
    (.print System/out (str msg "\n"))))
