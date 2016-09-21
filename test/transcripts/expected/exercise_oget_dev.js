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
//   (testing "static oget expansion"
//     (oget js/window "k1" ["?k2" "k3"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_47 = window;
var _STAR_runtime_state_STAR_48 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_47, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var next_obj_49 = oops.core.validate_object_access_dynamically.call(null, target_obj_47, 0, "k1", true) ? goog.object.get(target_obj_47, "k1") : null;
  var next_obj_50 = oops.core.validate_object_access_dynamically.call(null, next_obj_49, 1, "k2", true) ? goog.object.get(next_obj_49, "k2") : null;
  if (!(next_obj_50 == null)) var next_obj_51 = oops.core.validate_object_access_dynamically.call(null, next_obj_50, 0, "k3", true) ? goog.object.get(next_obj_50,
    "k3") : null;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_48
}

// SNIPPET #2:
//   (testing "dynamic oget expansion"
//     (oget+ js/window (identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_52 = window;
var _STAR_runtime_state_STAR_53 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_52, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  oops.core.get_selector_dynamically.call(null, target_obj_52, cljs.core.identity.call(null, "k1.?k2.k3"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_53
}

// SNIPPET #3:
//   (testing "dynamic oget expansion with macro-generated params"
//     (oget+ js/window (macro-identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_54 = window;
var _STAR_runtime_state_STAR_55 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_54, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  oops.core.get_selector_dynamically.call(null, target_obj_54, "k1.?k2.k3")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_55
}

// SNIPPET #4:
//   (testing "oget expansion with disabled diagnostics"
//     (without-diagnostics
//       (oget js/window "k1.?k2.k3")
//       (oget+ js/window (identity "k1.?k2.k3"))))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_56 = function() {
  var target_obj_2472 = window;
  var next_obj_58 = goog.object.get(target_obj_2472, "k1");
  var next_obj_59 = goog.object.get(next_obj_58, "k2");
  if (!(next_obj_59 == null)) var next_obj_60 = goog.object.get(next_obj_59, "k3");
  else;
  var target_obj_29 = window;
  return oops.core.get_selector_dynamically.call(null, target_obj_29, cljs.core.identity.call(null, "k1.?k2.k3"))
}();

// SNIPPET #5:
//   (testing "oget expansion with enabled debugging"
//     (with-debug
//       (oget js/window "k1.?k2.k3")
//       (oget+ js/window (identity "k1.?k2.k3"))))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_61 = function() {
  var target_obj_62 = window;
  var _STAR_runtime_state_STAR_63 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_62, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_64 = oops.state._STAR_runtime_state_STAR_;
    var result_65 = function() {
      var next_obj_40 = oops.core.validate_object_access_dynamically.call(null,
        target_obj_62, 0, "k1", true) ? goog.object.get(target_obj_62, "k1") : null;
      var next_obj_41 = oops.core.validate_object_access_dynamically.call(null, next_obj_40, 1, "k2", true) ? goog.object.get(next_obj_40, "k2") : null;
      if (!(next_obj_41 == null)) {
        var next_obj_42 = oops.core.validate_object_access_dynamically.call(null, next_obj_41, 0, "k3", true) ? goog.object.get(next_obj_41, "k3") : null;
        return next_obj_42
      } else return null
    }();
    if (captured_runtime_state_64 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-36 oops.state/*runtime-state*)");
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_63
  }
  var target_obj_43 = window;
  var _STAR_runtime_state_STAR_44 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_43, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_45 =
      oops.state._STAR_runtime_state_STAR_;
    var result_46 = oops.core.get_selector_dynamically.call(null, target_obj_43, cljs.core.identity.call(null, "k1.?k2.k3"));
    if (captured_runtime_state_45 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-45 oops.state/*runtime-state*)");
    return result_46
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_44
  }
}();
