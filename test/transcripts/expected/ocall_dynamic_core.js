// Clojure v1.9.0-alpha13, ClojureScript v1.9.229, js-beautify v1.6.4
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_dynamic.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.ocall-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/ocall-dynamic-core/_workdir",
//    :output-to "test/resources/.compiled/ocall-dynamic-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple dynamic ocall"
//     (ocall+ #js {"f" (fn [] 42)} (identity "f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_9$$ = $oops$core$get_selector_call_info_dynamically$$({
    f: function() {
      return 42
    }
  }, "f"),
  $fn_10$$ = $call_info_9$$[1];
null != $fn_10$$ && $fn_10$$.call($call_info_9$$[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retageted dynamic ocall"
//     (ocall+ #js {"a" #js {"f" (fn [] 42)}} (identity "a.f") "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_11$$ = $oops$core$get_selector_call_info_dynamically$$({
    a: {
      f: function() {
        return 42
      }
    }
  }, "a.f"),
  $fn_12$$ = $call_info_11$$[1];
null != $fn_12$$ && $fn_12$$.call($call_info_11$$[0], "p1", "p2");
