// Clojure v1.9.0-alpha14, ClojureScript v1.9.456, js-beautify v1.6.8
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oset_static.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.oset-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oset-static-core/_workdir",
//    :output-to "test/resources/.compiled/oset-static-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "static oset! expansion"
//     (oset! js/window "!k1" "!k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = window,
  next_obj_1 = target_obj_1.k1;
(null == next_obj_1 ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$ ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$(target_obj_1, "k1") : oops.core.punch_key_dynamically_BANG_.call(null, target_obj_1, "k1") : next_obj_1).k2 = "val";

// SNIPPET #2:
//   (testing "oset! expansion with macro-generated params should be static"
//     (oset! js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = window,
  next_obj_2 = target_obj_2.k1;
(null == next_obj_2 ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$ ? oops.core.punch_key_dynamically_BANG_.cljs.core.IFn$_invoke$arity$4$(target_obj_2, "k1") : oops.core.punch_key_dynamically_BANG_.call(null, target_obj_2, "k1") : next_obj_2).k2 = "val";

// SNIPPET #3:
//   (testing "static oset! expansion without punching"
//     (oset! js/window "k1.k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

window.k1.k2 = "val";
