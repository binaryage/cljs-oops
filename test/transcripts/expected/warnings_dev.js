// Clojure v1.9.0-alpha14, ClojureScript v1.9.494, js-beautify v1.6.8
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/warnings.cljs [dev]
//   {:elide-asserts false,
//    :main oops.arena.warnings,
//    :optimizations :whitespace,
//    :output-dir "test/resources/.compiled/warnings-dev/_workdir",
//    :output-to "test/resources/.compiled/warnings-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   WARNING: Oops, Unexpected dynamic selector usage (consider using oget+) at line 15 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected nil target object at line 18 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected empty selector at line 21 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected empty selector at line 22 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected empty selector at line 23 test/src/arena/oops/arena/warnings.cljs
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
var _STAR_runtime_state_STAR_1 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_1, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var next_obj_1 = oops.core.validate_object_access_dynamically.call(null, target_obj_1, 0, "key", true) ? target_obj_1["key"] : null
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_1
}

// SNIPPET #2:
//   (testing "dynamic property access"
//     (oget #js {} (identity "key")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = {};
var _STAR_runtime_state_STAR_2 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_2, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  oops.core.get_selector_dynamically.call(null, target_obj_2, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_2
}

// SNIPPET #3:
//   (testing "static nil target object"
//     (oget nil "k1" "k2" "k3"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_3 = null;
var _STAR_runtime_state_STAR_3 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_3, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {
  var next_obj_2 = oops.core.validate_object_access_dynamically.call(null, target_obj_3, 0, "k1", true) ? target_obj_3["k1"] : null;
  var next_obj_3 = oops.core.validate_object_access_dynamically.call(null, next_obj_2, 0, "k2", true) ? next_obj_2["k2"] : null;
  var next_obj_4 = oops.core.validate_object_access_dynamically.call(null, next_obj_3, 0, "k3", true) ? next_obj_3["k3"] : null
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_3
}

// SNIPPET #4:
//   (testing "static empty selector access in oget"
//     (oget (js-obj))
//     (oget (js-obj []))
//     (oget (js-obj [[] []])))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_4 = {};
var _STAR_runtime_state_STAR_4 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_4, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_4
}
var target_obj_5 = function() {
  var obj28 = {};
  return obj28
}();
var _STAR_runtime_state_STAR_5 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_5, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_5
}
var target_obj_6 = function() {
  var obj33 = {};
  return obj33
}();
var _STAR_runtime_state_STAR_6 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, target_obj_6, new Error, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
});
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_6
};
