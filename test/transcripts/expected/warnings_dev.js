// js-beautify v1.10.0
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/warnings.cljs [dev]
//   {:elide-asserts false,
//    :external-config
//    #:devtools{:config {:silence-optimizations-warning true}},
//    :main oops.arena.warnings,
//    :optimizations :whitespace,
//    :output-dir "test/resources/.compiled/warnings-dev/_workdir",
//    :output-to "test/resources/.compiled/warnings-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   WARNING: Oops, Unexpected dynamic selector usage (consider using oget+) at line 15 <absolute-path>/test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected nil target object at line 18 <absolute-path>/test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected empty selector at line 21 <absolute-path>/test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected empty selector at line 22 <absolute-path>/test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected empty selector at line 23 <absolute-path>/test/src/arena/oops/arena/warnings.cljs
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "no warnings"
//     (let [o #js {"key" "val"}]
//       (oget o "key")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var o_1 = {
  "key": "val"
};
var target_obj_1 = o_1;
var _STAR_runtime_state_STAR__orig_val__1 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__1 = oops.state.prepare_state.call(null, target_obj_1, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__1;
try {
  var next_obj_1 = oops.core.validate_object_access_dynamically.call(null, target_obj_1, 0, "key", true, true, false) ? target_obj_1["key"] : null
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__1
}

// SNIPPET #2:
//   (testing "dynamic property access"
//     (oget #js {} (identity "key")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = {};
var _STAR_runtime_state_STAR__orig_val__2 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__2 = oops.state.prepare_state.call(null, target_obj_2, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__2;
try {
  oops.core.get_selector_dynamically.call(null, target_obj_2, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__2
}

// SNIPPET #3:
//   (testing "static nil target object"
//     (oget nil "k1" "k2" "k3"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3 = null;
var _STAR_runtime_state_STAR__orig_val__3 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__3 = oops.state.prepare_state.call(null, target_obj_3, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__3;
try {
  var next_obj_2 = oops.core.validate_object_access_dynamically.call(null, target_obj_3, 0, "k1", true, true, false) ? target_obj_3["k1"] : null;
  var next_obj_3 = oops.core.validate_object_access_dynamically.call(null, next_obj_2, 0, "k2", true, true, false) ? next_obj_2["k2"] : null;
  var next_obj_4 = oops.core.validate_object_access_dynamically.call(null, next_obj_3, 0, "k3", true, true, false) ? next_obj_3["k3"] : null
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__3
}

// SNIPPET #4:
//   (testing "static empty selector access in oget"
//     (oget (js-obj))
//     (oget (js-obj []))
//     (oget (js-obj [[] []])))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_4 = {};
var _STAR_runtime_state_STAR__orig_val__4 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__4 = oops.state.prepare_state.call(null, target_obj_4, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__4;
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__4
}
var target_obj_5 = function() {
  var obj36 = {};
  return obj36
}();
var _STAR_runtime_state_STAR__orig_val__5 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__5 = oops.state.prepare_state.call(null, target_obj_5, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__5;
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__5
}
var target_obj_6 = function() {
  var obj43 = {};
  return obj43
}();
var _STAR_runtime_state_STAR__orig_val__6 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__6 = oops.state.prepare_state.call(null, target_obj_6, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__6;
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__6
}
