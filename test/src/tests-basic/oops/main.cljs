(ns oops.main
  (:require [cljs.test :refer-macros [deftest testing is are run-tests]]
            [oops.core :refer [oget oset ocall oapply]]))

(deftest test-oget
  (testing "simple key retrieval"
    (let [sample-obj #js {:key               "val"
                          "@#$%fancy key^&*" "fancy-val"
                          "nested"           #js {:nested-key1  "nk1"
                                                  "nested-key2" 2}}]
      (are [key expected] (= (oget sample-obj key) expected)
        "non-existent" nil
        "key" "val"
        "@#$%fancy key^&*" "fancy-val"
        ["nested" "nested-key2"] 2)))
  (testing "oget corner cases"
    ; TODO
    ))

(deftest test-oset
  (testing "simple key store"
    (let [sample-obj #js {"nested" #js {}}]
      (are [ks] (= (oget (oset sample-obj ks "val") ks) "val")
        ["xxx"]
        ["yyy"]
        ["nested" "y"])
      (is (= (js/JSON.stringify sample-obj) "{\"nested\":{\"y\":\"val\"},\"xxx\":\"val\",\"yyy\":\"val\"}"))))
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
      (ocall sample-obj "inc-fn")
      (is (= @counter 1))
      (is (= (ocall sample-obj "return-fn" 1) '(1)))
      (is (= (ocall sample-obj "return-fn") nil))
      (is (= (ocall sample-obj "return-fn" 1 2 3) '(1 2 3)))
      (ocall sample-obj "add-fn" 1)
      (is (= @counter 2))
      (ocall sample-obj "add-fn" 1 2 3 4)
      (is (= @counter 3))
      (ocall sample-obj "add*-fn" 1 2 3 4)
      (is (= @counter 13)))))

(deftest test-oapply
  (testing "simple invocation via apply"
    (let [counter (volatile! 0)
          sample-obj #js {"inc-fn"    #(vswap! counter inc)
                          "return-fn" (fn [& args] args)
                          "add-fn"    (fn [n] (vswap! counter + n))
                          "add*-fn"   (fn [& args] (vreset! counter (apply + @counter args)))}]
      (oapply sample-obj "inc-fn" [])
      (is (= @counter 1))
      (is (= (oapply sample-obj "return-fn" [1]) '(1)))
      (is (= (oapply sample-obj "return-fn" []) nil))
      (is (= (oapply sample-obj "return-fn" [1 2 3]) '(1 2 3)))
      (oapply sample-obj "add-fn" (list 1))
      (is (= @counter 2))
      (oapply sample-obj "add-fn" (list 1 2 3 4))
      (is (= @counter 3))
      (oapply sample-obj "add*-fn" (range 5))
      (is (= @counter 13)))))
