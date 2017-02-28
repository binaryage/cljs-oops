// Clojure v1.9.0-alpha14, ClojureScript v1.9.494, js-beautify v1.6.8
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/gcall_dev.cljs
//   {:elide-asserts false,
//    :main oops.arena.gcall-dev,
//    :optimizations :whitespace,
//    :output-dir "test/resources/.compiled/gcall-dev/_workdir",
//    :output-to "test/resources/.compiled/gcall-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "static gcall expansion"
//     (gcall "method" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = goog.global;
var _STAR_runtime_state_STAR_1 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_1, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var call_info_1 = [target_obj_1, function() {
    var next_obj_1 = oops.core.validate_object_access_dynamically.call(null, target_obj_1, 0, "method", true) ? target_obj_1["method"] : null;
    return next_obj_1
  }()];
  var fn_1 = call_info_1[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_1, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_1 == null)) fn_1.call(call_info_1[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_1
}

// SNIPPET #2:
//   (testing "dynamic gcall expansion"
//     (gcall+ (identity "method") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = goog.global;
var _STAR_runtime_state_STAR_2 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_2, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var call_info_2 = oops.core.get_selector_call_info_dynamically.call(null, target_obj_2, cljs.core.identity.call(null, "method"));
  var fn_2 = call_info_2[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_2, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_2 == null)) fn_2.call(call_info_2[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_2
}

// SNIPPET #3:
//   (testing "gcall expansion with macro-generated method and params should be static"
//     (gcall (macro-identity "method") (macro-identity "p1") "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3 = goog.global;
var _STAR_runtime_state_STAR_3 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_3, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var call_info_3 = [target_obj_3, function() {
    var next_obj_2 = oops.core.validate_object_access_dynamically.call(null, target_obj_3, 0, "method", true) ? target_obj_3["method"] : null;
    return next_obj_2
  }()];
  var fn_3 = call_info_3[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_3, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_3 == null)) fn_3.call(call_info_3[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_3
}

// SNIPPET #4:
//   (testing "gcall expansion with disabled diagnostics"
//     (without-diagnostics
//       (gcall "method" "p1" "p2")
//       (gcall+ (identity "method") "p1" "p2")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_1 = function() {
  var target_obj_4 = goog.global;
  var call_info_4 = [target_obj_4, function() {
    var next_obj_3 = target_obj_4["method"];
    return next_obj_3
  }()];
  var fn_4 = call_info_4[1];
  if (!(fn_4 == null)) fn_4.call(call_info_4[0], "p1", "p2");
  else;
  var target_obj_5 = goog.global;
  var call_info_5 = oops.core.get_selector_call_info_dynamically.call(null, target_obj_5, cljs.core.identity.call(null, "method"));
  var fn_5 = call_info_5[1];
  if (!(fn_5 == null)) return fn_5.call(call_info_5[0], "p1", "p2");
  else return null
}();

// SNIPPET #5:
//   (testing "gcall expansion with enabled debugging"
//     (with-debug
//       (gcall "method" "p1" "p2")
//       (gcall+ (identity "method") "p1" "p2")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var result_2 = function() {
  var target_obj_6 = goog.global;
  var _STAR_runtime_state_STAR_4 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_6, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
  try {
    var captured_runtime_state_1 = oops.state._STAR_runtime_state_STAR_;
    var result_3 = function() {
      var call_info_6 = [target_obj_6, function() {
        var next_obj_4 = oops.core.validate_object_access_dynamically.call(null, target_obj_6, 0, "method", true) ? target_obj_6["method"] : null;
        return next_obj_4
      }()];
      var fn_6 = call_info_6[1];
      if (oops.core.validate_fn_call_dynamically.call(null, fn_6, oops.state.get_last_access_modifier.call(null)))
        if (!(fn_6 == null)) return fn_6.call(call_info_6[0], "p1", "p2");
        else return null;
      else return null
    }();
    if (captured_runtime_state_1 === oops.state._STAR_runtime_state_STAR_);
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-45 oops.state/*runtime-state*)");
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_4
  }
  var target_obj_7 = goog.global;
  var _STAR_runtime_state_STAR_5 = oops.state._STAR_runtime_state_STAR_;
  oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_7, new Error, function() {
    arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
  });
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
    else throw new Error("Assert failed: (clojure.core/identical? captured-runtime-state-54 oops.state/*runtime-state*)");
    return result_4
  } finally {
    oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_5
  }
}();

// SNIPPET #6:
//   (testing "static gcall expansion with retargeting"
//     (gcall "m1.m2" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_8 = goog.global;
var _STAR_runtime_state_STAR_6 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_8, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var call_info_8 = function() {
    var target_obj_9 = function() {
      var next_obj_5 = oops.core.validate_object_access_dynamically.call(null, target_obj_8, 0, "m1", true) ? target_obj_8["m1"] : null;
      return next_obj_5
    }();
    return [target_obj_9, function() {
      var next_obj_6 = oops.core.validate_object_access_dynamically.call(null, target_obj_9, 0, "m2", true) ? target_obj_9["m2"] : null;
      return next_obj_6
    }()]
  }();
  var fn_8 = call_info_8[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_8, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_8 == null)) fn_8.call(call_info_8[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_6
}

// SNIPPET #7:
//   (testing "dynamic gcall expansion with retargeting"
//     (gcall+ (identity "m1.m2") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_10 = goog.global;
var _STAR_runtime_state_STAR_7 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_10, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var call_info_9 = oops.core.get_selector_call_info_dynamically.call(null, target_obj_10, cljs.core.identity.call(null, "m1.m2"));
  var fn_9 = call_info_9[1];
  if (oops.core.validate_fn_call_dynamically.call(null, fn_9, oops.state.get_last_access_modifier.call(null)))
    if (!(fn_9 == null)) fn_9.call(call_info_9[0], "p1", "p2");
    else;
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_7
};
