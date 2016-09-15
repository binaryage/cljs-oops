// Clojure v1.8.0, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/basic_oget.cljs [default]
//   {:elide-asserts true,
//    :main oops.arena.basic-oget,
//    :optimizations :advanced,
//    :output-dir "test/resources/_compiled/basic-oget-default/_workdir",
//    :output-to "test/resources/_compiled/basic-oget-default/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   => simple get
//   (oget <JSValue#1> "key")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

"val";

// SNIPPET #2:
//   => simple miss
//   (oget <JSValue#2> "xxx")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

({
  key: "val"
}).xxx;

// SNIPPET #3:
//   => simple get from refd-object
//   (def o1 <JSValue#3>)
//   (oget o1 "key")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

"val";

// SNIPPET #4:
//   => nested get
//   (def o2 <JSValue#4>)
//   (oget o2 "nested" "nested-key")
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

"nested-val";

// SNIPPET #5:
//   => nested keyword selector
//   (def o3 <JSValue#5>)
//   (oget o3 [:nested [:nested-key]])
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

"nested-val";

// SNIPPET #6:
//   => some edge cases
//   (oget nil)
//   (def o4 nil)
//   (oget o4)
//   (oget o4 :a :b :c)
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

null.a.b.c;
