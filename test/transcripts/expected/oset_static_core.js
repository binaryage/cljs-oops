// js-beautify v1.14.0
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oset_static.cljs [core]
//   {:elide-asserts true,
//    :external-config
//    {:oops/config {:key-set :core, :key-get :core},
//     :devtools/config {:silence-optimizations-warning true}},
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
(null == next_obj_1 ? oops.core.punch_key_dynamically_BANG_(target_obj_1) : next_obj_1).k2 = "val";

// SNIPPET #2:
//   (testing "oset! expansion with macro-generated params should be static"
//     (oset! js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = window,
  next_obj_2 = target_obj_2.k1;
(null == next_obj_2 ? oops.core.punch_key_dynamically_BANG_(target_obj_2) : next_obj_2).k2 = "val";

// SNIPPET #3:
//   (testing "static oset! expansion without punching"
//     (oset! js/window "k1.k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

window.k1.k2 = "val";
