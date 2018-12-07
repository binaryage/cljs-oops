// js-beautify v1.8.9
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_static.cljs [core]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :core, :key-get :core},
//     :devtools/config {:silence-optimizations-warning true}},
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

// SNIPPET #3:
//   (testing "threading macro with static ocall, see issue #12"
//     (let [o #js {"e" #js {"f" (fn [x] #js {"g" (fn [y z] (+ x y z))})}}]
//       (-> o
//           (ocall "e.f" 1)
//           (ocall "g" 2 3))))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3, call_info_3, target_obj_4 = {
  f: function(x13) {
    return {
      g: function(y14, z15) {
        return x13 + y14 + z15
      }
    }
  }
};
call_info_3 = [target_obj_4, target_obj_4.f];
var fn_3 = call_info_3[1];
target_obj_3 = null != fn_3 ? fn_3.call(call_info_3[0], 1) : null;
var call_info_4 = [target_obj_3, target_obj_3.g],
  fn_4 = call_info_4[1];
null != fn_4 && fn_4.call(call_info_4[0], 2, 3);
