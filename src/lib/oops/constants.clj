(ns oops.constants)

(def ^:const dot-access 0)
(def ^:const soft-access 1)
(def ^:const punch-access 2)

(def ^:const op-get 0)
(def ^:const op-set 1)

; --- macros for cljs -------------------------------------------------------------------------------------------------------

(defmacro get-dot-access [] dot-access)
(defmacro get-soft-access [] soft-access)
(defmacro get-punch-access [] punch-access)

; -- constants for runtime state slots --------------------------------------------------------------------------------------

(defmacro target-object-idx [] 0)
(defmacro call-site-error-idx [] 1)
(defmacro console-reporter-idx [] 2)
(defmacro error-reported-idx [] 3)
(defmacro key-path-idx [] 4)
(defmacro last-access-modifier-idx [] 5)

(defmacro gen-op-get [] op-get)
(defmacro gen-op-set [] op-set)
