(ns oops.runner
  (:require-macros [oops.runner :refer [ansi-enabled?]])
  (:require [cljs.test :as test :refer-macros [run-tests] :refer [report]]
            [oops.main]))

; taken from https://github.com/pjlegato/clansi/blob/7c9a525f5a72d928031573586cbce9a5f5699e15/src/clansi/core.clj
(def ANSI-CODES
  {:reset             "[0m"
   :bright            "[1m"
   :blink-slow        "[5m"
   :underline         "[4m"
   :underline-off     "[24m"
   :inverse           "[7m"
   :inverse-off       "[27m"
   :strikethrough     "[9m"
   :strikethrough-off "[29m"

   :default           "[39m"
   :white             "[37m"
   :black             "[30m"
   :red               "[31m"
   :green             "[32m"
   :blue              "[34m"
   :yellow            "[33m"
   :magenta           "[35m"
   :cyan              "[36m"

   :bg-default        "[49m"
   :bg-white          "[47m"
   :bg-black          "[40m"
   :bg-red            "[41m"
   :bg-green          "[42m"
   :bg-blue           "[44m"
   :bg-yellow         "[43m"
   :bg-magenta        "[45m"
   :bg-cyan           "[46m"
   })


(def ^:dynamic *use-ansi* "Rebind this to false if you don't want to see ANSI codes in some part of your code." (ansi-enabled?))

(defn ansi
  "Output an ANSI escape code using a style key.
   (ansi :blue)
   (ansi :underline)
  Note, try (style-test-page) to see all available styles.
  If *use-ansi* is bound to false, outputs an empty string instead of an
  ANSI code. You can use this to temporarily or permanently turn off
  ANSI color in some part of your program, while maintaining only 1
  version of your marked-up text.
  "
  [code]
  (if *use-ansi*
    (str \u001b (get ANSI-CODES code (:reset ANSI-CODES)))
    ""))

(defmethod report [::test/default :summary] [m]
  (println "\nRan" (:test m) "tests containing"
           (+ (:pass m) (:fail m) (:error m)) "assertions.")
  (println (:fail m) "failures," (:error m) "errors.")
  (let [failures-count (+ (:fail m) (:error m))]
    (.log js/console (str "TESTS DONE (" failures-count ")"))))

(defmethod report [::test/default :fail] [m]
  (test/inc-report-counter! :fail)
  (println (ansi :red) "\nFAIL in" (test/testing-vars-str m) (ansi :reset))
  (when (seq (:testing-contexts (test/get-current-env)))
    (println (ansi :blue) (test/testing-contexts-str) (ansi :reset)))
  (when-let [message (:message m)] (println (ansi :magenta) message (ansi :reset)))
  (println "expected:" (ansi :green) (pr-str (:expected m)) (ansi :reset))
  (println "  actual:" (ansi :yellow) (pr-str (:actual m)) (ansi :reset)))

(defn run-normal-tests []
  (test/run-tests
    (cljs.test/empty-env ::test/default)
    'oops.main))

; -- entry point ------------------------------------------------------------------------------------------------------------

(enable-console-print!)
(println "ClojureScript version:" *clojurescript-version*)
(case (.-selectedTestSuite js/window)
  (run-normal-tests))
