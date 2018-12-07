// js-beautify v1.8.9
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oset_dev.cljs
//   {:elide-asserts false,
//    :external-config
//    #:devtools{:config {:silence-optimizations-warning true}},
//    :main oops.arena.oset-dev,
//    :optimizations :whitespace,
//    :output-dir "test/resources/.compiled/oset-dev/_workdir",
//    :output-to "test/resources/.compiled/oset-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "static oset! expansion"
//     (oset! js/window "!k1" "!k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = window;
var _STAR_runtime_state_STAR__orig_val__1 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__1 = oops.state.prepare_state.call(null, target_obj_1, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__1;
try {
  var parent_obj_1 = function() {
    var next_obj_1 = oops.core.validate_object_access_dynamically.call(null, target_obj_1, 2, "k1", true, true, false) ? target_obj_1["k1"] : null;
    var ensured_obj_1 = next_obj_1 == null ? oops.core.punch_key_dynamically_BANG_.call(null, target_obj_1, "k1") : next_obj_1;
    return ensured_obj_1
  }();
  if (oops.core.validate_object_access_dynamically.call(null, parent_obj_1, 2, "k2", true, true, true)) parent_obj_1["k2"] = "val";
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__1
}

// SNIPPET #2:
//   (testing "dynamic oset! expansion"
//     (oset!+ js/window (identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = window;
var _STAR_runtime_state_STAR__orig_val__2 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__2 = oops.state.prepare_state.call(null, target_obj_2, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__2;
try {
  oops.core.set_selector_dynamically.call(null, target_obj_2, cljs.core.identity.call(null, "!k1.!k2"), "val")
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__2
}

// SNIPPET #3:
//   (testing "oset! expansion with macro-generated params should be static"
//     (oset! js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3 = window;
var _STAR_runtime_state_STAR__orig_val__3 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__3 = oops.state.prepare_state.call(null, target_obj_3, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__3;
try {
  var parent_obj_2 = function() {
    var next_obj_2 = oops.core.validate_object_access_dynamically.call(null, target_obj_3, 2, "k1", true, true, false) ? target_obj_3["k1"] : null;
    var ensured_obj_2 = next_obj_2 == null ? oops.core.punch_key_dynamically_BANG_.call(null, target_obj_3, "k1") : next_obj_2;
    return ensured_obj_2
  }();
  if (oops.core.validate_object_access_dynamically.call(null, parent_obj_2, 2, "k2", true, true, true)) parent_obj_2["k2"] = "val";
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__3
}

// SNIPPET #4:
//   (testing "oset! expansion with disabled diagnostics"
//     (without-diagnostics
//       (oset! js/window "!k1" "!k2" "val")
//       (oset!+ js/window (identity "!k1.!k2") "val")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_1 = function() {
  var target_obj_4 = window;
  var parent_obj_3 = function() {
    var next_obj_3 = target_obj_4["k1"];
    var ensured_obj_3 = next_obj_3 == null ? oops.core.punch_key_dynamically_BANG_.call(null, target_obj_4, "k1") : next_obj_3;
    return ensured_obj_3
  }();
  parent_obj_3["k2"] = "val";
  var target_obj_5 = window;
  oops.core.set_selector_dynamically.call(null, target_obj_5, cljs.core.identity.call(null, "!k1.!k2"), "val");
  return target_obj_5
}();

// SNIPPET #5:
//   (testing "oset! expansion with enabled debugging"
//     (with-debug
//       (oset! js/window "!k1" "!k2" "val")
//       (oset!+ js/window (identity "!k1.!k2") "val")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_2 = function() {
  var target_obj_6 = window;
  var _STAR_runtime_state_STAR__orig_val__4 = oops.state._STAR_runtime_state_STAR_;
  var _STAR_runtime_state_STAR__temp_val__4 = oops.state.prepare_state.call(null, target_obj_6, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__4;
  try {
    var captured_runtime_state_1 = oops.state._STAR_runtime_state_STAR_;
    var result_3 = function() {
      var parent_obj_4 = function() {
        var next_obj_4 = oops.core.validate_object_access_dynamically.call(null, target_obj_6, 2, "k1", true, true, false) ? target_obj_6["k1"] : null;
        var ensured_obj_4 = next_obj_4 == null ? oops.core.punch_key_dynamically_BANG_.call(null, target_obj_6, "k1") : next_obj_4;
        return ensured_obj_4
      }();
      if (oops.core.validate_object_access_dynamically.call(null, parent_obj_4, 2, "k2", true, true, true)) parent_obj_4["k2"] = "val";
      else;
      return target_obj_6
    }();
    if (captured_runtime_state_1 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-44 oops.state/*runtime-state*)");
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__4
  }
  var target_obj_7 = window;
  var _STAR_runtime_state_STAR__orig_val__5 = oops.state._STAR_runtime_state_STAR_;
  var _STAR_runtime_state_STAR__temp_val__5 = oops.state.prepare_state.call(null, target_obj_7, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__5;
  try {
    var captured_runtime_state_2 = oops.state._STAR_runtime_state_STAR_;
    var result_4 = function() {
      oops.core.set_selector_dynamically.call(null, target_obj_7, cljs.core.identity.call(null, "!k1.!k2"), "val");
      return target_obj_7
    }();
    if (captured_runtime_state_2 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-55 oops.state/*runtime-state*)");
    return result_4
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__5
  }
}();
