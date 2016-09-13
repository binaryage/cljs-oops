(ns oops.arena
  (:require [clojure.test :refer :all]
            [oops.circus.build :refer [gen-build-variants make-build exercise-builds!]]))

(def all-builds
  (concat
    (gen-build-variants "basic_oget.cljs")
    (gen-build-variants "dynamic_oget.cljs")
    [(make-build "warnings.cljs" "dev" {} {:optimizations :whitespace})
     (make-build "error_static_nil_object.cljs" "dev" {:static-nil-target-object :error} {:optimizations :whitespace})
     (make-build "error_dynamic_property_access.cljs" "dev" {:dynamic-property-access :error} {:optimizations :whitespace})]))

; -- tests ------------------------------------------------------------------------------------------------------------------

(deftest exercise-all-builds
  (exercise-builds! all-builds))
