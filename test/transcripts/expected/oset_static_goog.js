// Clojure v1.9.0-alpha17, ClojureScript v1.9.660, js-beautify v1.6.14
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oset_static.cljs [goog]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :goog, :key-get :goog},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.oset-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oset-static-goog/_workdir",
//    :output-to "test/resources/.compiled/oset-static-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "static oset! expansion"
//     (oset! js/window "!k1" "!k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = window,
  next_obj_1 = goog.object.get(target_obj_1);
(null == next_obj_1 ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$ ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$(target_obj_1, "k1") : oops.core.punch_key_dynamically_BANG_.call(null, target_obj_1, "k1") : next_obj_1).k2 = "val";

// SNIPPET #2:
//   (testing "oset! expansion with macro-generated params should be static"
//     (oset! js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = window,
  next_obj_2 = goog.object.get(target_obj_2);
(null == next_obj_2 ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$ ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$(target_obj_2, "k1") : oops.core.punch_key_dynamically_BANG_.call(null, target_obj_2, "k1") : next_obj_2).k2 = "val";

// SNIPPET #3:
//   (testing "static oset! expansion without punching"
//     (oset! js/window "k1.k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

goog.object.get(window).k2 = "val";
