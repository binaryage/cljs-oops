// Clojure v1.9.0-alpha13, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_static.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.ocall-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/ocall-static-core/_workdir",
//    :output-to "test/resources/.compiled/ocall-static-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   WARNING: Use of undeclared Var oops.arena.ocall-static/ocall at line 13 /Users/darwin/code/cljs-oops/test/src/arena/oops/arena/ocall_static.cljs
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple static ocall"
//     (ocall #js {"f" (fn [] 42)} "f"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $G__4$$ = {
  f: function() {
    return 42
  }
};
$oops$arena$ocall_static$$.$ocall$.$cljs$core$IFn$_invoke$arity$2$ ? $oops$arena$ocall_static$$.$ocall$.$cljs$core$IFn$_invoke$arity$2$($G__4$$, "f") : $oops$arena$ocall_static$$.$ocall$.call(null, $G__4$$, "f");
