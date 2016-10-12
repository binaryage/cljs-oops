// Clojure v1.9.0-alpha13, ClojureScript v1.9.229, js-beautify v1.6.4
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_static.cljs [goog]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :goog, :key-get :goog}},
//    :main oops.arena.ocall-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/ocall-static-goog/_workdir",
//    :output-to "test/resources/.compiled/ocall-static-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple static ocall"
//     (ocall #js {"f" (fn [] 42)} "f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $target_obj_13$$ = {
    f: function() {
      return 42
    }
  },
  $call_info_14$$ = [$target_obj_13$$, $goog$object$get$$($target_obj_13$$, "f")],
  $fn_15$$ = $call_info_14$$[1];
null != $fn_15$$ && $fn_15$$.call($call_info_14$$[0], "p1", "p2");

// SNIPPET #2:
//   (testing "retargeted static ocall"
//     (ocall #js {"a" #js {"f" (fn [] 42)}} "a.f" "p1" "p2"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_16$$, $target_obj_5$$inline_6$$ = $goog$object$get$$({
  a: {
    f: function() {
      return 42
    }
  }
}, "a");
$call_info_16$$ = [$target_obj_5$$inline_6$$, $goog$object$get$$($target_obj_5$$inline_6$$, "f")];
var $fn_17$$ = $call_info_16$$[1];
null != $fn_17$$ && $fn_17$$.call($call_info_16$$[0], "p1", "p2");
