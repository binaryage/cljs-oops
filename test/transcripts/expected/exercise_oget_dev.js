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

var _STAR_runtime_state_STAR_15 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  var obj_16 = window;
  var obj_17 = oops.core.validate_object_access_dynamically.call(null, obj_16, 0, "k1", true) ? goog.object.get(obj_16, "k1") : null;
  var next_obj_18 = oops.core.validate_object_access_dynamically.call(null, obj_17, 1, "k2", true) ? goog.object.get(obj_17, "k2") : null;
  if (!(next_obj_18 == null)) {
    var obj_19 = next_obj_18;
    if (oops.core.validate_object_access_dynamically.call(null, obj_19, 0, "k3", true)) goog.object.get(obj_19,
      "k3");
    else;
  } else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_15
}

// SNIPPET #2:
//   (testing "dev oget+ expansion"
//     (oget+ js/window (identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_20 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  oops.core.get_selector_dynamically.call(null, window, cljs.core.identity.call(null, "k1.?k2.k3"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_20
}

// SNIPPET #3:
//   (testing "dev ocall+ expansion with macro-generated params"
//     (oget+ js/window (macro-identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_21 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  oops.core.get_selector_dynamically.call(null, window, "k1.?k2.k3")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_21
};
