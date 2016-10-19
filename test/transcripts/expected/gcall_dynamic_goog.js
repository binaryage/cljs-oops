// Clojure v1.9.0-alpha13, ClojureScript v1.9.293, js-beautify v1.6.4
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/gcall_dynamic.cljs [goog]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :goog, :key-get :goog}},
//    :main oops.arena.gcall-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/gcall-dynamic-goog/_workdir",
//    :output-to "test/resources/.compiled/gcall-dynamic-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple dynamic gcall"
//     (gcall+ (identity "f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_9$$ = $oops$core$get_selector_call_info_dynamically$$("f"),
  $fn_10$$ = $call_info_9$$[1];
null != $fn_10$$ && $fn_10$$.call($call_info_9$$[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retageted dynamic gcall"
//     (gcall+ (identity "a.f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_11$$ = $oops$core$get_selector_call_info_dynamically$$("a.f"),
  $fn_12$$ = $call_info_11$$[1];
null != $fn_12$$ && $fn_12$$.call($call_info_11$$[0], "p1", "p2");
