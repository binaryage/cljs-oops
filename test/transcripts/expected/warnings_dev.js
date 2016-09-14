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
var _STAR_console_reporter_STAR_64 = oops.state._STAR_console_reporter_STAR_;
var _STAR_error_reported_QMARK__STAR_65 = oops.state._STAR_error_reported_QMARK__STAR_;
var _STAR_current_key_path_STAR_66 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_67 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_error_reported_QMARK__STAR_ = false;
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = o_1;
try {
  var obj_68 = o_1;
  if (oops.core.validate_object_access_dynamically.call(null, obj_68, 0, "key", true)) obj_68["key"];
  else;
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_67;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_66;
  oops.state._STAR_error_reported_QMARK__STAR_ = _STAR_error_reported_QMARK__STAR_65;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_64
}

// SNIPPET #2:
//   => dynamic property access
//   (oget <JSValue#2> (identity "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_69 = oops.state._STAR_console_reporter_STAR_;
var _STAR_error_reported_QMARK__STAR_70 = oops.state._STAR_error_reported_QMARK__STAR_;
var _STAR_current_key_path_STAR_71 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_72 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_error_reported_QMARK__STAR_ = false;
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = {};
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_72;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_71;
  oops.state._STAR_error_reported_QMARK__STAR_ = _STAR_error_reported_QMARK__STAR_70;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_69
}

// SNIPPET #3:
//   => static nil target object
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_73 = oops.state._STAR_console_reporter_STAR_;
var _STAR_error_reported_QMARK__STAR_74 = oops.state._STAR_error_reported_QMARK__STAR_;
var _STAR_current_key_path_STAR_75 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_76 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_error_reported_QMARK__STAR_ = false;
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = null;
try {
  var obj_77 = null;
  var obj_78 = oops.core.validate_object_access_dynamically.call(null, obj_77, 0, "k1", true) ? obj_77["k1"] : null;
  var obj_3643 = oops.core.validate_object_access_dynamically.call(null, obj_78, 0, "k2", true) ? obj_78["k2"] : null;
  if (oops.core.validate_object_access_dynamically.call(null, obj_3643, 0, "k3", true)) obj_3643["k3"];
  else;
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_76;
  oops.state._STAR_current_key_path_STAR_ =
    _STAR_current_key_path_STAR_75;
  oops.state._STAR_error_reported_QMARK__STAR_ = _STAR_error_reported_QMARK__STAR_74;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_73
}

// SNIPPET #4:
//   => static empty selector access in oget
//   (oget (js-obj))
//   (oget (js-obj []))
//   (oget (js-obj [[] []]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_80 = oops.state._STAR_console_reporter_STAR_;
var _STAR_error_reported_QMARK__STAR_81 = oops.state._STAR_error_reported_QMARK__STAR_;
var _STAR_current_key_path_STAR_82 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_83 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_error_reported_QMARK__STAR_ = false;
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = {};
try {} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_83;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_82;
  oops.state._STAR_error_reported_QMARK__STAR_ = _STAR_error_reported_QMARK__STAR_81;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_80
}
var _STAR_console_reporter_STAR_84 = oops.state._STAR_console_reporter_STAR_;
var _STAR_error_reported_QMARK__STAR_85 = oops.state._STAR_error_reported_QMARK__STAR_;
var _STAR_current_key_path_STAR_86 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_87 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_error_reported_QMARK__STAR_ = false;
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = function() {
  var obj50 = {};
  return obj50
}();
try {
  var obj88 = {}
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_87;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_86;
  oops.state._STAR_error_reported_QMARK__STAR_ = _STAR_error_reported_QMARK__STAR_85;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_84
}
var _STAR_console_reporter_STAR_89 = oops.state._STAR_console_reporter_STAR_;
var _STAR_error_reported_QMARK__STAR_90 = oops.state._STAR_error_reported_QMARK__STAR_;
var _STAR_current_key_path_STAR_91 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_92 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_error_reported_QMARK__STAR_ = false;
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = function() {
  var obj61 = {};
  return obj61
}();
try {
  var obj93 = {}
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_92;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_91;
  oops.state._STAR_error_reported_QMARK__STAR_ = _STAR_error_reported_QMARK__STAR_90;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_89
};
