// Clojure v1.9.0-alpha12, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/exercise_ocall.cljs [dev]
//   {:elide-asserts false,
//    :main oops.arena.exercise-ocall,
//    :optimizations :whitespace,
//    :output-dir "test/resources/_compiled/exercise-ocall-dev/_workdir",
//    :output-to "test/resources/_compiled/exercise-ocall-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "dev ocall expansion"
//     (ocall js/window "method" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_20 = window;
var _STAR_runtime_state_STAR_21 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_20);
try {
  var fn_22 = function() {
    var next_obj_7 = oops.core.validate_object_access_dynamically.call(null, target_obj_20, 0, "method", true) ? goog.object.get(target_obj_20, "method") : null;
    return next_obj_7
  }();
  if (oops.core.validate_fn_call_dynamically.call(null, fn_22, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_22 == null)) fn_22.call(target_obj_20, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_21
}

// SNIPPET #2:
//   (testing "dev ocall+ expansion"
//     (ocall+ js/window (identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_23 = window;
var _STAR_runtime_state_STAR_24 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_23);
try {
  var fn_25 = oops.core.get_selector_dynamically.call(null, target_obj_23, cljs.core.identity.call(null, "method"));
  if (oops.core.validate_fn_call_dynamically.call(null, fn_25, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_25 == null)) fn_25.call(target_obj_23, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_24
}

// SNIPPET #3:
//   (testing "dev ocall+ expansion with macro-generated method"
//     (ocall+ js/window (macro-identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_26 = window;
var _STAR_runtime_state_STAR_27 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_26);
try {
  var fn_28 = oops.core.get_selector_dynamically.call(null, target_obj_26, "method");
  if (oops.core.validate_fn_call_dynamically.call(null, fn_28, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_28 == null)) fn_28.call(target_obj_26, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_27
};
