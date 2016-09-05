(ns oops.main
  (:require [cljs.test :refer-macros [deftest testing is are run-tests use-fixtures]]
            [oops.core :refer [oget oset! ocall! oapply! ocall oapply]]
            [oops.config :refer [with-runtime-config]]
            [oops.tools :refer [with-captured-console with-console-recording]]
            [clojure.string :as string]))

(use-fixtures :once with-captured-console)

(def expected-warnings
  "WARN: (\"Unexpected object value (nil)\" nil)
WARN: (\"Unexpected object value (undefined)\" nil)
WARN: (\"Unexpected object value (string)\" \"s\")
WARN: (\"Unexpected object value (number)\" 42)
WARN: (\"Unexpected object value (boolean)\" true)
WARN: (\"Unexpected object value (boolean)\" false)")

(deftest test-warnings
  (let [sample-obj #js {:key               "val"
                        "@#$%fancy key^&*" "fancy-val"
                        "nested"           #js {:nested-key1  "nk1"
                                                "nested-key2" 2}}]
    (testing "object access validation should output warnigns to console"
      ; root level
      (let [recorder (atom [])]
        (with-console-recording recorder
                                (are [o] (= (oget o "key") nil)
                                  nil
                                  js/undefined
                                  "s"
                                  42
                                  true
                                  false))
        (is (= (string/join "\n" @recorder) expected-warnings))))))
