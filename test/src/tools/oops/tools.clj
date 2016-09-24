(ns oops.tools
  (:require [environ.core :refer [env]]
            [clojure.pprint :refer [pprint]]
            [oops.config :as config]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [cuerdas.core :as cuerdas])
  (:import (java.io StringWriter)))

(defn pprint-code [v]
  (with-out-str
    (binding [clojure.pprint/*print-right-margin* 200
              *print-level* 5
              *print-length* 10]
      (pprint v))))

(defn get-classpath []
  (apply str (interpose "\n" (seq (.getURLs (ClassLoader/getSystemClassLoader))))))

(defn advanced-mode? []
  (if cljs.env/*compiler*
    (= (get-in @cljs.env/*compiler* [:options :optimizations]) :advanced)))

(defn gen-when-compiler-config [pred config-template body]
  (let [config (config/get-current-compiler-config)
        template-keys (keys config-template)]
    (if (pred config-template (select-keys config template-keys))
      `(do ~@body))))

(defn gen-preserved-comment [comment]
  (let [preserved-comment (str "@preserve " comment)]
    `(cljs.core/js-inline-comment ~preserved-comment)))

(defn gen-marker [s]
  `(.log js/console ~(str "-12345-" s "-54321-")))

(defn get-arena-separator []
  "--- compiled main namespace starts here ---")

(defn gen-arena-separator []
  (gen-marker (get-arena-separator)))

(defn gen-devtools-if-needed []
  (if-not (= (env :oops-elide-devtools) "1")
    `(under-chrome
       (devtools.core/install!))))

; http://stackoverflow.com/a/15627016/84283
(defn hexify "Convert byte sequence to hex string" [coll]
  (let [hex [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f]]
    (letfn [(hexify-byte [b]
              (let [v (bit-and b 0xFF)]
                [(hex (bit-shift-right v 4)) (hex (bit-and v 0x0F))]))]
      (apply str (mapcat hexify-byte coll)))))

(defn hexify-str [s]
  (hexify (.getBytes s)))

(defn unhexify "Convert hex string to byte sequence" [s]
  (letfn [(unhexify-2 [c1 c2]
            (unchecked-byte
              (+ (bit-shift-left (Character/digit c1 16) 4)
                 (Character/digit c2 16))))]
    (map #(apply unhexify-2 %) (partition 2 s))))

(defn unhexify-str [s]
  (apply str (map char (unhexify s))))

(defn encode [s]
  (hexify-str s))

(defn decode [s]
  (unhexify-str s))

(defn fetch-arena-source [ns-name]
  (let [file-name (str (namespace-munge (last (string/split ns-name #"\."))) ".cljs")
        file (io/file "test/src/arena/oops/arena" file-name)]
    (slurp file)))

(defn extract-code-snippet [source start-line]
  (->> (cuerdas/lines source)
       (drop (dec start-line))
       (take-while #(not (empty? %)))
       (cuerdas/unlines)))

(defn extrac-code-snippet-from-env [env]
  (let [ns-name (str (get-in env [:ns :name]))
        _ (assert ns-name)
        line (get env :line)
        _ (assert line)
        source (fetch-arena-source ns-name)]
    (extract-code-snippet source line)))

; -- macros -----------------------------------------------------------------------------------------------------------------

(defmacro with-console-recording [recorder & body]
  `(let [recorder# ~recorder]
     (try
       (add-console-recorder! recorder#)
       ~@body
       (finally
         (remove-console-recorder! recorder#)))))

(defmacro when-advanced-mode [& body]
  (if (advanced-mode?)
    `(do ~@body)))

(defmacro when-not-advanced-mode [& body]
  (if-not (advanced-mode?)
    `(do ~@body)))

(defmacro if-advanced-mode [advanced-body else-body]
  (if (advanced-mode?)
    advanced-body
    else-body))

(defmacro when-compiler-config [config-template & body]
  (gen-when-compiler-config = config-template body))

(defmacro when-not-compiler-config [config-template & body]
  (gen-when-compiler-config not= config-template body))

(defmacro under-phantom [& body]
  `(when (re-find #"PhantomJS" js/window.navigator.userAgent)
     ~@body))

(defmacro under-chrome [& body]
  `(when-not (re-find #"PhantomJS" js/window.navigator.userAgent)
     ~@body))

(defmacro if-phantom [phantom-code & [chrome-code]]
  `(if (re-find #"PhantomJS" js/window.navigator.userAgent)
     ~phantom-code
     ~chrome-code))

(defmacro init-test! []
  `(do
     ~(gen-devtools-if-needed)))

(defmacro init-arena-test! []
  `(do
     ~(gen-devtools-if-needed)
     ~(gen-arena-separator)))

(defmacro runonce [& body]
  (let [code (cons 'do body)
        code-string (pr-str code)
        code-hash (hash code-string)
        name (symbol (str "runonce_" code-hash))]
    `(defonce ~name {:value ~code
                     :code  ~code-string})))

(defmacro testing [_title & body]
  (let [code (extrac-code-snippet-from-env &env)
        snippet-str (str "SNIPPET:" (encode code))]
    `(do
       ~(gen-marker snippet-str)
       ~@body)))

(defmacro presume-compiler-config [config]
  (let [compiler-config (select-keys (config/get-compiler-config) (keys config))]
    `(cljs.test/is (= ~compiler-config ~config))))

(defmacro macro-identity [x]
  x)

(def err-recorder (volatile! nil))
(def prev-err (volatile! nil))

(defmacro start-err-recorder! []
  (assert (nil? @err-recorder))
  (vreset! err-recorder (new StringWriter))
  (vreset! prev-err *err*)
  (alter-var-root #'*err* (fn [_] @err-recorder))
  nil)

(defmacro stop-err-recorder! []
  (assert (some? @err-recorder))
  (let [recording (str @err-recorder)]
    (alter-var-root #'*err* (fn [_] @prev-err))
    (vreset! err-recorder nil)
    (vreset! prev-err nil)
    recording))

(defmacro with-stderr-recording [recorder & body]
  ; note: we rely on macroexpander behaviour here
  `(do
     (oops.tools/start-err-recorder!)
     (let [res# (do ~@body)
           recording# (oops.tools/stop-err-recorder!)]
       (cljs.core/swap! ~recorder cljs.core/concat (remove empty? (cuerdas.core/lines recording#)))
       res#)))
