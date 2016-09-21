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
//   (testing "static oset! expansion"
//     (oset! js/window "!k1" "!k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_44 = window;
var _STAR_runtime_state_STAR_45 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_44, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var parent_obj_46 = function() {
    var next_obj_7 = oops.core.validate_object_access_dynamically.call(null, target_obj_44, 2, "k1", true) ? goog.object.get(target_obj_44, "k1") : null;
    var ensured_obj_8 = next_obj_7 == null ? oops.core.punch_key_dynamically_BANG_.call(null, target_obj_44, "k1") : next_obj_7;
    return ensured_obj_8
  }();
  if (oops.core.validate_object_access_dynamically.call(null, parent_obj_46, 2, "k2", true)) goog.object.set(parent_obj_46, "k2",
    "val");
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_45
}

// SNIPPET #2:
//   (testing "dynamic oset! expansion"
//     (oset!+ js/window (identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_47 = window;
var _STAR_runtime_state_STAR_48 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_47, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  oops.core.set_selector_dynamically.call(null, target_obj_47, cljs.core.identity.call(null, "!k1.!k2"), "val")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_48
}

// SNIPPET #3:
//   (testing "dynamic oset! expansion with macro-generated params"
//     (oset!+ js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_49 = window;
var _STAR_runtime_state_STAR_50 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_49, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  oops.core.set_selector_dynamically.call(null, target_obj_49, "!k1.!k2", "val")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_50
}

// SNIPPET #4:
//   (testing "oset! expansion with disabled diagnostics"
//     (without-diagnostics
//       (oset! js/window "!k1" "!k2" "val")
//       (oset!+ js/window (identity "!k1.!k2") "val")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_51 = function() {
  var target_obj_52 = window;
  var parent_obj_2442 = function() {
    var next_obj_23 = goog.object.get(target_obj_52, "k1");
    var ensured_obj_24 = next_obj_23 == null ? oops.core.punch_key_dynamically_BANG_.call(null, target_obj_52, "k1") : next_obj_23;
    return ensured_obj_24
  }();
  goog.object.set(parent_obj_2442, "k2", "val");
  var target_obj_25 = window;
  oops.core.set_selector_dynamically.call(null, target_obj_25, cljs.core.identity.call(null,
    "!k1.!k2"), "val");
  return target_obj_25
}();

// SNIPPET #5:
//   (testing "oset! expansion with enabled debugging"
//     (with-debug
//       (oset! js/window "!k1" "!k2" "val")
//       (oset!+ js/window (identity "!k1.!k2") "val")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_54 = function() {
  var target_obj_55 = window;
  var _STAR_runtime_state_STAR_56 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_55, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_57 = oops.state._STAR_runtime_state_STAR_;
    var result_58 = function() {
      var parent_obj_59 = function() {
        var next_obj_38 =
          oops.core.validate_object_access_dynamically.call(null, target_obj_55, 2, "k1", true) ? goog.object.get(target_obj_55, "k1") : null;
        var ensured_obj_39 = next_obj_38 == null ? oops.core.punch_key_dynamically_BANG_.call(null, target_obj_55, "k1") : next_obj_38;
        return ensured_obj_39
      }();
      if (oops.core.validate_object_access_dynamically.call(null, parent_obj_59, 2, "k2", true)) goog.object.set(parent_obj_59, "k2", "val");
      else;
      return target_obj_55
    }();
    if (captured_runtime_state_57 ===
      oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-32 oops.state/*runtime-state*)");
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_56
  }
  var target_obj_40 = window;
  var _STAR_runtime_state_STAR_41 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_40, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments,
      1))
  });
  try {
    var captured_runtime_state_42 = oops.state._STAR_runtime_state_STAR_;
    var result_43 = function() {
      oops.core.set_selector_dynamically.call(null, target_obj_40, cljs.core.identity.call(null, "!k1.!k2"), "val");
      return target_obj_40
    }();
    if (captured_runtime_state_42 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-42 oops.state/*runtime-state*)");
    return result_43
  } finally {
    oops.state._STAR_runtime_state_STAR_ =
      _STAR_runtime_state_STAR_41
  }
}();
