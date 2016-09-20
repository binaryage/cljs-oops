// Clojure v1.9.0-alpha12, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/exercise_oget.cljs [dev]
//   {:elide-asserts false,
//    :main oops.arena.exercise-oget,
//    :optimizations :whitespace,
//    :output-dir "test/resources/_compiled/exercise-oget-dev/_workdir",
//    :output-to "test/resources/_compiled/exercise-oget-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "dev oget expansion"
//     (oget js/window "k1" ["?k2" "k3"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_19 = window;
var _STAR_runtime_state_STAR_20 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_19, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var next_obj_21 = oops.core.validate_object_access_dynamically.call(null, target_obj_19, 0, "k1", true) ? goog.object.get(target_obj_19, "k1") : null;
  var next_obj_22 = oops.core.validate_object_access_dynamically.call(null, next_obj_21, 1, "k2", true) ? goog.object.get(next_obj_21, "k2") : null;
  if (!(next_obj_22 == null)) var next_obj_23 = oops.core.validate_object_access_dynamically.call(null, next_obj_22, 0, "k3", true) ? goog.object.get(next_obj_22,
    "k3") : null;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_20
}

// SNIPPET #2:
//   (testing "dev oget+ expansion"
//     (oget+ js/window (identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_24 = window;
var _STAR_runtime_state_STAR_25 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_24, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  oops.core.get_selector_dynamically.call(null, target_obj_24, cljs.core.identity.call(null, "k1.?k2.k3"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_25
}

// SNIPPET #3:
//   (testing "dev ocall+ expansion with macro-generated params"
//     (oget+ js/window (macro-identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_26 = window;
var _STAR_runtime_state_STAR_27 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_26, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  oops.core.get_selector_dynamically.call(null, target_obj_26, "k1.?k2.k3")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_27
};
