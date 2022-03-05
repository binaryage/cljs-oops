// js-beautify v1.14.0
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oget_dev.cljs
//   {:elide-asserts false,
//    :external-config
//    #:devtools{:config {:silence-optimizations-warning true}},
//    :main oops.arena.oget-dev,
//    :optimizations :whitespace,
//    :output-dir "test/resources/.compiled/oget-dev/_workdir",
//    :output-to "test/resources/.compiled/oget-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "static oget expansion"
//     (oget js/window "k1" ["?k2" "k3"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = window;
var _STAR_runtime_state_STAR__orig_val__1 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__1 = oops.state.prepare_state.call(null, target_obj_1, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__1;
try {
  var next_obj_1 = oops.core.validate_object_access_dynamically.call(null, target_obj_1, 0, "k1", true, true, false) ? target_obj_1["k1"] : null;
  var next_obj_2 = oops.core.validate_object_access_dynamically.call(null, next_obj_1, 1, "k2", true, true, false) ? next_obj_1["k2"] : null;
  if (!(next_obj_2 == null)) var next_obj_3 = oops.core.validate_object_access_dynamically.call(null, next_obj_2, 0, "k3", true, true, false) ? next_obj_2["k3"] : null;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__1
}

// SNIPPET #2:
//   (testing "dynamic oget expansion"
//     (oget+ js/window (identity "k1.?k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = window;
var _STAR_runtime_state_STAR__orig_val__2 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__2 = oops.state.prepare_state.call(null, target_obj_2, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__2;
try {
  oops.core.get_selector_dynamically.call(null, target_obj_2, cljs.core.identity.call(null, "k1.?k2.k3"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__2
}

// SNIPPET #3:
//   (testing "oget expansion with macro-generated params should be static"
//     (oget js/window (macro-identity "k1.?k2.k3") (macro-identity :k4)))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3 = window;
var _STAR_runtime_state_STAR__orig_val__3 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__3 = oops.state.prepare_state.call(null, target_obj_3, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__3;
try {
  var next_obj_4 = oops.core.validate_object_access_dynamically.call(null, target_obj_3, 0, "k1", true, true, false) ? target_obj_3["k1"] : null;
  var next_obj_5 = oops.core.validate_object_access_dynamically.call(null, next_obj_4, 1, "k2", true, true, false) ? next_obj_4["k2"] : null;
  if (!(next_obj_5 == null)) {
    var next_obj_6 = oops.core.validate_object_access_dynamically.call(null, next_obj_5, 0, "k3", true, true, false) ? next_obj_5["k3"] : null;
    var next_obj_7 = oops.core.validate_object_access_dynamically.call(null, next_obj_6, 0, "k4", true, true, false) ? next_obj_6["k4"] : null
  } else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__3
}

// SNIPPET #4:
//   (testing "oget expansion with disabled diagnostics"
//     (without-diagnostics
//       (oget js/window "k1.?k2.k3")
//       (oget+ js/window (identity "k1.?k2.k3"))))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_1 = function() {
  var target_obj_4 = window;
  var next_obj_8 = target_obj_4["k1"];
  var next_obj_9 = next_obj_8["k2"];
  if (!(next_obj_9 == null)) var next_obj_10 = next_obj_9["k3"];
  else;
  var target_obj_5 = window;
  return oops.core.get_selector_dynamically.call(null, target_obj_5, cljs.core.identity.call(null, "k1.?k2.k3"))
}();

// SNIPPET #5:
//   (testing "oget expansion with enabled debugging"
//     (with-debug
//       (oget js/window "k1.?k2.k3")
//       (oget+ js/window (identity "k1.?k2.k3"))))
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
      var next_obj_11 = oops.core.validate_object_access_dynamically.call(null, target_obj_6, 0, "k1", true, true, false) ? target_obj_6["k1"] : null;
      var next_obj_12 = oops.core.validate_object_access_dynamically.call(null, next_obj_11, 1, "k2", true, true, false) ? next_obj_11["k2"] : null;
      if (!(next_obj_12 == null)) {
        var next_obj_13 = oops.core.validate_object_access_dynamically.call(null, next_obj_12, 0, "k3", true, true, false) ? next_obj_12["k3"] : null;
        return next_obj_13
      } else return null
    }();
    if (captured_runtime_state_1 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-52 oops.state/*runtime-state*)");
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
    var result_4 = oops.core.get_selector_dynamically.call(null, target_obj_7, cljs.core.identity.call(null, "k1.?k2.k3"));
    if (captured_runtime_state_2 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-62 oops.state/*runtime-state*)");
    return result_4
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__5
  }
}();

// SNIPPET #6:
//   (testing "nested static oget expansion"
//     (oget (oget js/window "k1") ["?k2" "k3"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_8 = function() {
  var target_obj_9 = window;
  var _STAR_runtime_state_STAR__orig_val__6 = oops.state._STAR_runtime_state_STAR_;
  var _STAR_runtime_state_STAR__temp_val__6 = oops.state.prepare_state.call(null, target_obj_9, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__6;
  try {
    var next_obj_14 = oops.core.validate_object_access_dynamically.call(null, target_obj_9, 0, "k1", true, true, false) ? target_obj_9["k1"] : null;
    return next_obj_14
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__6
  }
}();
var _STAR_runtime_state_STAR__orig_val__7 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__7 = oops.state.prepare_state.call(null, target_obj_8, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__7;
try {
  var next_obj_15 = oops.core.validate_object_access_dynamically.call(null, target_obj_8, 1, "k2", true, true, false) ? target_obj_8["k2"] : null;
  if (!(next_obj_15 == null)) var next_obj_16 = oops.core.validate_object_access_dynamically.call(null, next_obj_15, 0, "k3", true, true, false) ? next_obj_15["k3"] : null;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__7
}

// SNIPPET #7:
//   (testing "nested dynamic oget expansion"
//     (oget+ (oget js/window "k1") (oget js/window "k2.k3")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_10 = function() {
  var target_obj_11 = window;
  var _STAR_runtime_state_STAR__orig_val__8 = oops.state._STAR_runtime_state_STAR_;
  var _STAR_runtime_state_STAR__temp_val__8 = oops.state.prepare_state.call(null, target_obj_11, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__8;
  try {
    var next_obj_17 = oops.core.validate_object_access_dynamically.call(null, target_obj_11, 0, "k1", true, true, false) ? target_obj_11["k1"] : null;
    return next_obj_17
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__8
  }
}();
var _STAR_runtime_state_STAR__orig_val__9 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__9 = oops.state.prepare_state.call(null, target_obj_10, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__9;
try {
  oops.core.get_selector_dynamically.call(null, target_obj_10, function() {
    var target_obj_12 = window;
    var _STAR_runtime_state_STAR__orig_val__10 = oops.state._STAR_runtime_state_STAR_;
    var _STAR_runtime_state_STAR__temp_val__10 = oops.state.prepare_state.call(null, target_obj_12, new Error, function() {
      arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
    });
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__10;
    try {
      var next_obj_18 = oops.core.validate_object_access_dynamically.call(null, target_obj_12, 0, "k2", true, true, false) ? target_obj_12["k2"] : null;
      var next_obj_19 = oops.core.validate_object_access_dynamically.call(null, next_obj_18, 0, "k3", true, true, false) ? next_obj_18["k3"] : null;
      return next_obj_19
    } finally {
      oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__10
    }
  }())
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__9
}
