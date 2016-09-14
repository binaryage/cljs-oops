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
var _STAR_console_reporter_STAR_56 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_57 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_58 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = o_1;
try {
  var obj_59 = o_1;
  if (oops.core.validate_object_dynamically.call(null, obj_59, 0)) {
    oops.state.add_key_to_current_path_BANG_.call(null, "key");
    if (cljs.core._EQ_.call(null, 0, 0) && cljs.core.not.call(null, goog.object.containsKey(obj_59, "key"))) oops.core.report_if_needed_dynamically.call(null, new cljs.core.Keyword(null, "missing-object-key", "missing-object-key", -10), new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null, "path", "path", -11), oops.state.get_current_key_path_str.call(null),
      new cljs.core.Keyword(null, "key", "key", -12), "key", new cljs.core.Keyword(null, "obj", "obj", 13), oops.state.get_current_obj.call(null)
    ], null));
    else;
    obj_59["key"]
  } else;
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_58;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_57;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_56
}

// SNIPPET #2:
//   => dynamic property access
//   (oget <JSValue#2> (identity "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_60 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_61 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_62 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = {};
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_62;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_61;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_60
}

// SNIPPET #3:
//   => static nil target object
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_63 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_64 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_65 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = null;
try {
  var obj_66 = null;
  var obj_67 = oops.core.validate_object_dynamically.call(null, obj_66, 0) ? function() {
    oops.state.add_key_to_current_path_BANG_.call(null, "k1");
    if (cljs.core._EQ_.call(null, 0, 0) && cljs.core.not.call(null, goog.object.containsKey(obj_66, "k1"))) oops.core.report_if_needed_dynamically.call(null, new cljs.core.Keyword(null, "missing-object-key", "missing-object-key", -10), new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null, "path", "path", -11), oops.state.get_current_key_path_str.call(null), new cljs.core.Keyword(null, "key", "key", -12), "k1", new cljs.core.Keyword(null, "obj", "obj", 13), oops.state.get_current_obj.call(null)], null));
    else;
    return obj_66["k1"]
  }() : null;
  var obj_68 = oops.core.validate_object_dynamically.call(null, obj_67, 0) ? function() {
    oops.state.add_key_to_current_path_BANG_.call(null, "k2");
    if (cljs.core._EQ_.call(null, 0, 0) && cljs.core.not.call(null, goog.object.containsKey(obj_67,
        "k2"))) oops.core.report_if_needed_dynamically.call(null, new cljs.core.Keyword(null, "missing-object-key", "missing-object-key", -10), new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null, "path", "path", -11), oops.state.get_current_key_path_str.call(null), new cljs.core.Keyword(null, "key", "key", -12), "k2", new cljs.core.Keyword(null, "obj", "obj", 13), oops.state.get_current_obj.call(null)], null));
    else;
    return obj_67["k2"]
  }() : null;
  if (oops.core.validate_object_dynamically.call(null,
      obj_68, 0)) {
    oops.state.add_key_to_current_path_BANG_.call(null, "k3");
    if (cljs.core._EQ_.call(null, 0, 0) && cljs.core.not.call(null, goog.object.containsKey(obj_68, "k3"))) oops.core.report_if_needed_dynamically.call(null, new cljs.core.Keyword(null, "missing-object-key", "missing-object-key", -10), new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null, "path", "path", -11), oops.state.get_current_key_path_str.call(null), new cljs.core.Keyword(null, "key", "key", -12),
      "k3", new cljs.core.Keyword(null, "obj", "obj", 13), oops.state.get_current_obj.call(null)
    ], null));
    else;
    obj_68["k3"]
  } else;
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_65;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_64;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_63
}

// SNIPPET #4:
//   => static empty selector access in oget
//   (oget (js-obj))
//   (oget (js-obj []))
//   (oget (js-obj [[] []]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_3563 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_70 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_71 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = {};
try {} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_71;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_70;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_3563
}
var _STAR_console_reporter_STAR_72 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_73 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_74 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = function() {
  var obj44 = {};
  return obj44
}();
try {
  var obj75 = {}
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_74;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_73;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_72
}
var _STAR_console_reporter_STAR_76 = oops.state._STAR_console_reporter_STAR_;
var _STAR_current_key_path_STAR_77 = oops.state._STAR_current_key_path_STAR_;
var _STAR_current_obj_STAR_78 = oops.state._STAR_current_obj_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
oops.state._STAR_current_key_path_STAR_ = [];
oops.state._STAR_current_obj_STAR_ = function() {
  var obj53 = {};
  return obj53
}();
try {
  var obj5575 = {}
} finally {
  oops.state._STAR_current_obj_STAR_ = _STAR_current_obj_STAR_78;
  oops.state._STAR_current_key_path_STAR_ = _STAR_current_key_path_STAR_77;
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_76
};
