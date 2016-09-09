// COMPILER CONFIG:
//   arena/dynamic_oget.cljs [goog]
//   {:elide-asserts true,
//    :external-config {:oops/config {:key-set :goog, :key-get :goog}},
//    :main oops.arena.dynamic-oget,
//    :optimizations :advanced,
//    :output-dir "test/resources/_compiled/dynamic-oget-goog/_workdir",
//    :output-to "test/resources/_compiled/dynamic-oget-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   => simple get
//   (oget+ <JSValue#1> (return-this-key "key"))
//   (oget+ <JSValue#2> (identity "key"))
//   (oget+ <JSValue#3> (return-this-key-with-side-effect "key"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

$oops$core$get_selector_dynamically$$({
  key: "val"
}, "key");
$oops$core$get_selector_dynamically$$({
  key: "val"
}, "key");
window.x = "dirty";
$oops$core$get_selector_dynamically$$({
  key: "val"
}, "key");

// SNIPPET #2:
//   => simple miss
//   (oget+ <JSValue#4> (return-this-key "xxx"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

$oops$core$get_selector_dynamically$$({
  key: "val"
}, "xxx");

// SNIPPET #3:
//   => nested get
//   (def o1 <JSValue#5>)
//   (oget+ o1 (return-this-key "key") (return-this-key "nested"))
//   (def o2 <JSValue#6>)
//   (oget+ o2 [(return-this-key "key") (return-this-key "nested")])
//   (def o3 <JSValue#7>)
//   (oget+ o3 (return-this-key "key") [(return-this-key "nested")])
//   (def o4 <JSValue#8>)
//   (oget+ o4 <JSValue#9>)
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $arr__1__auto__$$inline_2$$ = ["key", "nested"];
$arr__1__auto__$$inline_2$$.$oops_tag$$ = !0;
$oops$core$get_selector_dynamically$$({
  key: "val",
  nested: {
    "nested-key": "nested-val"
  }
}, $arr__1__auto__$$inline_2$$);
$oops$core$get_selector_dynamically$$({
  key: "val",
  nested: {
    "nested-key": "nested-val"
  }
}, new $cljs$core$PersistentVector$$(null, 2, 5, $cljs$core$PersistentVector$EMPTY_NODE$$, ["key", "nested"], null));
var $arr__1__auto__$$inline_8$$ = ["key", new $cljs$core$PersistentVector$$(null, 1, 5, $cljs$core$PersistentVector$EMPTY_NODE$$, ["nested"], null)];
$arr__1__auto__$$inline_8$$.$oops_tag$$ = !0;
$oops$core$get_selector_dynamically$$({
  key: "val",
  nested: {
    "nested-key": "nested-val"
  }
}, $arr__1__auto__$$inline_8$$);
$oops$core$get_selector_dynamically$$({
  key: "val",
  nested: {
    "nested-key": "nested-val"
  }
}, ["key", "nested"]);
