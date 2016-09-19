(ns oops.state)

; we want to isolate all state into this namespace
; please note that we have two distinct environments:
;   1. compiler state (at compile time during macro expansion)
;   2. runtime state (in the browser)

(def ^:dynamic *invocation-form* nil)
(def ^:dynamic *invocation-env* nil)
(def ^:dynamic *invocation-opts* nil)

; -- constants for runtime state slots --------------------------------------------------------------------------------------

(defmacro state-console-reporter-idx []
  0)

(defmacro state-error-reported?-idx []
  1)

(defmacro state-current-key-path-idx []
  2)

(defmacro state-current-target-object-idx []
  3)

(defmacro state-last-access-modifier-idx []
  4)
