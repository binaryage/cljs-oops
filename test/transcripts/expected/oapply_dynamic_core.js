// Clojure v1.9.0-alpha13, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oapply_dynamic.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.oapply-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oapply-dynamic-core/_workdir",
//    :output-to "test/resources/.compiled/oapply-dynamic-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple dynamic oapply"
//     (oapply+ #js {"f" (fn [] 42)} (identity "f") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_9$$ = $oops$core$get2_selector_dynamically$$({
    f: function() {
      return 42
    }
  }, "f"),
  $fn_10$$ = $call_info_9$$[1];
null != $fn_10$$ && $fn_10$$.apply($call_info_9$$[0], $oops$helpers$to_native_array$$());

// SNIPPET #2:
//   (testing "retageted dynamic oapply"
//     (oapply+ #js {"a" #js {"f" (fn [] 42)}} (identity "a.f") ["p1" "p2"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_11$$ = $oops$core$get2_selector_dynamically$$({
    a: {
      f: function() {
        return 42
      }
    }
  }, "a.f"),
  $fn_12$$ = $call_info_11$$[1];
null != $fn_12$$ && $fn_12$$.apply($call_info_11$$[0], $oops$helpers$to_native_array$$());
