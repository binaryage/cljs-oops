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

var target_obj_23 = window;
var _STAR_runtime_state_STAR_24 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_23);
try {
  var obj_25 = target_obj_23;
  var parent_obj_26 = function() {
    var obj_9 = obj_25;
    var next_obj_10 = oops.core.validate_object_access_dynamically.call(null, obj_9, 2, "k1", true) ? goog.object.get(obj_9, "k1") : null;
    if (!(next_obj_10 == null)) return next_obj_10;
    else return oops.core.punch_key_dynamically_BANG_.call(null, obj_9, "k1")
  }();
  if (oops.core.validate_object_access_dynamically.call(null, parent_obj_26, 2, "k2", true)) goog.object.set(parent_obj_26,
    "k2", "val");
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_24
}

// SNIPPET #2:
//   (testing "dev oset!+ expansion"
//     (oset!+ js/window (identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_27 = window;
var _STAR_runtime_state_STAR_28 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_27);
try {
  var obj_29 = target_obj_27;
  oops.core.set_selector_dynamically.call(null, obj_29, cljs.core.identity.call(null, "!k1.!k2"), "val")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_28
}

// SNIPPET #3:
//   (testing "dev oset!+ expansion with macro-generated params"
//     (oset!+ js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_30 = window;
var _STAR_runtime_state_STAR_31 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_30);
try {
  var obj_2232 = target_obj_30;
  oops.core.set_selector_dynamically.call(null, obj_2232, "!k1.!k2", "val")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_31
};
