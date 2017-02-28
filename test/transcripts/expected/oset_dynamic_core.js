// Clojure v1.9.0-alpha14, ClojureScript v1.9.494, js-beautify v1.6.8
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/oset_dynamic.cljs [core]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :core, :key-get :core}},
//    :main oops.arena.oset-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/oset-dynamic-core/_workdir",
//    :output-to "test/resources/.compiled/oset-dynamic-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "dynamic oset! expansion"
//     (oset!+ js/window (identity "!k1.!k2") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

oops.core.set_selector_dynamically("!k1.!k2");

// SNIPPET #2:
//   (testing "dynamic oset! without punching"
//     (oset!+ js/window (identity "k1.k2.k3") "val"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

oops.core.set_selector_dynamically("k1.k2.k3");
