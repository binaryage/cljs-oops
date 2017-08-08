// Clojure v1.9.0-alpha17, ClojureScript v1.9.854, js-beautify v1.6.14
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_dynamic.cljs [goog]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :goog, :key-get :goog},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.ocall-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/ocall-dynamic-goog/_workdir",
//    :output-to "test/resources/.compiled/ocall-dynamic-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple dynamic ocall"
//     (ocall+ #js {"f" (fn [] 42)} (identity "f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_1 = oops.core.get_selector_call_info_dynamically({
    f: function() {
      return 42
    }
  }, "f"),
  fn_1 = call_info_1[1];
null != fn_1 && fn_1.call(call_info_1[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retageted dynamic ocall"
//     (ocall+ #js {"a" #js {"f" (fn [] 42)}} (identity "a.f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var call_info_2 = oops.core.get_selector_call_info_dynamically({
    a: {
      f: function() {
        return 42
      }
    }
  }, "a.f"),
  fn_2 = call_info_2[1];
null != fn_2 && fn_2.call(call_info_2[0], "p1", "p2");

// SNIPPET #3:
//   (testing "threading macro with dynamic ocall, see issue #12"
//     (let [o #js {"e" #js {"f" (fn [x] #js {"g" (fn [y z] (+ x y z))})}}]
//       (-> o
//           (ocall+ (identity "e.f") 1)
//           (ocall+ (identity "g") 2 3))))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1, call_info_3 = oops.core.get_selector_call_info_dynamically({
    e: {
      f: function(x8) {
        return {
          g: function(y9, z10) {
            return x8 + y9 + z10
          }
        }
      }
    }
  }, "e.f"),
  fn_3 = call_info_3[1];
target_obj_1 = null != fn_3 ? fn_3.call(call_info_3[0], 1) : null;
var call_info_4 = oops.core.get_selector_call_info_dynamically(target_obj_1, "g"),
  fn_4 = call_info_4[1];
null != fn_4 && fn_4.call(call_info_4[0], 2, 3);
