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

var _STAR_runtime_state_STAR_20 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  var obj_21 = window;
  var fn_22 = function() {
    var obj_7 = obj_21;
    if (oops.core.validate_object_access_dynamically.call(null, obj_7, 0, "method", true)) return goog.object.get(obj_7, "method");
    else return null
  }();
  if (oops.core.validate_fn_call_dynamically.call(null, fn_22, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_22 == null)) fn_22.apply(obj_21, cljs.core.into_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_20
}

// SNIPPET #2:
//   (testing "dev oapply+ expansion"
//     (oapply+ js/window (identity "method") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_23 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  var obj_24 = window;
  var fn_25 = oops.core.get_selector_dynamically.call(null, obj_24, cljs.core.identity.call(null, "method"));
  if (oops.core.validate_fn_call_dynamically.call(null, fn_25, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_25 == null)) fn_25.apply(obj_24, cljs.core.into_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ =
    _STAR_runtime_state_STAR_23
}

// SNIPPET #3:
//   (testing "dev oapply+ expansion with macro-generated method"
//     (oapply+ js/window (macro-identity "method") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_26 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, window);
try {
  var obj_27 = window;
  var fn_28 = oops.core.get_selector_dynamically.call(null, obj_27, "method");
  if (oops.core.validate_fn_call_dynamically.call(null, fn_28, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_28 == null)) fn_28.apply(obj_27, cljs.core.into_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_26
};
