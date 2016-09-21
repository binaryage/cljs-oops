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
//   (testing "static ocall expansion"
//     (ocall js/window "method" "p1" "p2"))
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
    if (!(fn_48 == null)) fn_48.call(target_obj_46, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_47
}

// SNIPPET #2:
//   (testing "dynamic ocall expansion"
//     (ocall+ js/window (identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_49 = window;
var _STAR_runtime_state_STAR_50 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_49, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_51 = oops.core.get_selector_dynamically.call(null, target_obj_49, cljs.core.identity.call(null, "method"));
  if (oops.core.validate_fn_call_dynamically.call(null, fn_51, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_51 == null)) fn_51.call(target_obj_49, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_50
}

// SNIPPET #3:
//   (testing "dynamic ocall expansion with macro-generated method"
//     (ocall+ js/window (macro-identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_52 = window;
var _STAR_runtime_state_STAR_53 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_52, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_54 = oops.core.get_selector_dynamically.call(null, target_obj_52, "method");
  if (oops.core.validate_fn_call_dynamically.call(null, fn_54, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_54 == null)) fn_54.call(target_obj_52, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_53
}

// SNIPPET #4:
//   (testing "ocall expansion with disabled diagnostics"
//     (without-diagnostics
//       (ocall js/window "method" "p1" "p2")
//       (ocall+ js/window (identity "method") "p1" "p2")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_55 = function() {
  var target_obj_56 = window;
  var fn_57 = function() {
    var next_obj_26 = goog.object.get(target_obj_56, "method");
    return next_obj_26
  }();
  if (!(fn_57 == null)) fn_57.call(target_obj_56, "p1", "p2");
  else;
  var target_obj_27 = window;
  var fn_28 = oops.core.get_selector_dynamically.call(null, target_obj_27, cljs.core.identity.call(null, "method"));
  if (!(fn_28 == null)) return fn_28.call(target_obj_27, "p1", "p2");
  else return null
}();

// SNIPPET #5:
//   (testing "ocall expansion with enabled debugging"
//     (with-debug
//       (ocall js/window "method" "p1" "p2")
//       (ocall+ js/window (identity "method") "p1" "p2")))
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
        if (!(fn_39 == null)) return fn_39.call(target_obj_59, "p1", "p2");
        else return null;
      else return null
    }();
    if (captured_runtime_state_61 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-35 oops.state/*runtime-state*)");
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_60
  }
  var target_obj_41 = window;
  var _STAR_runtime_state_STAR_42 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_41, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_43 = oops.state._STAR_runtime_state_STAR_;
    var result_44 = function() {
      var fn_45 = oops.core.get_selector_dynamically.call(null,
        target_obj_41, cljs.core.identity.call(null, "method"));
      if (oops.core.validate_fn_call_dynamically.call(null, fn_45, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_45 == null)) return fn_45.call(target_obj_41, "p1", "p2");
        else return null;
      else return null
    }();
    if (captured_runtime_state_43 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-43 oops.state/*runtime-state*)");
    return result_44
  } finally {
    oops.state._STAR_runtime_state_STAR_ =
      _STAR_runtime_state_STAR_42
  }
}();
