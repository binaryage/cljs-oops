// js-beautify v1.8.9
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/gcall_dynamic.cljs [core]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :core, :key-get :core},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.gcall-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/gcall-dynamic-core/_workdir",
//    :output-to "test/resources/.compiled/gcall-dynamic-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple dynamic gcall"
//     (gcall+ (identity "f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_1 = oops.core.get_selector_call_info_dynamically("f"),
  fn_1 = call_info_1[1];
null != fn_1 && fn_1.call(call_info_1[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retageted dynamic gcall"
//     (gcall+ (identity "a.f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_2 = oops.core.get_selector_call_info_dynamically("a.f"),
  fn_2 = call_info_2[1];
null != fn_2 && fn_2.call(call_info_2[0], "p1", "p2");
