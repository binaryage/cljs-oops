(ns oops.main
  (:require [cljs.test :refer-macros [deftest testing is are run-tests use-fixtures]]
            [cuerdas.core]
            [oops.core :refer [oget oset! ocall! oapply! ocall oapply
                               oget+ oset!+ ocall!+ oapply!+ ocall+ oapply+
                               gget gset! gcall! gapply! gcall gapply
                               gget+ gset!+ gcall!+ gapply!+ gcall+ gapply+]]
            [oops.config :refer [with-runtime-config with-compiler-config with-child-factory update-current-runtime-config!]]
            [oops.tools
             :refer [with-captured-console presume-runtime-config]
             :refer-macros [init-test!
                            presume-compiler-config
                            runonce
                            when-advanced-mode when-not-advanced-mode if-advanced-mode
                            under-phantom
                            under-chrome
                            if-phantom
                            with-console-recording
                            with-stderr-recording
                            when-compiler-config when-not-compiler-config
                            macro-identity]]
            [clojure.string :as string]))

(runonce
  (init-test!))

(use-fixtures :once with-captured-console)

(update-current-runtime-config! {:use-envelope false})

(deftest test-oget
  (let [sample-obj #js {:key               "val"
                        "@#$%fancy key^&*" "fancy-val"
                        "nested"           #js {:nested-key1  "nk1"
                                                "nested-key2" 2}}]
    (testing "simple static get"
      (with-runtime-config {:missing-object-key false}
        (are [key expected] (= (oget sample-obj key) expected)
          "non-existent" nil
          "key" "val"
          "@#$%fancy key^&*" "fancy-val"
          (macro-identity "key") "val"
          (macro-identity :key) "val"
          (macro-identity ["nested" (macro-identity (macro-identity "nested-key1"))]) "nk1"
          ["nested" "nested-key2"] 2)))
    (testing "simple dynamic get"
      (with-runtime-config {:missing-object-key false}
        (are [dyn-selector expected] (= (oget+ sample-obj dyn-selector) expected)
          (identity "key") "val"
          (identity "xxx") nil
          (list (identity "nested") "nested-key1") "nk1"
          (identity ["nested" "nested-key1"]) "nk1"
          [(identity "nested") "nested-key1"] "nk1")))
    (testing "static soft get"
      (are [selector expected] (= (oget sample-obj selector) expected)
        ".?key" "val"
        ".?x.?y" nil
        "?a" nil
        "?nested.nested-key1" "nk1"
        "?nested.?missing.?xxx" nil))
    (testing "dynamic soft get"
      (are [selector expected] (= (oget+ sample-obj selector) expected)
        (identity ".?key") "val"
        (identity ".?x.?y") nil
        (identity "?a") nil
        (identity "?nested.nested-key1") "nk1"
        (identity "?nested.?missing.?xxx") nil))
    (when-not-advanced-mode
      (testing "invalid selectors"
        (are [input] (thrown-with-msg? js/Error #"Invalid selector" (oget+ sample-obj (identity input)))
          'sym
          identity
          0
          #js {})))
    (when-not-advanced-mode
      (testing "dynamic get via js array"
        (is (= (oget+ sample-obj (identity #js ["nested" "nested-key1"])) "nk1"))
        (is (= (oget+ sample-obj (identity #js ["nested" :nested-key1])) "nk1"))))
    (when-not-advanced-mode
      (testing "object access validation should throw by default"
        (are [o msg] (thrown-with-msg? js/Error msg (oget o "key"))
          nil #"Unexpected object value \(nil\)"
          js/undefined #"Unexpected object value \(undefined\)"
          "s" #"Unexpected object value \(string\)"
          42 #"Unexpected object value \(number\)"
          true #"Unexpected object value \(boolean\)"
          false #"Unexpected object value \(boolean\)"
          #inst "2000" #"Unexpected object value \(date-like\)"
          StringBufferWriter #"Unexpected object value \(cljs type\)"
          [] #"Unexpected object value \(cljs instance\)"
          :keyword #"Unexpected object value \(cljs instance\)"
          (atom "X") #"Unexpected object value \(cljs instance\)")
        (are [o msg] (thrown-with-msg? js/Error msg (oget (js-obj "k1" (js-obj "k2" o)) "k1" "k2" "k3"))
          nil #"Unexpected object value \(nil\) on key path 'k1.k2'")
        (under-chrome
          (are [o msg] (thrown-with-msg? js/Error msg (oget o "key"))
            ; js/Symbol is not available under phantom and we cannot really test it even under Chrome due to CLJS-1631
            ; TODO: uncomment this later
            ; (js/Symbol "mysymbol") #"Unexpected object value \(non-object\)"
            nil #"Unexpected object value \(nil\)"))))
    (when-not-advanced-mode
      (testing "with {:error-reporting-mode false} object access validation should be elided"
        (with-runtime-config {:error-reporting false}
          (are [o] (= (oget o "key") nil)
            nil
            js/undefined
            "s"
            42
            true
            false))))
    (when-not-advanced-mode
      (testing "with {:error-reporting-mode :console} object access validation should report errors to console"
        (with-runtime-config {:error-reporting :console}
          (let [recorder (atom [])]
            (with-console-recording recorder
              (are [o] (= (oget o "key") nil)
                nil
                js/undefined
                "s"
                42
                true
                false))
            (is (= @recorder ["ERROR: (\"Oops, Unexpected object value (nil)\" {:path \"\", :flavor \"nil\", :obj nil})"
                              "ERROR: (\"Oops, Unexpected object value (undefined)\" {:path \"\", :flavor \"undefined\", :obj nil})"
                              "ERROR: (\"Oops, Unexpected object value (string)\" {:path \"\", :flavor \"string\", :obj \"s\"})"
                              "ERROR: (\"Oops, Unexpected object value (number)\" {:path \"\", :flavor \"number\", :obj 42})"
                              "ERROR: (\"Oops, Unexpected object value (boolean)\" {:path \"\", :flavor \"boolean\", :obj true})"
                              "ERROR: (\"Oops, Unexpected object value (boolean)\" {:path \"\", :flavor \"boolean\", :obj false})"])))
          (let [recorder (atom [])]
            (with-console-recording recorder
              (are [o] (= (oget (js-obj "k1" o) "k1" "k2") nil)
                nil))
            (is (= @recorder ["ERROR: (\"Oops, Unexpected object value (nil) on key path 'k1'\" {:path \"k1\", :flavor \"nil\", :obj #js {:k1 nil}})"])))
          ; make sure we don't print multiple errors on subsequent missing keys...
          (let [recorder (atom [])]
            (with-console-recording recorder
              (is (= (oget #js {:k1 #js {"k2" nil}} "k1" "k2" "k3" "k4") nil)))
            (is (= @recorder ["ERROR: (\"Oops, Unexpected object value (nil) on key path 'k1.k2'\" {:path \"k1.k2\", :flavor \"nil\", :obj #js {:k1 #js {:k2 nil}}})"]))))))
    (when-advanced-mode                                                                                                       ; advanced optimizations
      (testing "object access validation should crash or silently fail in advanced mode (no diagnostics)"
        (when-not-compiler-config {:key-get :goog}
          (under-phantom
            (are [o msg] (thrown-with-msg? js/TypeError msg (.log js/console (oget o "key")))                                 ; we have to log it otherwise closure could remove it as dead code
              nil #"null is not an object"
              js/undefined #"undefined is not an object"))
          (under-chrome
            (are [o msg] (thrown-with-msg? js/TypeError msg (.log js/console (oget o "key")))                                 ; we have to log it otherwise closure could remove it as dead code
              nil #"Cannot read property 'key' of null"
              js/undefined #"Cannot read property 'key' of undefined"))
          (are [o] (= (oget o "key") nil)
            "s"
            42
            true
            false))))
    (testing "static dot escaping"
      (let [o #js {".."     #js {".x." "."}
                   ".\\\\." "x"                                                                                               ; this is a cljs bug, it does java string escaping and then again when emitting javascript string
                   "prop.1" #js {".k2" "v2"
                                 "k3." #js {:some "val"}}}]
        (are [key expected] (= (oget o key) expected)
          "prop\\.1.\\.k2" "v2"
          "prop\\.1.k3\\..some" "val"
          "\\.\\..\\.x\\." "."
          "\\.\\\\." "x")))
    (testing "dynamic dot escaping"
      (let [o #js {".."     #js {".x." "."}
                   ".\\\\." "x"                                                                                               ; this is a cljs bug, it does java string escaping and then again when emitting javascript string
                   "prop.1" #js {".k2" "v2"
                                 "k3." #js {:some "val"}}}]
        (are [key expected] (= (oget+ o (identity key)) expected)
          "prop\\.1.\\.k2" "v2"
          "prop\\.1.k3\\..some" "val"
          "\\.\\..\\.x\\." "."
          "\\.\\\\." "x")))
    (testing "static modifiers escaping"
      (let [o #js {"?key"   "v"
                   "nested" #js {"!k2" "v2"}}]
        (are [key expected] (= (oget o key) expected)
          "\\?key" "v"
          ["nested" "\\!k2"] "v2"
          "nested.\\!k2" "v2"
          ["nested" ".\\!k2"] "v2")))
    (testing "dynamic modifiers escaping"
      (let [o #js {"?key"   "v"
                   "nested" #js {"!k2" "v2"}}]
        (are [key expected] (= (oget o (identity key)) expected)
          "\\?key" "v"
          ["nested" "\\!k2"] "v2"
          "nested.\\!k2" "v2"
          ["nested" ".\\!k2"] "v2")))
    (when-not-advanced-mode
      (testing "warning when accessing missing key"
        (presume-runtime-config {:warning-reporting :console})
        (with-runtime-config {:missing-object-key :warn}
          (let [recorder (atom [])]
            (with-console-recording recorder
              (let [o (js-obj "k1" (js-obj "k2" (js-obj)))]
                (oget o "k1.k2.k3")
                (oget+ o (identity "k1.k2.k3"))
                (oget o "kx")
                (oget o "k1" "kx")))
            (is (= @recorder ["WARN: (\"Oops, Missing expected object key 'k3' on key path 'k1.k2.k3'\" {:path \"k1.k2.k3\", :key \"k3\", :obj #js {:k1 #js {:k2 #js {}}}})"
                              "WARN: (\"Oops, Missing expected object key 'k3' on key path 'k1.k2.k3'\" {:path \"k1.k2.k3\", :key \"k3\", :obj #js {:k1 #js {:k2 #js {}}}})"
                              "WARN: (\"Oops, Missing expected object key 'kx'\" {:path \"kx\", :key \"kx\", :obj #js {:k1 #js {:k2 #js {}}}})"
                              "WARN: (\"Oops, Missing expected object key 'kx' on key path 'k1.kx'\" {:path \"k1.kx\", :key \"kx\", :obj #js {:k1 #js {:k2 #js {}}}})"]))))))
    (when-not-advanced-mode
      (testing "accessing missing key with {:missing-object-key :error} "
        (presume-runtime-config {:error-reporting    :throw
                                 :missing-object-key :error})
        (let [o (js-obj "k1" (js-obj "k2" (js-obj)))]
          (are [sel err] (thrown-with-msg? js/Error err (oget+ o sel))
            "k1.k2.k3" #"Missing expected object key 'k3' on key path 'k1.k2.k3'"
            (identity "k1.k2.k3") #"Missing expected object key 'k3' on key path 'k1.k2.k3'"
            "kx" #"Missing expected object key 'kx'"
            ["k1" "kx"] #"Missing expected object key 'kx' on key path 'k1.kx'"))))
    (testing "oget corner cases"
      ; TODO
      )))

(deftest test-oset
  (testing "static set"
    (let [sample-obj #js {"nested" #js {}}]
      (are [s1 s2] (= (oget (oset! sample-obj s1 "val") s2) "val")
        "!xxx" "xxx"
        ["!yyy"] "yyy"
        ["nested" "!y"] "nested.y")
      (is (= (js/JSON.stringify sample-obj) "{\"nested\":{\"y\":\"val\"},\"xxx\":\"val\",\"yyy\":\"val\"}"))))
  (testing "dynamic selector set"
    (let [sample-obj #js {"nested" #js {}}
          dynamic-key-fn (fn [name] name)]
      (are [s1 s2] (= (oget+ (oset!+ sample-obj s1 "val") s2) "val")
        (dynamic-key-fn "!key") "key"
        [(dynamic-key-fn "!nested") (dynamic-key-fn "!key2")] "nested.key2")
      (is (= (js/JSON.stringify sample-obj) "{\"nested\":{\"key2\":\"val\"},\"key\":\"val\"}"))))
  (testing "static punching set!"
    (let [sample-obj #js {"nested" #js {}}]
      (are [s1 s2] (= (oget+ (oset! sample-obj s1 "val") s2) "val")
        ".!nested.!xxx" "nested.xxx"
        "!aaa" "aaa"
        ["!" "x"] "x"                                                                                                         ; standalone modifier
        ["!" (macro-identity "y")] "y"                                                                                        ; standalone modifier + macro expansion
        ["!" ["?" "z"]] "z"                                                                                                   ; ! overrides ?
        ["!z1" "!z2" "!z3"] "z1.z2.z3")
      (is (= (js/JSON.stringify sample-obj) "{\"nested\":{\"xxx\":\"val\"},\"aaa\":\"val\",\"x\":\"val\",\"y\":\"val\",\"z\":\"val\",\"z1\":{\"z2\":{\"z3\":\"val\"}}}"))))
  (testing "dynamic punching set!"
    (let [sample-obj #js {"nested" #js {}}]
      (are [s1 s2] (= (oget+ (oset!+ sample-obj (identity s1) "val") s2) "val")
        ".!nested.!xxx" "nested.xxx"
        "!aaa" "aaa"
        ["!" "x"] "x"                                                                                                         ; standalone modifier
        ["!" (macro-identity "y")] "y"                                                                                        ; standalone modifier + macro expansion
        ["!" ["?" "z"]] "z"                                                                                                   ; ! overrides ?
        ["!z1" "!z2" "!z3"] "z1.z2.z3")
      (is (= (js/JSON.stringify sample-obj) "{\"nested\":{\"xxx\":\"val\"},\"aaa\":\"val\",\"x\":\"val\",\"y\":\"val\",\"z\":\"val\",\"z1\":{\"z2\":{\"z3\":\"val\"}}}"))))
  (testing "punching set! with custom child-factory"
    (let [sample-obj #js {"nested" #js {}}
          counter (volatile! nil)]
      (with-child-factory (fn [_obj key]
                            (vreset! counter (str (if @counter (str @counter ",")) key))
                            (js-obj))
        (are [s1 s2] (= (oget+ (oset!+ sample-obj s1 "val") s2) "val")
          ".!nested.!xxx" "nested.xxx"
          "!aaa" "aaa"
          ["!z1" "!z2" "!z3"] "z1.z2.z3")
        (is (= @counter "z1,z2")))))                                                                                          ; only z1 and z2 are punched
  (testing "punching set! with :js-array child-factory"
    (let [sample-obj #js {"nested" #js {}}]
      (with-child-factory :js-array
        (are [s1 s2] (= (oget+ (oset!+ sample-obj s1 "val") s2) "val")
          ".!nested.!xxx.!1" "nested.xxx.1"
          "!aaa" "aaa"
          ["!z1" "!0" "!3"] "z1.0.3")
        (is (= (js/JSON.stringify sample-obj) "{\"nested\":{\"xxx\":[null,\"val\"]},\"aaa\":\"val\",\"z1\":[[null,null,null,\"val\"]]}")))))
  (testing "flexible selector in oset!"
    (let [sample-obj #js {"n1" #js {"n2" #js {}}}]
      (is (= (oget (oset! sample-obj "n1" "n2" "val") "n1" "n2") "val"))
      (is (= (oget (oset! sample-obj ["n1" "n2"] "val") "n1" "n2") "val"))
      #_(is (= (oget (oset! sample-obj #js ["n1" "n2"] "val") "n1" "n2") "val"))))
  (testing "non-strict punching"
    (presume-compiler-config {:strict-punching true})
    (with-compiler-config {:strict-punching false}
      (let [sample-obj #js {"nested" #js {}}]
        (are [s1 s2] (= (oget+ (oset!+ sample-obj s1 "val") s2) "val")
          ".!nested.xxx" "nested.xxx"
          "aaa" "aaa"
          ["!z1" "!z2" "z3"] "z1.z2.z3")
        (is (= (js/JSON.stringify sample-obj) "{\"nested\":{\"xxx\":\"val\"},\"aaa\":\"val\",\"z1\":{\"z2\":{\"z3\":\"val\"}}}")))))
  (testing "oset corner cases"
    ; TODO
    ))

(deftest test-ocall
  (testing "simple invocation via call"
    (let [counter (volatile! 0)
          sample-obj #js {"inc-fn"    #(vswap! counter inc)
                          "return-fn" (fn [& args] args)
                          "add-fn"    (fn [n] (vswap! counter + n))
                          "add*-fn"   (fn [& args] (vreset! counter (apply + @counter args)))}]
      (ocall sample-obj "inc-fn")                                                                                             ; note ocall should work the same as ocall!
      (is (= @counter 1))
      (is (= (ocall! sample-obj "return-fn" 1) '(1)))
      (is (= (ocall! sample-obj "return-fn") nil))
      (is (= (ocall! sample-obj ["return-fn" []] 1 2 3) '(1 2 3)))
      (ocall! sample-obj "add-fn" 1)
      (is (= @counter 2))
      (ocall! sample-obj "add-fn" 1 2 3 4)
      (is (= @counter 3))
      (ocall! sample-obj "add*-fn" 1 2 3 4)
      (is (= @counter 13))))
  (testing "ocall should retarget this"
    (let [who? (fn []
                 (this-as this
                   (aget this "whoami")))
          obj #js {"whoami" "ROOT!"
                   "f"      who?
                   "a"      #js {"whoami" "A!"
                                 "f"      who?}
                   "b"      #js {"a" #js {"whoami" "BA!"
                                          "f"      who?}}}]
      ; static case
      (is (= (ocall obj "f") "ROOT!"))
      (is (= (ocall obj "a.f") "A!"))
      (is (= (ocall obj "b.a.f") "BA!"))
      ; dynamic case
      (is (= (ocall obj (identity "f")) "ROOT!"))
      (is (= (ocall obj (identity "a.f")) "A!"))
      (is (= (ocall obj (identity "b.a.f")) "BA!"))))
  (testing "threading macro with ocall"
    (let [o #js {"e" #js {"f" (fn [x] #js {"g" (fn [y z] (+ x y z))})}}]
      (is (= (-> o
                 (ocall "e.f" 1)
                 (ocall "g" 2 3)) 6)
          (= (-> o
                 (ocall (identity "e.f") 1)
                 (ocall (identity "g") 2 3)) 6))))
  (testing "test errors when ocalling non-functions"
    (when-not-advanced-mode
      (presume-compiler-config {:runtime-expected-function-value :error})
      (with-runtime-config {:error-reporting :console}
        (let [sample-obj #js {"nil-fn" nil
                              "non-fn" 1}
              recorder (atom [])]
          (with-console-recording recorder
            ; static/dynamic case
            (ocall sample-obj "missing-fn")
            (ocall+ sample-obj (identity "missing-fn"))
            (ocall sample-obj "nil-fn")
            (ocall+ sample-obj (identity "nil-fn"))
            (ocall sample-obj "non-fn")
            (ocall+ sample-obj (identity "non-fn"))
            (ocall sample-obj "?missing-fn2")                                                                                 ; should be silent
            (ocall+ sample-obj (identity "?missing-fn2"))
            (ocall sample-obj "?nil-fn")                                                                                      ; should be silent as well
            (ocall+ sample-obj (identity "?nil-fn"))
            (ocall sample-obj "?non-fn")                                                                                      ; should not be silent
            (ocall+ sample-obj (identity "?non-fn")))
          (is (= @recorder ["ERROR: (\"Oops, Missing expected object key 'missing-fn'\" {:path \"missing-fn\", :key \"missing-fn\", :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Missing expected object key 'missing-fn'\" {:path \"missing-fn\", :key \"missing-fn\", :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'nil-fn', got <null> instead\" {:path \"nil-fn\", :soft? false, :fn nil, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'nil-fn', got <null> instead\" {:path \"nil-fn\", :soft? false, :fn nil, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? false, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? false, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function or nil on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? true, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function or nil on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? true, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"])))))))

(deftest test-oapply
  (testing "simple invocation via apply"
    (let [counter (volatile! 0)
          sample-obj #js {"inc-fn"    #(vswap! counter inc)
                          "return-fn" (fn [& args] args)
                          "add-fn"    (fn [n] (vswap! counter + n))
                          "add*-fn"   (fn [& args] (vreset! counter (apply + @counter args)))}]
      (oapply sample-obj "inc-fn" [])                                                                                         ; note oapply should work the same as oapply!
      (is (= @counter 1))
      (is (= (oapply! sample-obj "return-fn" [1]) '(1)))
      (is (= (oapply! sample-obj "return-fn" []) nil))
      (is (= (oapply! sample-obj "return-fn" [1 2 3]) '(1 2 3)))
      (oapply! sample-obj "add-fn" (list 1))
      (is (= @counter 2))
      (oapply! sample-obj "add-fn" (list 1 2 3 4))
      (is (= @counter 3))
      (oapply! sample-obj "add*-fn" (range 5))
      (is (= @counter 13))))
  (testing "oapply should retarget this"
    (let [who? (fn []
                 (this-as this
                   (aget this "whoami")))
          obj #js {"whoami" "ROOT!"
                   "f"      who?
                   "a"      #js {"whoami" "A!"
                                 "f"      who?}
                   "b"      #js {"a" #js {"whoami" "BA!"
                                          "f"      who?}}}]
      ; static case
      (is (= (oapply obj "f" []) "ROOT!"))
      (is (= (oapply obj "a.f" []) "A!"))
      (is (= (oapply obj "b.a.f" []) "BA!"))
      ; dynamic case
      (is (= (oapply obj (identity "f") []) "ROOT!"))
      (is (= (oapply obj (identity "a.f") []) "A!"))
      (is (= (oapply obj (identity "b.a.f") []) "BA!"))))
  (testing "test errors when oapplying to non-functions"
    (when-not-advanced-mode
      (presume-compiler-config {:runtime-expected-function-value :error})
      (with-runtime-config {:error-reporting :console}
        (let [sample-obj #js {"nil-fn" nil
                              "non-fn" 1}
              recorder (atom [])]
          (with-console-recording recorder
            ; static/dynamic case
            (oapply sample-obj "missing-fn" [])
            (oapply+ sample-obj (identity "missing-fn") [])
            (oapply sample-obj "nil-fn" [])
            (oapply+ sample-obj (identity "nil-fn") [])
            (oapply sample-obj "non-fn" [])
            (oapply+ sample-obj (identity "non-fn") [])
            (oapply sample-obj "?missing-fn2" [])                                                                             ; should be silent
            (oapply+ sample-obj (identity "?missing-fn2") [])
            (oapply sample-obj "?nil-fn" [])                                                                                  ; should be silent as well
            (oapply+ sample-obj (identity "?nil-fn") [])
            (oapply sample-obj "?non-fn" [])                                                                                  ; should not be silent
            (oapply+ sample-obj (identity "?non-fn") []))
          (is (= @recorder ["ERROR: (\"Oops, Missing expected object key 'missing-fn'\" {:path \"missing-fn\", :key \"missing-fn\", :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Missing expected object key 'missing-fn'\" {:path \"missing-fn\", :key \"missing-fn\", :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'nil-fn', got <null> instead\" {:path \"nil-fn\", :soft? false, :fn nil, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'nil-fn', got <null> instead\" {:path \"nil-fn\", :soft? false, :fn nil, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? false, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? false, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function or nil on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? true, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"
                            "ERROR: (\"Oops, Expected a function or nil on key path 'non-fn', got <number> instead\" {:path \"non-fn\", :soft? true, :fn 1, :obj #js {:nil-fn nil, :non-fn 1}})"])))))))

(deftest test-param-evaluation
  (testing "obj param must evaluate only once"
    (with-runtime-config {:suppress-reporting #{:dynamic-selector-usage}}
      (let [counter (atom 0)
            get-obj (fn [] (swap! counter inc) (js-obj "k1" nil "f" identity))]
        (are [code call-count] (do (reset! counter 0) code (= @counter call-count))
          (oget (get-obj) "?k1" "?k2") 1
          (oget+ (get-obj) (identity "?k1") "?k2") 1
          (oset! (get-obj) "!kx" true) 1
          (oset!+ (get-obj) (identity "!ky") true) 1
          (ocall (get-obj) "f") 1
          (ocall! (get-obj) "f") 1
          (ocall+ (get-obj) (identity "f")) 1
          (ocall!+ (get-obj) (identity "f")) 1
          (oapply (get-obj) "f" []) 1
          (oapply! (get-obj) "f" []) 1
          (oapply+ (get-obj) (identity "f") []) 1
          (oapply!+ (get-obj) (identity "f") []) 1))))
  (testing "selector must evaluate only once"
    (with-runtime-config {:suppress-reporting #{:dynamic-selector-usage}}
      (let [counter (atom 0)
            o (js-obj "k1" nil "f" identity)
            get-sel (fn [x] (swap! counter inc) x)]
        (are [code call-count] (do (reset! counter 0) code (= @counter call-count))
          (oget o (get-sel "?k1") "?k2") 1
          (oget+ o (get-sel "?k1") "?k2") 1
          (oset! o (get-sel "!kx") true) 1
          (oset!+ o (get-sel "!ky") true) 1
          (ocall o (get-sel "f")) 1
          (ocall! o (get-sel "f")) 1
          (ocall+ o (get-sel "f")) 1
          (ocall!+ o (get-sel "f")) 1
          (oapply o (get-sel "f") []) 1
          (oapply! o (get-sel "f") []) 1
          (oapply+ o (get-sel "f") []) 1
          (oapply!+ o (get-sel "f") []) 1))))
  (testing "val must evaluate only once"
    (with-runtime-config {:suppress-reporting #{:dynamic-selector-usage}}
      (let [counter (atom 0)
            o (js-obj)
            get-val (fn [x] (swap! counter inc) x)]
        (are [code call-count] (do (reset! counter 0) code (= @counter call-count))
          (oset! o "!kx" (get-val true)) 1
          (oset!+ o "!ky" (get-val true)) 1))))
  (testing "args must evaluate only once"
    (with-runtime-config {:suppress-reporting #{:dynamic-selector-usage}}
      (let [counter (atom 0)
            o (js-obj "k1" nil "f" identity)
            get-arg (fn [x] (swap! counter inc) x)]
        (are [code call-count] (do (reset! counter 0) code (= @counter call-count))
          (ocall o "f" (get-arg 1) (get-arg 2)) 2
          (ocall! o "f" (get-arg 1)) 1
          (ocall+ o "f" (get-arg 1)) 1
          (ocall!+ o "f" (get-arg 1)) 1
          (oapply o "f" (get-arg [1 2 3])) 1
          (oapply o "f" [(get-arg 1) (get-arg 2)]) 2
          (oapply! o "f" [(get-arg 1)]) 1
          (oapply+ o "f" [(get-arg 1)]) 1
          (oapply!+ o "f" [(get-arg 1)]) 1)))))

; TODO: we will probably need chromedriver to automate this...

(defn raise-error! []
  (oget+ (js-obj) (identity nil)))

(comment
  ; just for testing stack call-site stack trace in Chrome by hand...
  (with-runtime-config {:unexpected-empty-selector :error}
    (raise-error!)))

(deftest test-runtime-errors
  (under-chrome
    (testing "runtime errors should be thrown from call-site locations"
      (presume-runtime-config {:error-reporting                    :throw
                               :throw-errors-from-macro-call-sites true})
      (with-runtime-config {:unexpected-empty-selector :error}
        (let [cause-error! (fn []
                             (try
                               (raise-error!)
                               (catch :default e
                                 e)))
              extract-top-stack-location (fn [e]
                                           ; first line is the error message itself
                                           (second (string/split-lines (.-stack e))))]
          (is (some? (re-find #"raise_error_BANG_" (extract-top-stack-location (cause-error!)))))
          (with-runtime-config {:throw-errors-from-macro-call-sites false}
            (is (nil? (re-find #"raise_error_BANG_" (extract-top-stack-location (cause-error!)))))))))))

(deftest test-selector-macroexpansion
  (testing "selectors should macro-expand before processed"
    (with-compiler-config {:dynamic-selector-usage :warn
                           :diagnostics            true}
      (let [recorder (atom [])]
        (with-stderr-recording recorder
          (oget (js-obj) (macro-identity "?x"))
          (oget (js-obj) ["?a" (macro-identity (macro-identity "?x"))])
          (oget (js-obj) ["?" "a" "?" (macro-identity (macro-identity "x"))])
          (oset! (js-obj) (macro-identity "!x") (macro-identity "val"))
          (oset! (js-obj) ["!" (macro-identity "x")] (macro-identity "val"))
          (ocall (js-obj "f" identity) (macro-identity "f") (macro-identity "p"))
          (oapply (js-obj "f" identity) (macro-identity "f") (macro-identity ["p"])))
        (is (empty? @recorder) "expected no warnings about dynamic selectors"))))
  (testing "selectors should not macro-expand if expansion was disabled"
    (with-compiler-config {:dynamic-selector-usage :warn
                           :diagnostics            true
                           :macroexpand-selectors  false}
      (let [recorder (atom [])]
        (with-stderr-recording recorder
          (oget (js-obj) (macro-identity "?x"))
          (oset! (js-obj) (macro-identity "!x") (macro-identity "val"))
          (ocall (js-obj "f" identity) (macro-identity "f") (macro-identity "p"))
          (oapply (js-obj "f" identity) (macro-identity "f") (macro-identity ["p"])))
        (is (= (count @recorder) 4))
        (is (re-matches #".*Unexpected dynamic selector usage.*" (str (first @recorder))))))))

(deftest test-invalid-selectors
  (testing "invalid punching selectors (static)"
    (with-compiler-config {:static-unexpected-punching-selector :warn
                           :diagnostics                         true}
      (let [recorder (atom [])]
        (with-stderr-recording recorder
          (oget (js-obj) "!x")
          (oset! (js-obj) "!x" "val")                                                                                         ; no warning
          (ocall (js-obj "f" identity) "!f")
          (oapply (js-obj "f" identity) "!f" []))
        (is (= (count @recorder) 3))
        (is (every? #(re-matches #".*Unexpected punching selector.*" (str %)) @recorder)))))
  (when-not-advanced-mode
    (testing "invalid punching selectors (dynamic)"
      (with-compiler-config {:runtime-unexpected-punching-selector :warn
                             :diagnostics                          true}
        (let [recorder (atom [])]
          (with-console-recording recorder
            (oget+ (js-obj) (identity "!x"))
            (oset!+ (js-obj) (identity "!x") "val")                                                                           ; no warning
            (ocall+ (js-obj "f" identity) (identity "!f"))
            (oapply+ (js-obj "f" identity) (identity "!f") []))
          (is (= (count @recorder) 3))
          (is (every? #(re-matches #".*Unexpected punching selector.*" (str %)) @recorder))))))
  (testing "invalid soft selectors (static)"
    (with-compiler-config {:static-unexpected-soft-selector :warn
                           :diagnostics                     true}
      (let [recorder (atom [])]
        (with-stderr-recording recorder
          (oget (js-obj) "?x")                                                                                                ; no warning
          (oset! (js-obj) "?x" "val")
          (oset! (js-obj) "!a.?x" "val")
          (ocall (js-obj "f" identity) "?f")                                                                                  ; no warning
          (oapply (js-obj "f" identity) "?f" []))                                                                             ; no warning
        (is (= (count @recorder) 2))
        (is (every? #(re-matches #".*Unexpected soft selector.*" (str %)) @recorder)))))
  (when-not-advanced-mode
    (testing "invalid soft selectors (dynamic)"
      (with-compiler-config {:runtime-unexpected-soft-selector :warn
                             :diagnostics                      true}
        (let [recorder (atom [])]
          (with-console-recording recorder
            (oget+ (js-obj) (identity "?x"))                                                                                  ; no warning
            (oset!+ (js-obj) (identity "?x") "val")
            (oset!+ (js-obj) (identity "!a.?x") "val")
            (ocall+ (js-obj "f" identity) (identity "?f"))                                                                    ; no warning
            (oapply+ (js-obj "f" identity) (identity "?f") []))                                                               ; no warning
          (is (= (count @recorder) 2))
          (is (every? #(re-matches #".*Unexpected soft selector.*" (str %)) @recorder))))))
  (testing "empty selectors (static)"
    (with-compiler-config {:static-unexpected-empty-selector :warn
                           :diagnostics                      true}
      (let [recorder (atom [])]
        (with-stderr-recording recorder
          (oget (js-obj) nil)
          (oget (js-obj) [])
          (oget (js-obj) [[] []])
          (oset! (js-obj) nil "val")
          (oset! (js-obj) [[] []] "val")
          (when-not-advanced-mode
            (are [snippet] (thrown-with-msg? js/Error #".*Expected a function, got <object> instead.*" snippet)
              (ocall (js-obj) [[] []] "p1" "p2")
              (oapply (js-obj) [[] []] ["p1" "p2"]))))
        (is (= (count @recorder) (if-advanced-mode 5 7)))
        (is (every? #(re-matches #".*Unexpected empty selector.*" (str %)) @recorder)))))
  (when-not-advanced-mode
    (testing "empty selector usage in oget (dynamic)"
      (let [recorder (atom [])]
        (with-console-recording recorder
          (oget+ (js-obj) (identity nil))
          (oget+ (js-obj) (identity []))
          (oget+ (js-obj) (identity [[] []])))
        (is (= @recorder ["WARN: (\"Oops, Unexpected empty selector\" nil)"
                          "WARN: (\"Oops, Unexpected empty selector\" nil)"
                          "WARN: (\"Oops, Unexpected empty selector\" nil)"])))))
  (when-not-advanced-mode
    (testing "empty selector usage error with :unexpected-empty-selector :error (dynamic)"
      (presume-runtime-config {:error-reporting :throw})
      (with-runtime-config {:unexpected-empty-selector :error}
        (is (thrown-with-msg? js/Error #"Unexpected empty selector" (oget+ (js-obj) (identity nil)))))))
  (when-not-advanced-mode
    (testing "empty selector usage error with :unexpected-empty-selector false (dynamic)"
      (with-runtime-config {:unexpected-empty-selector false}
        (let [o (js-obj)]
          (is o (oget+ o (identity nil))))))))

(deftest test-gget
  (let [obj #js {:key               "val"
                 "@#$%fancy key^&*" "fancy-val"
                 "nested"           #js {:nested-key1  "nk1"
                                         "nested-key2" 2}}]
    (oset! js/window "!x" obj)
    (testing "simple static gget"
      (with-runtime-config {:missing-object-key false}
        (are [key expected] (= (gget key) expected)
          "x.non-existent" nil
          "x.key" "val"
          "x.@#$%fancy key^&*" "fancy-val"
          ["x" "nested" "nested-key2"] 2)))
    (testing "simple dynamic get"
      (with-runtime-config {:missing-object-key false}
        (are [dyn-selector expected] (= (gget+ dyn-selector) expected)
          (identity "x.key") "val"
          (identity "x.xxx") nil
          (list "x" (identity "nested") "nested-key1") "nk1"
          (identity ["x" "nested" "nested-key1"]) "nk1"
          ["x" (identity "nested") "nested-key1"] "nk1")))))

(deftest test-gset
  (testing "static set"
    (let [obj #js {"nested" #js {}}]
      (oset! js/window "!x" obj)
      (are [s1 s2] (do (gset! s1 "val") (= (oget obj s2) "val"))
        "x.!xxx" "xxx"
        ["x.!yyy"] "yyy"
        ["x.nested" "!y"] "nested.y")
      (is (= (js/JSON.stringify obj) "{\"nested\":{\"y\":\"val\"},\"xxx\":\"val\",\"yyy\":\"val\"}"))))
  (testing "dynamic selector set"
    (let [obj #js {"nested" #js {}}]
      (oset! js/window "!x" obj)
      (are [s1 s2] (do (gset!+ s1 "val") (= (oget+ obj s2) "val"))
        (identity "x.!key") "key"
        [(identity "x.!nested") (identity "!key2")] "nested.key2")
      (is (= (js/JSON.stringify obj) "{\"nested\":{\"key2\":\"val\"},\"key\":\"val\"}"))))
  (testing "static punching set!"
    (let [obj #js {"nested" #js {}}]
      (oset! js/window "!x" obj)
      (are [s1 s2] (do (gset!+ s1 "val") (= (oget+ obj s2) "val"))
        ".x.!nested.!xxx" "nested.xxx"
        "x.!aaa" "aaa"
        ["x.!z1" "!z2" "!z3"] "z1.z2.z3")
      (is (= (js/JSON.stringify obj) "{\"nested\":{\"xxx\":\"val\"},\"aaa\":\"val\",\"z1\":{\"z2\":{\"z3\":\"val\"}}}"))))
  (testing "dynamic punching set!"
    (let [obj #js {"nested" #js {}}]
      (oset! js/window "!x" obj)
      (are [s1 s2] (do (gset!+ (identity s1) "val") (= (oget+ obj s2) "val"))
        ".x.!nested.!xxx" "nested.xxx"
        "x.!aaa" "aaa"
        ["x" "!z1" "!z2" "!z3"] "z1.z2.z3")
      (is (= (js/JSON.stringify obj) "{\"nested\":{\"xxx\":\"val\"},\"aaa\":\"val\",\"z1\":{\"z2\":{\"z3\":\"val\"}}}")))))

(deftest test-gcall
  (testing "simple invocation via gcall"
    (let [counter (volatile! 0)
          obj #js {"inc-fn"    #(vswap! counter inc)
                   "return-fn" (fn [& args] args)
                   "add-fn"    (fn [n] (vswap! counter + n))
                   "add*-fn"   (fn [& args] (vreset! counter (apply + @counter args)))}]
      (oset! js/window "!x" obj)
      (gcall "x.inc-fn")
      (is (= @counter 1))
      (is (= (gcall! "x.return-fn" 1) '(1)))
      (is (= (gcall! "x.return-fn") nil))
      (is (= (gcall! ["x" "return-fn" []] 1 2 3) '(1 2 3)))
      (gcall! "x.add-fn" 1)
      (is (= @counter 2))
      (gcall! "x.add-fn" 1 2 3 4)
      (is (= @counter 3))
      (gcall! "x.add*-fn" 1 2 3 4)
      (is (= @counter 13))))
  (testing "gcall should retarget this"
    (let [who? (fn []
                 (this-as this
                   (aget this "whoami")))
          obj #js {"whoami" "ROOT!"
                   "f"      who?
                   "a"      #js {"whoami" "A!"
                                 "f"      who?}
                   "b"      #js {"a" #js {"whoami" "BA!"
                                          "f"      who?}}}]
      (oset! js/window "!x" obj)
      ; static case
      (is (= (gcall "x.f") "ROOT!"))
      (is (= (gcall "x.a.f") "A!"))
      (is (= (gcall "x.b.a.f") "BA!"))
      ; dynamic case
      (is (= (gcall (identity "x.f")) "ROOT!"))
      (is (= (gcall (identity "x.a.f")) "A!"))
      (is (= (gcall (identity "x.b.a.f")) "BA!")))))

(deftest test-gapply
  (testing "simple invocation via gapply"
    (let [counter (volatile! 0)
          obj #js {"inc-fn"    #(vswap! counter inc)
                   "return-fn" (fn [& args] args)
                   "add-fn"    (fn [n] (vswap! counter + n))
                   "add*-fn"   (fn [& args] (vreset! counter (apply + @counter args)))}]
      (oset! js/window "!x" obj)
      (gapply "x.inc-fn" [])
      (is (= @counter 1))
      (is (= (gapply! "x.return-fn" [1]) '(1)))
      (is (= (gapply! "x.return-fn" []) nil))
      (is (= (gapply! "x.return-fn" [1 2 3]) '(1 2 3)))
      (gapply! "x.add-fn" (list 1))
      (is (= @counter 2))
      (gapply! "x.add-fn" (list 1 2 3 4))
      (is (= @counter 3))
      (gapply! "x.add*-fn" (range 5))
      (is (= @counter 13))))
  (testing "gapply should retarget this"
    (let [who? (fn []
                 (this-as this
                   (aget this "whoami")))
          obj #js {"whoami" "ROOT!"
                   "f"      who?
                   "a"      #js {"whoami" "A!"
                                 "f"      who?}
                   "b"      #js {"a" #js {"whoami" "BA!"
                                          "f"      who?}}}]
      (oset! js/window "!x" obj)
      ; static case
      (is (= (gapply "x.f" []) "ROOT!"))
      (is (= (gapply "x.a.f" []) "A!"))
      (is (= (gapply "x.b.a.f" []) "BA!"))
      ; dynamic case
      (is (= (gapply (identity "x.f") []) "ROOT!"))
      (is (= (gapply (identity "x.a.f") []) "A!"))
      (is (= (gapply (identity "x.b.a.f") []) "BA!")))))
