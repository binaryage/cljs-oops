(ns oops.arena
  (:require [clojure.test :refer :all]
            [oops.circus.build :refer [get-key-mode-options make-build exercise-builds!]]))

(defn make-build-variants [file]
  [(make-build file "core" (get-key-mode-options :core))
   (make-build file "goog" (get-key-mode-options :goog))])

(def all-builds
  (concat
    (make-build-variants "oget_static.cljs")
    (make-build-variants "oget_dynamic.cljs")
    (make-build-variants "oset_static.cljs")
    (make-build-variants "oset_dynamic.cljs")
    (make-build-variants "ocall_static.cljs")
    (make-build-variants "ocall_dynamic.cljs")
    (make-build-variants "oapply_static.cljs")
    (make-build-variants "oapply_dynamic.cljs")
    (make-build-variants "gcall_static.cljs")
    (make-build-variants "gcall_dynamic.cljs")
    [(make-build "oget_dev.cljs" "" {} {:optimizations :whitespace})
     (make-build "oset_dev.cljs" "" {} {:optimizations :whitespace})
     (make-build "ocall_dev.cljs" "" {} {:optimizations :whitespace})
     (make-build "oapply_dev.cljs" "" {} {:optimizations :whitespace})
     (make-build "gcall_dev.cljs" "" {} {:optimizations :whitespace})
     (make-build "warnings.cljs" "dev" {} {:optimizations :whitespace})
     (make-build "error_static_nil_object.cljs" "dev" {:static-nil-target-object :error} {:optimizations :whitespace})
     (make-build "error_dynamic_selector_usage.cljs" "dev" {:dynamic-selector-usage :error} {:optimizations :whitespace})]))

; -- tests ------------------------------------------------------------------------------------------------------------------

(deftest exercise-all-builds
  (exercise-builds! all-builds))
