(ns oops.core
  "Public macros to be consumed via core.cljs."
  (:refer-clojure :exclude [gensym])
  (:require [clojure.spec :as s]
            [oops.codegen :refer [gen-oget gen-oset gen-ocall gen-oapply]]
            [oops.compiler :refer [with-compiler-context! with-suppressed-reporting!]]))

; -- core macros ------------------------------------------------------------------------------------------------------------

(defmacro oget [obj & selector]
  (with-compiler-context! &form &env
    (gen-oget obj selector)))

(defmacro oget+ [obj & selector]
  (with-compiler-context! &form &env
    (with-suppressed-reporting! :dynamic-selector-usage
      (gen-oget obj selector))))

(defmacro oset! [obj & selector+val]
  (with-compiler-context! &form &env
    (gen-oset obj selector+val)))

(defmacro oset!+ [obj & selector+val]
  (with-compiler-context! &form &env
    (with-suppressed-reporting! :dynamic-selector-usage
      (gen-oset obj selector+val))))

(defmacro ocall [obj selector & args]
  (with-compiler-context! &form &env
    (gen-ocall obj selector args)))

(defmacro ocall+ [obj selector & args]
  (with-compiler-context! &form &env
    (with-suppressed-reporting! :dynamic-selector-usage
      (gen-ocall obj selector args))))

(defmacro oapply [obj & selector+args]
  (with-compiler-context! &form &env
    (gen-oapply obj selector+args)))

(defmacro oapply+ [obj & selector+args]
  (with-compiler-context! &form &env
    (with-suppressed-reporting! :dynamic-selector-usage
      (gen-oapply obj selector+args))))

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-compiler-context! &form &env
    (gen-ocall obj selector args)))

(defmacro ocall!+
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [obj selector & args]
  (with-compiler-context! &form &env
    (with-suppressed-reporting! :dynamic-selector-usage
      (gen-ocall obj selector args))))

(defmacro oapply!
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj & selector+args]
  (with-compiler-context! &form &env
    (gen-oapply obj selector+args)))

(defmacro oapply!+
  "This macro is identical to oapply, use it if you want to express a side-effecting call."
  [obj & selector+args]
  (with-compiler-context! &form &env
    (with-suppressed-reporting! :dynamic-selector-usage
      (gen-oapply obj selector+args))))

; -- convenience g-macros ---------------------------------------------------------------------------------------------------

(defmacro gget [& args]
  `(oops.core/oget js/goog.global ~@args))

(defmacro gget+ [& args]
  `(oops.core/oget+ js/goog.global ~@args))

(defmacro gset! [& args]
  `(oops.core/oset! js/goog.global ~@args))

(defmacro gset!+ [& args]
  `(oops.core/oset!+ js/goog.global ~@args))

(defmacro gcall [& args]
  `(oops.core/ocall js/goog.global ~@args))

(defmacro gcall+ [& args]
  `(oops.core/ocall+ js/goog.global ~@args))

(defmacro gapply [& args]
  `(oops.core/oapply js/goog.global ~@args))

(defmacro gapply+ [& args]
  `(oops.core/oapply+ js/goog.global ~@args))

(defmacro gcall! [& args]
  `(oops.core/ocall! js/goog.global ~@args))

(defmacro gcall!+ [& args]
  `(oops.core/ocall!+ js/goog.global ~@args))

(defmacro gapply! [& args]
  `(oops.core/oapply! js/goog.global ~@args))

(defmacro gapply!+ [& args]
  `(oops.core/oapply!+ js/goog.global ~@args))

; -- specs for our macro apis -----------------------------------------------------------------------------------------------
;
; This is not much useful because we cannot reason about macro args much,
; but I include it because it is catching some edge cases
; and there is a room for possible further refinements.
; Additionally we do ad-hoc validations inside our macros.

(defn anything? [_] true)                                                                                                     ; TODO: use any? when we drop Clojure 1.8 support

(defmacro api-spec [obj? args]
  `(s/fspec :args (s/cat ~@(if (= obj? 1) [:obj anything?])
                         ~@(if (symbol? args)
                             (var-get (resolve args))
                             args))
            :ret anything?))

(def oget-args [:selector (s/* anything?)])
(def oget-api (api-spec 1 oget-args))
(def gget-api (api-spec 0 oget-args))

(def oset-args [:selector (s/+ anything?) :val anything?])
(def oset-api (api-spec 1 oset-args))
(def gset-api (api-spec 0 oset-args))

(def ocall-args [:selector anything? :args (s/* anything?)])
(def ocall-api (api-spec 1 ocall-args))
(def gcall-api (api-spec 0 ocall-args))

(def oapply-args [:selector (s/+ anything?) :args sequential?])
(def oapply-api (api-spec 1 oapply-args))
(def gapply-api (api-spec 0 oapply-args))

; --- o-api

(s/def oget oget-api)
(s/def oget+ oget-api)

(s/def oset! oset-api)
(s/def oset!+ oset-api)

(s/def ocall ocall-api)
(s/def ocall+ ocall-api)
(s/def ocall! ocall-api)
(s/def ocall!+ ocall-api)

(s/def oapply oapply-api)
(s/def oapply+ oapply-api)
(s/def oapply! oapply-api)
(s/def oapply!+ oapply-api)

; --- g-api

(s/def gget gget-api)
(s/def gget+ gget-api)

(s/def gset! gset-api)
(s/def gset!+ gset-api)

(s/def gcall gcall-api)
(s/def gcall+ gcall-api)
(s/def gcall! gcall-api)
(s/def gcall!+ gcall-api)

(s/def gapply gapply-api)
(s/def gapply+ gapply-api)
(s/def gapply! gapply-api)
(s/def gapply!+ gapply-api)
