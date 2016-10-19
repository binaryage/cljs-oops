// Clojure v1.9.0-alpha13, ClojureScript v1.9.293, js-beautify v1.6.4
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oset_static.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.oset-static,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oset-static-core/_workdir",
//    :output-to "test/resources/.compiled/oset-static-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "static oset! expansion"
//     (oset! js/window "!k1" "!k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $target_obj_10$$ = window,
  $next_obj_4$$inline_3$$ = $target_obj_10$$.k1;
(null == $next_obj_4$$inline_3$$ ? $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$4$ ? $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$4$($target_obj_10$$, "k1") : $oops$core$punch_key_dynamically_BANG_$$.call(null, $target_obj_10$$, "k1") : $next_obj_4$$inline_3$$).k2 = "val";

// SNIPPET #2:
//   (testing "oset! expansion with macro-generated params should be static"
//     (oset! js/window (macro-identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $target_obj_11$$ = window,
  $next_obj_6$$inline_7$$ = $target_obj_11$$.k1;
(null == $next_obj_6$$inline_7$$ ? $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$4$ ? $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$4$($target_obj_11$$, "k1") : $oops$core$punch_key_dynamically_BANG_$$.call(null, $target_obj_11$$, "k1") : $next_obj_6$$inline_7$$).k2 = "val";

// SNIPPET #3:
//   (testing "static oset! expansion without punching"
//     (oset! js/window "k1.k2" "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

window.k1.k2 = "val";
