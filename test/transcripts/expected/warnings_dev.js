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
var _STAR_console_reporter_STAR_26 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj_27 = o_1;
  if (oops.core.validate_object_dynamically.call(null, obj_27, 0)) obj_27["key"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_26
}

// SNIPPET #2:
//   => dynamic property access
//   (oget <JSValue#2> (identity "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_28 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_28
}

// SNIPPET #3:
//   => static nil target object
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_29 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj_30 = null;
  var obj_31 = oops.core.validate_object_dynamically.call(null, obj_30, 0) ? obj_30["k1"] : null;
  var obj_32 = oops.core.validate_object_dynamically.call(null, obj_31, 0) ? obj_31["k2"] : null;
  if (oops.core.validate_object_dynamically.call(null, obj_32, 0)) obj_32["k3"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_29
}

// SNIPPET #4:
//   => static empty selector access in oget
//   (oget (js-obj))
//   (oget (js-obj []))
//   (oget (js-obj [[] []]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_33 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_33
}
var _STAR_console_reporter_STAR_34 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj35 = {}
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_34
}
var _STAR_console_reporter_STAR_36 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj37 = {}
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_36
};
