(ns oops.tools
  (:require [environ.core :refer [env]]
            [oops.config :as config]))

;(println (interpose "\n" (seq (.getURLs (ClassLoader/getSystemClassLoader)))))

(defmacro with-console-recording [recorder & body]
  `(let [recorder# ~recorder]
     (try
       (add-console-recorder! recorder#)
       ~@body
       (finally
         (remove-console-recorder! recorder#)))))

(defn advanced-mode? []
  (if cljs.env/*compiler*
    (= (get-in @cljs.env/*compiler* [:options :optimizations]) :advanced)))

(defmacro when-advanced-mode [& body]
  (if (advanced-mode?)
    `(do ~@body)))

(defmacro when-none-mode [& body]
  (if-not (advanced-mode?)
    `(do ~@body)))

(defn gen-when-compiler-config [pred config-template body]
  (let [config (config/get-current-compiler-config)
        template-keys (keys config-template)]
    (if (pred config-template (select-keys config template-keys))
      `(do ~@body))))

(defmacro when-compiler-config [config-template & body]
  (gen-when-compiler-config = config-template body))

(defmacro when-not-compiler-config [config-template & body]
  (gen-when-compiler-config not= config-template body))

(defn get-arena-separator []
  "###---> compiled main namespace starts here <---###")

(defmacro emit-arena-separator! []
  (let [comment (str " @preserve " (get-arena-separator) "")]
    `(~'js-inline-comment ~comment)))

(defn gen-devtools-if-needed []
  (if-not (= (env :oops-elide-devtools) "1")
    `(if-not (re-find #"PhantomJS" js/window.navigator.userAgent)
       (devtools.core/install!))))

(defmacro init-test! []
  `(do
     ~(gen-devtools-if-needed)
     (emit-arena-separator!)))

(defmacro runonce [& body]
  (let [code (cons 'do body)
        code-string (pr-str code)
        code-hash (hash code-string)
        name (symbol (str "runonce_" code-hash))]
    `(defonce ~name {:value ~code
                     :code  ~code-string})))
