// js-beautify v1.8.8
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oget_static.cljs [goog]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :goog, :key-get :goog},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.oget-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oget-static-goog/_workdir",
//    :output-to "test/resources/.compiled/oget-static-goog/main.js",
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

// SNIPPET #7:
//   (testing "oget expansion with macro-generated params should be static"
//     (oget js/window (macro-identity "k1.?k2.k3") (macro-identity :k4)))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// SNIPPET #8:
//   (testing "simple get with usage"
//     (.log js/console (oget #js {"key" "val"} "key")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

console.log(goog.object.get("key"));

// SNIPPET #9:
//   (testing "simple miss with usage"
//     (.log js/console (oget #js {"key" "val"} "xxx")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

console.log(goog.object.get("xxx"));
