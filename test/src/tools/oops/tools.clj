(ns oops.tools)

(defmacro with-captured-console [recorder & body]
  `(try
     (let [recorder# ~recorder]
       (add-console-recorder! recorder#)
       ~@body
       (finally
         (remove-console-recorder! recorder#)))))
