// COMPILER CONFIG:
//   arena/error_dynamic_property_access.cljs [dev]
//   {:elide-asserts false,
//    :external-config {:oops/config {:dynamic-property-access :error}},
//    :main oops.arena.error-dynamic-property-access,
//    :optimizations :whitespace,
//    :output-dir
//    "test/resources/_compiled/error-dynamic-property-access-dev/_workdir",
//    :output-to
//    "test/resources/_compiled/error-dynamic-property-access-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   THROWN: clojure.lang.ExceptionInfo: failed compiling file:test/src/arena/oops/arena/error_dynamic_property_access.cljs
//   Caused by: clojure.lang.ExceptionInfo: Oops, Unexpected dynamic property access at line 8 test/src/arena/oops/arena/error_dynamic_property_access.cljs
// ----------------------------------------------------------------------------------------------------------
// NO GENERATED CODE
