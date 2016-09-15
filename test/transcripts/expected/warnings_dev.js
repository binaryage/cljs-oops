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
//   WARNING: Oops, Unexpected dynamic property access at line 15 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected nil target object at line 18 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Accessing target object with empty selector at line 21 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Accessing target object with empty selector at line 22 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Accessing target object with empty selector at line 23 test/src/arena/oops/arena/warnings.cljs
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   => no warnings
//   (let [o <JSValue#1>] (oget o "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var o_1 = {
  "key": "val"
};
var _STAR_runtime_state_STAR_28 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, o_1);
try {
  var obj_29 = o_1;
  if (oops.core.validate_object_access_dynamically.call(null, obj_29, 0, "key", true)) obj_29["key"];
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_28
}

// SNIPPET #2:
//   => dynamic property access
//   (oget <JSValue#2> (identity "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_30 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, {});
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_30
}

// SNIPPET #3:
//   => static nil target object
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_31 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, null);
try {
  var obj_32 = null;
  var obj_33 = oops.core.validate_object_access_dynamically.call(null, obj_32, 0, "k1", true) ? obj_32["k1"] : null;
  var obj_34 = oops.core.validate_object_access_dynamically.call(null, obj_33, 0, "k2", true) ? obj_33["k2"] : null;
  if (oops.core.validate_object_access_dynamically.call(null, obj_34, 0, "k3", true)) obj_34["k3"];
  else;
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_31
}

// SNIPPET #4:
//   => static empty selector access in oget
//   (oget (js-obj))
//   (oget (js-obj []))
//   (oget (js-obj [[] []]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_runtime_state_STAR_35 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, {});
try {} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_35
}
var _STAR_runtime_state_STAR_36 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, function() {
  var obj20 = {};
  return obj20
}());
try {
  var obj37 = {}
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_36
}
var _STAR_runtime_state_STAR_38 = oops.state._STAR_runtime_state_STAR_;
oops.state._STAR_runtime_state_STAR_ = oops.state.prepare_state.call(null, function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
}, function() {
  var obj25 = {};
  return obj25
}());
try {
  var obj39 = {}
} finally {
  oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR_38
};
