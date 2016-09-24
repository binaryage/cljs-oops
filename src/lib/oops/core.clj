(ns oops.core
  (:refer-clojure :exclude [gensym])
  (:require [clojure.spec :as s]
            [oops.codegen :refer :all]
            [oops.compiler :refer [with-compiler-context! with-compiler-opts!]]
            [oops.debug :refer [log debug-assert]]))

; -- core macros ------------------------------------------------------------------------------------------------------------

(defmacro oget [obj & selector]
  (with-compiler-context! &form &env
    (gen-oget obj selector)))

(defmacro oget+ [obj & selector]
  (with-compiler-context! &form &env
    (with-compiler-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (gen-oget obj selector))))

(defmacro oset! [obj & selector+val]
  (with-compiler-context! &form &env
    (gen-oset obj selector+val)))

(defmacro oset!+ [obj & selector+val]
  (with-compiler-context! &form &env
    (with-compiler-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (gen-oset obj selector+val))))

(defmacro ocall [obj selector & args]
  (with-compiler-context! &form &env
    (gen-ocall obj selector args)))

(defmacro ocall+ [obj selector & args]
  (with-compiler-context! &form &env
    (with-compiler-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (gen-ocall obj selector args))))

(defmacro oapply [obj & selector+args]
  (with-compiler-context! &form &env
    (gen-oapply obj selector+args)))

(defmacro oapply+ [obj & selector+args]
  (with-compiler-context! &form &env
    (with-compiler-opts! {:suppress-reporting #{:dynamic-selector-usage}}
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
    (with-compiler-opts! {:suppress-reporting #{:dynamic-selector-usage}}
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
    (with-compiler-opts! {:suppress-reporting #{:dynamic-selector-usage}}
      (gen-oapply obj selector+args))))

; -- specs for our macro apis -----------------------------------------------------------------------------------------------
;
; This is not much useful because we cannot reason about macro args much,
; but I include it because it is catching some edge cases
; and there is a room for possible further refinements.
; Additionally we do ad-hoc validations inside our macros.

(defn anything? [_] true)                                                                                                     ; TODO: use any? when we drop Clojure 1.8 support

(def oget-api (s/fspec :args (s/cat :obj anything?
                                    :selector (s/* anything?))
                       :ret anything?))

(def oset-api (s/fspec :args (s/cat :obj anything?
                                    :selector (s/+ anything?)
                                    :val anything?)
                       :ret anything?))

(def ocall-api (s/fspec :args (s/cat :obj anything?
                                     :selector anything?
                                     :args (s/* anything?))
                        :ret anything?))

(def oapply-api (s/fspec :args (s/cat :obj anything?
                                      :selector (s/+ anything?)
                                      :args sequential?)
                         :ret anything?))

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
