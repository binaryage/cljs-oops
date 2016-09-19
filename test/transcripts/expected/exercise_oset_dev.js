// Clojure v1.9.0-alpha12, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/exercise_oset.cljs [dev]
//   {:elide-asserts false,
//    :main oops.arena.exercise-oset,
//    :optimizations :whitespace,
//    :output-dir "test/resources/_compiled/exercise-oset-dev/_workdir",
//    :output-to "test/resources/_compiled/exercise-oset-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "dev oset! expansion"
//     (oset! js/window "!k1" "!k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_17 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  var obj_18 = window;
  var parent_obj_19 = function() {
    var obj_7 = obj_18;
    var next_obj_8 = oops.core.validate_object_access_dynamically.call(null, obj_7, 2, "k1", true) ? goog.object.get(obj_7, "k1") : null;
    if (!(next_obj_8 == null)) return next_obj_8;
    else return oops.core.punch_key_dynamically_BANG_.call(null, obj_7, "k1")
  }();
  if (oops.core.validate_object_access_dynamically.call(null, parent_obj_19, 2, "k2", true)) goog.object.set(parent_obj_19, "k2", "val");
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_17
}

// SNIPPET #2:
//   (testing "dev oset!+ expansion"
//     (oset!+ js/window (identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_20 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  var obj_21 = window;
  oops.core.set_selector_dynamically.call(null, obj_21, cljs.core.identity.call(null, "!k1.!k2"), "val")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_20
}

// SNIPPET #3:
//   (testing "dev oset!+ expansion with macro-generated params"
//     (oset!+ js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_22 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  var obj_23 = window;
  oops.core.set_selector_dynamically.call(null, obj_23, "!k1.!k2", "val")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_22
};
