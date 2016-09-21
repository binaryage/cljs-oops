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

var target_obj_1 = window;
var _STAR_runtime_state_STAR_1 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_1, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_1 = function() {
    var next_obj_1 = oops.core.validate_object_access_dynamically.call(null, target_obj_1, 0, "method", true) ? goog.object.get(target_obj_1, "method") : null;
    return next_obj_1
  }();
  if (oops.core.validate_fn_call_dynamically.call(null, fn_1, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_1 == null)) fn_1.call(target_obj_1, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_1
}

// SNIPPET #2:
//   (testing "dynamic ocall expansion"
//     (ocall+ js/window (identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = window;
var _STAR_runtime_state_STAR_2 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_2, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_2 = oops.core.get_selector_dynamically.call(null, target_obj_2, cljs.core.identity.call(null, "method"));
  if (oops.core.validate_fn_call_dynamically.call(null, fn_2, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_2 == null)) fn_2.call(target_obj_2, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_2
}

// SNIPPET #3:
//   (testing "dynamic ocall expansion with macro-generated method"
//     (ocall+ js/window (macro-identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3 = window;
var _STAR_runtime_state_STAR_3 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_3, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var fn_3 = oops.core.get_selector_dynamically.call(null, target_obj_3, "method");
  if (oops.core.validate_fn_call_dynamically.call(null, fn_3, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_3 == null)) fn_3.call(target_obj_3, "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_3
}

// SNIPPET #4:
//   (testing "ocall expansion with disabled diagnostics"
//     (without-diagnostics
//       (ocall js/window "method" "p1" "p2")
//       (ocall+ js/window (identity "method") "p1" "p2")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_1 = function() {
  var target_obj_4 = window;
  var fn_4 = function() {
    var next_obj_2 = goog.object.get(target_obj_4, "method");
    return next_obj_2
  }();
  if (!(fn_4 == null)) fn_4.call(target_obj_4, "p1", "p2");
  else;
  var target_obj_5 = window;
  var fn_5 = oops.core.get_selector_dynamically.call(null, target_obj_5, cljs.core.identity.call(null, "method"));
  if (!(fn_5 == null)) return fn_5.call(target_obj_5, "p1", "p2");
  else return null
}();

// SNIPPET #5:
//   (testing "ocall expansion with enabled debugging"
//     (with-debug
//       (ocall js/window "method" "p1" "p2")
//       (ocall+ js/window (identity "method") "p1" "p2")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_2 = function() {
  var target_obj_6 = window;
  var _STAR_runtime_state_STAR_4 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_6, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_1 = oops.state._STAR_runtime_state_STAR_;
    var result_3 = function() {
      var fn_6 = function() {
        var next_obj_3 = oops.core.validate_object_access_dynamically.call(null,
          target_obj_6, 0, "method", true) ? goog.object.get(target_obj_6, "method") : null;
        return next_obj_3
      }();
      if (oops.core.validate_fn_call_dynamically.call(null, fn_6, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_6 == null)) return fn_6.call(target_obj_6, "p1", "p2");
        else return null;
      else return null
    }();
    if (captured_runtime_state_1 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-35 oops.state/*runtime-state*)");
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_4
  }
  var target_obj_7 = window;
  var _STAR_runtime_state_STAR_5 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_7, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_2 = oops.state._STAR_runtime_state_STAR_;
    var result_4 = function() {
      var fn_7 = oops.core.get_selector_dynamically.call(null,
        target_obj_7, cljs.core.identity.call(null, "method"));
      if (oops.core.validate_fn_call_dynamically.call(null, fn_7, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_7 == null)) return fn_7.call(target_obj_7, "p1", "p2");
        else return null;
      else return null
    }();
    if (captured_runtime_state_2 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-43 oops.state/*runtime-state*)");
    return result_4
  } finally {
    oops.state._STAR_runtime_state_STAR_ =
      _STAR_runtime_state_STAR_5
  }
}();
