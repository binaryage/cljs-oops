// js-beautify v1.13.5
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/error_dynamic_selector_usage.cljs [dev]
//   {:elide-asserts false,
//    :external-config
//    {:oops/config {:dynamic-selector-usage :error},
//     :devtools/config {:silence-optimizations-warning true}},
//    :main oops.arena.error-dynamic-selector-usage,
//    :optimizations :whitespace,
//    :output-dir
//    "test/resources/.compiled/error-dynamic-selector-usage-dev/_workdir",
//    :output-to
//    "test/resources/.compiled/error-dynamic-selector-usage-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   THROWN: clojure.lang.ExceptionInfo: failed compiling file:<absolute-path>/test/src/arena/oops/arena/error_dynamic_selector_usage.cljs
//   Caused by: clojure.lang.ExceptionInfo: null
//   Caused by: clojure.lang.ExceptionInfo: Oops, Unexpected dynamic selector usage (consider using oget+) at line 8 <absolute-path>/test/src/arena/oops/arena/error_dynamic_selector_usage.cljs
// ----------------------------------------------------------------------------------------------------------
// NO GENERATED CODE
