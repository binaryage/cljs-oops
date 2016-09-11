// COMPILER CONFIG:
//   arena/warnings.cljs [dev]
//   {:elide-asserts true,
//    :main oops.arena.warnings,
//    :optimizations :whitespace,
//    :output-dir "test/resources/_compiled/warnings-dev/_workdir",
//    :output-to "test/resources/_compiled/warnings-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   WARNING: Oops, Unexpected dynamic property access at line 11 test/src/arena/oops/arena/warnings.cljs
//   WARNING: Oops, Unexpected nil object at line 14 test/src/arena/oops/arena/warnings.cljs
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (oget <JSValue#1> (identity "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_16 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_16
}

// SNIPPET #2:
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_17 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj18 = null;
  var obj19 = oops.core.validate_object_dynamically.call(null, obj18, 0) ? obj18["k1"] : null;
  var obj20 = oops.core.validate_object_dynamically.call(null, obj19, 0) ? obj19["k2"] : null;
  if (oops.core.validate_object_dynamically.call(null, obj20, 0)) obj20["k3"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_17
}

// SNIPPET #3:
//   (let [o <JSValue#2>] (oget o "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var o_11 = {
  "key": "val"
};
var _STAR_console_reporter_STAR_21 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj22 = o_11;
  if (oops.core.validate_object_dynamically.call(null, obj22, 0)) obj22["key"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_21
};
