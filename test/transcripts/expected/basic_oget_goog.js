// Clojure v1.9.0-alpha12, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/basic_oget.cljs [goog]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :goog, :key-get :goog}},
//    :main oops.arena.basic-oget,
//    :optimizations :advanced,
//    :output-dir "test/resources/_compiled/basic-oget-goog/_workdir",
//    :output-to "test/resources/_compiled/basic-oget-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple get"
//     (oget #js {"key" "val"} "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// SNIPPET #2:
//   (testing "simple miss"
//     (oget #js {"key" "val"} "xxx"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// SNIPPET #3:
//   (testing "simple get from refd-object"
//     (def o1 #js {"key"    "val"
//                  "nested" #js {"nested-key" "nested-val"}})
//     (oget o1 "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// SNIPPET #4:
//   (testing "nested get"
//     (def o2 #js {"key"    "val"
//                  "nested" #js {"nested-key" "nested-val"}})
//     (oget o2 "nested" "nested-key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// SNIPPET #5:
//   (testing "nested keyword selector"
//     (def o3 #js {"key"    "val"
//                  "nested" #js {"nested-key" "nested-val"}})
//     (oget o3 [:nested [:nested-key]]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// SNIPPET #6:
//   (testing "some edge cases"
//     (oget nil)
//     (def o4 nil)
//     (oget o4)
//     (oget o4 :a :b :c))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
