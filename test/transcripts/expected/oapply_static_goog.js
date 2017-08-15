// Clojure v1.9.0-alpha17, js-beautify v1.6.14
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oapply_static.cljs [goog]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :goog, :key-get :goog},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.oapply-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oapply-static-goog/_workdir",
//    :output-to "test/resources/.compiled/oapply-static-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple static oapply"
//     (oapply #js {"f" (fn [] 42)} "f" ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = {
    f: function() {
      return 42
    }
  },
  call_info_1 = [target_obj_1, goog.object.get(target_obj_1, "f")],
  fn_1 = call_info_1[1];
null != fn_1 && fn_1.apply(call_info_1[0], oops.helpers.to_native_array());

// SNIPPET #2:
//   (testing "retargeted static oapply"
//     (oapply #js {"a" #js {"f" (fn [] 42)}} "a.f" ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_2, target_obj_2 = goog.object.get({
  a: {
    f: function() {
      return 42
    }
  }
}, "a");
call_info_2 = [target_obj_2, goog.object.get(target_obj_2, "f")];
var fn_2 = call_info_2[1];
null != fn_2 && fn_2.apply(call_info_2[0], oops.helpers.to_native_array());
