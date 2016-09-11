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

var _STAR_console_reporter_STAR_17 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_17
}

// SNIPPET #2:
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_18 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj19 = null;
  var obj20 = oops.core.validate_object_dynamically.call(null, obj19, new cljs.core.Keyword(null, "dot", "dot", 9)) ? obj19["k1"] : null;
  var obj21 = oops.core.validate_object_dynamically.call(null, obj20, new cljs.core.Keyword(null, "dot", "dot", 9)) ? obj20["k2"] : null;
  if (oops.core.validate_object_dynamically.call(null, obj21, new cljs.core.Keyword(null, "dot", "dot", 9))) obj21["k3"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ =
    _STAR_console_reporter_STAR_18
}

// SNIPPET #3:
//   (let [o <JSValue#2>] (oget o "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var o_12 = {
  "key": "val"
};
var _STAR_console_reporter_STAR_22 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj23 = o_12;
  if (oops.core.validate_object_dynamically.call(null, obj23, new cljs.core.Keyword(null, "dot", "dot", 9))) obj23["key"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_22
};
