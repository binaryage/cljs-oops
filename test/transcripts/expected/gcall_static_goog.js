// js-beautify v1.14.0
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/gcall_static.cljs [goog]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :goog, :key-get :goog},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.gcall-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/gcall-static-goog/_workdir",
//    :output-to "test/resources/.compiled/gcall-static-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple static gcall"
//     (gcall "f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_1 = [goog.global, module.contents$goog.object_get.call(null, goog.global, "f")],
  fn_1 = call_info_1[1];
null != fn_1 && fn_1.call(call_info_1[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retargeted static gcall"
//     (gcall "a.f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_2, target_obj_1 = module$contents$goog.object_get.call(null, goog.global, "a");
call_info_2 = [target_obj_1, module$contents$goog$object_get.call(null, target_obj_1, "f")];
var fn_2 = call_info_2[1];
null != fn_2 && fn_2.call(call_info_2[0], "p1", "p2");
