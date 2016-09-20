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

var target_obj_21 = window;
var _STAR_runtime_state_STAR_22 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_21);
try {
  var obj_23 = target_obj_21;
  var obj_24 = oops.core.validate_object_access_dynamically.call(null, obj_23, 0, "k1", true) ? goog.object.get(obj_23, "k1") : null;
  var next_obj_25 = oops.core.validate_object_access_dynamically.call(null, obj_24, 1, "k2", true) ? goog.object.get(obj_24, "k2") : null;
  if (!(next_obj_25 == null)) {
    var obj_26 = next_obj_25;
    if (oops.core.validate_object_access_dynamically.call(null, obj_26, 0, "k3",
        true)) goog.object.get(obj_26, "k3");
    else;
  } else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_22
}

// SNIPPET #2:
//   (testing "dev oget+ expansion"
//     (oget+ js/window (identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_27 = window;
var _STAR_runtime_state_STAR_28 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_27);
try {
  oops.core.get_selector_dynamically.call(null, target_obj_27, cljs.core.identity.call(null, "k1.?k2.k3"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_28
}

// SNIPPET #3:
//   (testing "dev ocall+ expansion with macro-generated params"
//     (oget+ js/window (macro-identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_29 = window;
var _STAR_runtime_state_STAR_30 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_29);
try {
  oops.core.get_selector_dynamically.call(null, target_obj_29, "k1.?k2.k3")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_30
};
