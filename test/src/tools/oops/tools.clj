(ns oops.tools)

(defmacro with-captured-console [recorder & body]
  `(try
     (let [recorder# ~recorder]
       (add-console-recorder! recorder#)
       ~@body
       (finally
         (remove-console-recorder! recorder#)))))

(defn advanced-mode? []
  (if cljs.env/*compiler*
    (= (get-in @cljs.env/*compiler* [:options :optimizations]) :advanced)))

(defmacro under-advanced-mode? []
  (boolean (advanced-mode?)))
