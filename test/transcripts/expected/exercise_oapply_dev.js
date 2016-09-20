// Clojure v1.9.0-alpha12, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/exercise_oapply.cljs [dev]
//   {:elide-asserts false,
//    :main oops.arena.exercise-oapply,
//    :optimizations :whitespace,
//    :output-dir "test/resources/_compiled/exercise-oapply-dev/_workdir",
//    :output-to "test/resources/_compiled/exercise-oapply-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "dev oapply expansion"
//     (oapply js/window "method" ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_26 = window;
var _STAR_runtime_state_STAR_27 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_26);
try {
  var obj_28 = target_obj_26;
  var fn_29 = function() {
    var obj_9 = obj_28;
    if (oops.core.validate_object_access_dynamically.call(null, obj_9, 0, "method", true)) return goog.object.get(obj_9, "method");
    else return null
  }();
  if (oops.core.validate_fn_call_dynamically.call(null, fn_29, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_29 == null)) fn_29.apply(obj_28, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null,
      2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_27
}

// SNIPPET #2:
//   (testing "dev oapply+ expansion"
//     (oapply+ js/window (identity "method") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_30 = window;
var _STAR_runtime_state_STAR_31 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_30);
try {
  var obj_32 = target_obj_30;
  var fn_33 = oops.core.get_selector_dynamically.call(null, obj_32, cljs.core.identity.call(null, "method"));
  if (oops.core.validate_fn_call_dynamically.call(null, fn_33, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_33 == null)) fn_33.apply(obj_32, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ =
    _STAR_runtime_state_STAR_31
}

// SNIPPET #3:
//   (testing "dev oapply+ expansion with macro-generated method"
//     (oapply+ js/window (macro-identity "method") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_34 = window;
var _STAR_runtime_state_STAR_35 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_34);
try {
  var obj_36 = target_obj_34;
  var fn_37 = oops.core.get_selector_dynamically.call(null, obj_36, "method");
  if (oops.core.validate_fn_call_dynamically.call(null, fn_37, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_37 == null)) fn_37.apply(obj_36, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ =
    _STAR_runtime_state_STAR_35
};
