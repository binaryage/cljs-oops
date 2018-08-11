// js-beautify v1.7.5
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_dev.cljs
//   {:elide-asserts false,
//    :external-config
//    #:devtools{:config {:silence-optimizations-warning true}},
//    :main oops.arena.ocall-dev,
//    :optimizations :whitespace,
//    :output-dir "test/resources/.compiled/ocall-dev/_workdir",
//    :output-to "test/resources/.compiled/ocall-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "static ocall expansion"
//     (ocall js/window "method" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = window;
var _STAR_runtime_state_STAR__orig_val__1 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__1 = oops.state.prepare_state.call(null, target_obj_1, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__1;
try {
  var call_info_1 = [target_obj_1, function() {
    var next_obj_1 = oops.core.validate_object_access_dynamically.call(null, target_obj_1, 0, "method", true, true, false) ? target_obj_1["method"] : null;
    return next_obj_1
  }()];
  var fn_1 = call_info_1[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_1, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_1 == null)) fn_1.call(call_info_1[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__1
}

// SNIPPET #2:
//   (testing "dynamic ocall expansion"
//     (ocall+ js/window (identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = window;
var _STAR_runtime_state_STAR__orig_val__2 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__2 = oops.state.prepare_state.call(null, target_obj_2, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__2;
try {
  var call_info_2 = oops.core.get_selector_call_info_dynamically.call(null, target_obj_2, cljs.core.identity.call(null, "method"));
  var fn_2 = call_info_2[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_2, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_2 == null)) fn_2.call(call_info_2[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__2
}

// SNIPPET #3:
//   (testing "ocall expansion with macro-generated method and params should be static"
//     (ocall js/window (macro-identity "method") (macro-identity "p1") "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3 = window;
var _STAR_runtime_state_STAR__orig_val__3 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__3 = oops.state.prepare_state.call(null, target_obj_3, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__3;
try {
  var call_info_3 = [target_obj_3, function() {
    var next_obj_2 = oops.core.validate_object_access_dynamically.call(null, target_obj_3, 0, "method", true, true, false) ? target_obj_3["method"] : null;
    return next_obj_2
  }()];
  var fn_3 = call_info_3[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_3, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_3 == null)) fn_3.call(call_info_3[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__3
}

// SNIPPET #4:
//   (testing "ocall expansion with disabled diagnostics"
//     (without-diagnostics
//       (ocall js/window "method" "p1" "p2")
//       (ocall+ js/window (identity "method") "p1" "p2")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_1 = function() {
  var target_obj_4 = window;
  var call_info_4 = [target_obj_4, function() {
    var next_obj_3 = target_obj_4["method"];
    return next_obj_3
  }()];
  var fn_4 = call_info_4[1];
  if (!(fn_4 == null)) fn_4.call(call_info_4[0], "p1", "p2");
  else;
  var target_obj_5 = window;
  var call_info_5 = oops.core.get_selector_call_info_dynamically.call(null, target_obj_5, cljs.core.identity.call(null, "method"));
  var fn_5 = call_info_5[1];
  if (!(fn_5 == null)) return fn_5.call(call_info_5[0], "p1", "p2");
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
  var _STAR_runtime_state_STAR__orig_val__4 = oops.state._STAR_runtime_state_STAR_;
  var _STAR_runtime_state_STAR__temp_val__4 = oops.state.prepare_state.call(null, target_obj_6, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__4;
  try {
    var captured_runtime_state_1 = oops.state._STAR_runtime_state_STAR_;
    var result_3 = function() {
      var call_info_6 = [target_obj_6, function() {
        var next_obj_4 = oops.core.validate_object_access_dynamically.call(null, target_obj_6, 0, "method", true, true, false) ? target_obj_6["method"] : null;
        return next_obj_4
      }()];
      var fn_6 = call_info_6[1];
      if (oops.core.validate_fn_call_dynamically.call(null, fn_6, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_6 == null)) return fn_6.call(call_info_6[0], "p1", "p2");
        else return null;
      else return null
    }();
    if (captured_runtime_state_1 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-53 oops.state/*runtime-state*)");
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
      var call_info_7 = oops.core.get_selector_call_info_dynamically.call(null, target_obj_7, cljs.core.identity.call(null, "method"));
      var fn_7 = call_info_7[1];
      if (oops.core.validate_fn_call_dynamically.call(null, fn_7, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_7 == null)) return fn_7.call(call_info_7[0], "p1", "p2");
        else return null;
      else return null
    }();
    if (captured_runtime_state_2 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-63 oops.state/*runtime-state*)");
    return result_4
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__5
  }
}();

// SNIPPET #6:
//   (testing "static ocall expansion with retargeting"
//     (ocall js/window "m1.m2" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_8 = window;
var _STAR_runtime_state_STAR__orig_val__6 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__6 = oops.state.prepare_state.call(null, target_obj_8, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__6;
try {
  var call_info_8 = function() {
    var target_obj_9 = function() {
      var next_obj_5 = oops.core.validate_object_access_dynamically.call(null, target_obj_8, 0, "m1", true, true, false) ? target_obj_8["m1"] : null;
      return next_obj_5
    }();
    return [target_obj_9, function() {
      var next_obj_6 = oops.core.validate_object_access_dynamically.call(null, target_obj_9, 0, "m2", true, true, false) ? target_obj_9["m2"] : null;
      return next_obj_6
    }()]
  }();
  var fn_8 = call_info_8[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_8, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_8 == null)) fn_8.call(call_info_8[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__6
}

// SNIPPET #7:
//   (testing "dynamic ocall expansion with retargeting"
//     (ocall+ js/window (identity "m1.m2") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_10 = window;
var _STAR_runtime_state_STAR__orig_val__7 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__7 = oops.state.prepare_state.call(null, target_obj_10, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__7;
try {
  var call_info_9 = oops.core.get_selector_call_info_dynamically.call(null, target_obj_10, cljs.core.identity.call(null, "m1.m2"));
  var fn_9 = call_info_9[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_9, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_9 == null)) fn_9.call(call_info_9[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__7
}

// SNIPPET #8:
//   (testing "threading macro with ocall, see issue #12"
//     (let [o #js {"e" #js {"f" (fn [x] #js {"g"  (fn [y z] (+ x y z))})}}]
//       (-> o
//           (ocall "e.f" 1)
//           (ocall "g" 2 3))))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var o_1 = {
  "e": {
    "f": function(x) {
      return {
        "g": function(y, z) {
          return x + y + z
        }
      }
    }
  }
};
var target_obj_11 = function() {
  var target_obj_12 = o_1;
  var _STAR_runtime_state_STAR__orig_val__8 = oops.state._STAR_runtime_state_STAR_;
  var _STAR_runtime_state_STAR__temp_val__8 = oops.state.prepare_state.call(null, target_obj_12, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__8;
  try {
    var call_info_10 = function() {
      var target_obj_13 = function() {
        var next_obj_18 = oops.core.validate_object_access_dynamically.call(null, target_obj_12, 0, "e", true, true, false) ? target_obj_12["e"] : null;
        return next_obj_18
      }();
      return [target_obj_13, function() {
        var next_obj_19 = oops.core.validate_object_access_dynamically.call(null, target_obj_13, 0, "f", true, true, false) ? target_obj_13["f"] : null;
        return next_obj_19
      }()]
    }();
    var fn_10 = call_info_10[1];
    if (oops.core.validate_fn_call_dynamically.call(null, fn_10, oops.state.get_last_access_modifier.call(null)))
      if (!(fn_10 == null)) return fn_10.call(call_info_10[0], 1);
      else return null;
    else return null
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__8
  }
}();
var _STAR_runtime_state_STAR__orig_val__9 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__9 = oops.state.prepare_state.call(null, target_obj_11, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__9;
try {
  var call_info_11 = [target_obj_11, function() {
    var next_obj_9 = oops.core.validate_object_access_dynamically.call(null, target_obj_11, 0, "g", true, true, false) ? target_obj_11["g"] : null;
    return next_obj_9
  }()];
  var fn_11 = call_info_11[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_11, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_11 == null)) fn_11.call(call_info_11[0], 2, 3);
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__9
}
