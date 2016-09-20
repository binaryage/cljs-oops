// Clojure v1.9.0-alpha12, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/warnings.cljs [dev]
//   {:elide-asserts false,
//    :main oops.arena.warnings,
//    :optimizations :whitespace,
//    :output-dir "test/resources/_compiled/warnings-dev/_workdir",
//    :output-to "test/resources/_compiled/warnings-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   WARNING: Oops, Unexpected dynamic selector usage (consider using oget+) at line 15 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected nil target object at line 18 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Accessing target object with empty selector at line 21 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Accessing target object with empty selector at line 22 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Accessing target object with empty selector at line 23 test/src/arena/oops/arena/warnings.cljs
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "no warnings"
//     (let [o #js {"key" "val"}]
//       (oget o "key")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var o_1 = {
  "key": "val"
};
var target_obj_36 = o_1;
var _STAR_runtime_state_STAR_37 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_36);
try {
  var next_obj_38 = oops.core.validate_object_access_dynamically.call(null, target_obj_36, 0, "key", true) ? goog.object.get(target_obj_36, "key") : null
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_37
}

// SNIPPET #2:
//   (testing "dynamic property access"
//     (oget #js {} (identity "key")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_39 = {};
var _STAR_runtime_state_STAR_40 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_39);
try {
  oops.core.get_selector_dynamically.call(null, target_obj_39, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_40
}

// SNIPPET #3:
//   (testing "static nil target object"
//     (oget nil "k1" "k2" "k3"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_41 = null;
var _STAR_runtime_state_STAR_42 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_41);
try {
  var next_obj_43 = oops.core.validate_object_access_dynamically.call(null, target_obj_41, 0, "k1", true) ? goog.object.get(target_obj_41, "k1") : null;
  var next_obj_44 = oops.core.validate_object_access_dynamically.call(null, next_obj_43, 0, "k2", true) ? goog.object.get(next_obj_43, "k2") : null;
  var next_obj_45 = oops.core.validate_object_access_dynamically.call(null, next_obj_44, 0, "k3", true) ? goog.object.get(next_obj_44, "k3") : null
} finally {
  oops.state._STAR_runtime_state_STAR_ =
    _STAR_runtime_state_STAR_42
}

// SNIPPET #4:
//   (testing "static empty selector access in oget"
//     (oget (js-obj))
//     (oget (js-obj []))
//     (oget (js-obj [[] []])))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_46 = {};
var _STAR_runtime_state_STAR_47 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_46);
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_47
}
var target_obj_48 = function() {
  var obj28 = {};
  return obj28
}();
var _STAR_runtime_state_STAR_49 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_48);
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_49
}
var target_obj_50 = function() {
  var obj33 = {};
  return obj33
}();
var _STAR_runtime_state_STAR_51 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, target_obj_50);
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_51
};
