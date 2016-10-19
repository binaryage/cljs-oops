// Clojure v1.9.0-alpha13, ClojureScript v1.9.293, js-beautify v1.6.4
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/gcall_static.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.gcall-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/gcall-static-core/_workdir",
//    :output-to "test/resources/.compiled/gcall-static-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple static gcall"
//     (gcall "f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_11$$ = [this, this.f],
  $fn_12$$ = $call_info_11$$[1];
null != $fn_12$$ && $fn_12$$.call($call_info_11$$[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retargeted static gcall"
//     (gcall "a.f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_13$$, $target_obj_4$$inline_5$$ = this.a;
$call_info_13$$ = [$target_obj_4$$inline_5$$, $target_obj_4$$inline_5$$.f];
var $fn_14$$ = $call_info_13$$[1];
null != $fn_14$$ && $fn_14$$.call($call_info_13$$[0], "p1", "p2");
