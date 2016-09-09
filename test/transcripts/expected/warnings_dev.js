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

var _STAR_console_reporter_STAR_34 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  oops.core.get_selector_dynamically.call(null, {}, cljs.core.identity.call(null, "key"))
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_35
}

// SNIPPET #2:
//   (oget nil "k1" "k2" "k3")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var _STAR_console_reporter_STAR_36 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj37 = function() {
    var obj9 = function() {
      var obj10 = null;
      if (cljs.core.truth_(oops.core.validate_object_dynamically.call(null, obj10))) return obj10["k1"];
      else return null
    }();
    if (cljs.core.truth_(oops.core.validate_object_dynamically.call(null, obj9))) return obj9["k2"];
    else return null
  }();
  if (cljs.core.truth_(oops.core.validate_object_dynamically.call(null, obj38))) obj39["k3"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_40
}

// SNIPPET #3:
//   (let [o <JSValue#2>] (oget o "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var o_21 = {
  "key": "val"
};
var _STAR_console_reporter_STAR_41 = oops.state._STAR_console_reporter_STAR_;
oops.state._STAR_console_reporter_STAR_ = function() {
  arguments[0].apply(console, Array.prototype.slice.call(arguments, 1))
};
try {
  var obj42 = o_21;
  if (cljs.core.truth_(oops.core.validate_object_dynamically.call(null, obj43))) obj44["key"];
  else;
} finally {
  oops.state._STAR_console_reporter_STAR_ = _STAR_console_reporter_STAR_45
};
