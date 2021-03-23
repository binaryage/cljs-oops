// js-beautify v1.13.5
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oapply_dynamic.cljs [goog]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :goog, :key-get :goog},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.oapply-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oapply-dynamic-goog/_workdir",
//    :output-to "test/resources/.compiled/oapply-dynamic-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple dynamic oapply"
//     (oapply+ #js {"f" (fn [] 42)} (identity "f") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_1 = oops.core.get_selector_call_info_dynamically({
    f: function() {
      return 42
    }
  }, "f"),
  fn_1 = call_info_1[1];
null != fn_1 && fn_1.apply(call_info_1[0], oops.helpers.to_native_array());

// SNIPPET #2:
//   (testing "retageted dynamic oapply"
//     (oapply+ #js {"a" #js {"f" (fn [] 42)}} (identity "a.f") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_2 = oops.core.get_selector_call_info_dynamically({
    a: {
      f: function() {
        return 42
      }
    }
  }, "a.f"),
  fn_2 = call_info_2[1];
null != fn_2 && fn_2.apply(call_info_2[0], oops.helpers.to_native_array());
