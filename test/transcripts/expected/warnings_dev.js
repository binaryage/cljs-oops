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
var _STAR_console_reporter_STAR_52 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_53 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_54 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = o_1;
try {
  var obj_55 = o_1;
  if (oops.core.validate_object_access_dynamically.call(null, obj_55, 0, "key", true)) obj_55["key"];
  else;
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_54;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_53;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_52
}

// SNIPPET #2:
//   => dynamic property access
//   (oget <JSValue#2> (identity "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_56 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_57 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_58 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = {};
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_58;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_57;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_56
}

// SNIPPET #3:
//   => static nil target object
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_59 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_60 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_61 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = null;
try {
  var obj_62 = null;
  var obj_63 = oops.core.validate_object_access_dynamically.call(null, obj_62, 0, "k1", true) ? obj_62["k1"] : null;
  var obj_64 = oops.core.validate_object_access_dynamically.call(null, obj_63, 0, "k2", true) ? obj_63["k2"] : null;
  if (oops.core.validate_object_access_dynamically.call(null, obj_64, 0, "k3", true)) obj_64["k3"];
  else;
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_61;
  oops.state._STAR_current_key_path_STAR_ =
    _STAR_current_key_path_STAR_60;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_59
}

// SNIPPET #4:
//   => static empty selector access in oget
//   (oget (js-obj))
//   (oget (js-obj []))
//   (oget (js-obj [[] []]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_65 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_66 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_3523 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = {};
try {} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_3523;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_66;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_65
}
var _STAR_console_reporter_STAR_68 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_69 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_70 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = function() {
  var obj40 = {};
  return obj40
}();
try {
  var obj71 = {}
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_70;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_69;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_68
}
var _STAR_console_reporter_STAR_72 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_73 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_74 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = function() {
  var obj49 = {};
  return obj49
}();
try {
  var obj75 = {}
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_74;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_73;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_72
};
