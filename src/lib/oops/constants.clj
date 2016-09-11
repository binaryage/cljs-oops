(ns oops.constants)

(def ^:const dot-access 0)
(def ^:const soft-access 1)
(def ^:const punch-access 2)

; --- macros for cljs -------------------------------------------------------------------------------------------------------

(defmacro get-dot-access []
  dot-access)

(defmacro get-soft-access []
  soft-access)

(defmacro get-punch-access []
  punch-access)
