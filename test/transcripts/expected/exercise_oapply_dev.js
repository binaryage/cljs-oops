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
//   (testing "static oapply expansion"
//     (oapply js/window "method" ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_46 = window;
var _STAR_runtime_state_STAR_47 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_46, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_48 = function() {
    var next_obj_7 = oops.core.validate_object_access_dynamically.call(null, target_obj_46, 0, "method", true) ? goog.object.get(target_obj_46, "method") : null;
    return next_obj_7
  }();
  if (oops.core.validate_fn_call_dynamically.call(null, fn_48, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_48 == null)) fn_48.apply(target_obj_46, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_47
}

// SNIPPET #2:
//   (testing "dynamic oapply expansion"
//     (oapply+ js/window (identity "method") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_49 = window;
var _STAR_runtime_state_STAR_50 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_49, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_51 = oops.core.get_selector_dynamically.call(null, target_obj_49, cljs.core.identity.call(null, "method"));
  if (oops.core.validate_fn_call_dynamically.call(null, fn_51, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_51 == null)) fn_51.apply(target_obj_49, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ =
    _STAR_runtime_state_STAR_50
}

// SNIPPET #3:
//   (testing "dynamic oapply expansion with macro-generated method"
//     (oapply+ js/window (macro-identity "method") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_52 = window;
var _STAR_runtime_state_STAR_53 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_52, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_54 = oops.core.get_selector_dynamically.call(null, target_obj_52, "method");
  if (oops.core.validate_fn_call_dynamically.call(null, fn_54, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_54 == null)) fn_54.apply(target_obj_52, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_53
}

// SNIPPET #4:
//   (testing "oapply expansion with disabled diagnostics"
//     (without-diagnostics
//       (oapply js/window "method" ["p1" "p2"])
//       (oapply+ js/window (identity "method") ["p1" "p2"])))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_55 = function() {
  var target_obj_56 = window;
  var fn_57 = function() {
    var next_obj_26 = goog.object.get(target_obj_56, "method");
    return next_obj_26
  }();
  if (!(fn_57 == null)) fn_57.apply(target_obj_56, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
  else;
  var target_obj_27 = window;
  var fn_28 = oops.core.get_selector_dynamically.call(null, target_obj_27,
    cljs.core.identity.call(null, "method"));
  if (!(fn_28 == null)) return fn_28.apply(target_obj_27, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
  else return null
}();

// SNIPPET #5:
//   (testing "oapply expansion with enabled debugging"
//     (with-debug
//       (oapply js/window "method" ["p1" "p2"])
//       (oapply+ js/window (identity "method") ["p1" "p2"])))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_58 = function() {
  var target_obj_59 = window;
  var _STAR_runtime_state_STAR_60 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_59, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_61 = oops.state._STAR_runtime_state_STAR_;
    var result_62 = function() {
      var fn_39 = function() {
        var next_obj_40 = oops.core.validate_object_access_dynamically.call(null,
          target_obj_59, 0, "method", true) ? goog.object.get(target_obj_59, "method") : null;
        return next_obj_40
      }();
      if (oops.core.validate_fn_call_dynamically.call(null, fn_39, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_39 == null)) return fn_39.apply(target_obj_59, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
        else return null;
      else return null
    }();
    if (captured_runtime_state_61 ===
      oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-35 oops.state/*runtime-state*)");
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_60
  }
  var target_obj_41 = window;
  var _STAR_runtime_state_STAR_42 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_41, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments,
      1))
  });
  try {
    var captured_runtime_state_43 = oops.state._STAR_runtime_state_STAR_;
    var result_44 = function() {
      var fn_45 = oops.core.get_selector_dynamically.call(null, target_obj_41, cljs.core.identity.call(null, "method"));
      if (oops.core.validate_fn_call_dynamically.call(null, fn_45, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_45 == null)) return fn_45.apply(target_obj_41, oops.helpers.to_native_array.call(null, new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["p1", "p2"], null)));
        else return null;
      else return null
    }();
    if (captured_runtime_state_43 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-43 oops.state/*runtime-state*)");
    return result_44
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_42
  }
}();
