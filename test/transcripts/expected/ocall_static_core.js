// Clojure v1.9.0-alpha14, ClojureScript v1.9.456, js-beautify v1.6.8
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_static.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.ocall-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/ocall-static-core/_workdir",
//    :output-to "test/resources/.compiled/ocall-static-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple static ocall"
//     (ocall #js {"f" (fn [] 42)} "f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = {
    f: function() {
      return 42
    }
  },
  call_info_1 = [target_obj_1, target_obj_1.f],
  fn_1 = call_info_1[1];
null != fn_1 && fn_1.call(call_info_1[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retargeted static ocall"
//     (ocall #js {"a" #js {"f" (fn [] 42)}} "a.f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_2, target_obj_2 = {
  f: function() {
    return 42
  }
};
call_info_2 = [target_obj_2, target_obj_2.f];
var fn_2 = call_info_2[1];
null != fn_2 && fn_2.call(call_info_2[0], "p1", "p2");
