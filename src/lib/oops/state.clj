(ns oops.state)

; we want to isolate all state into this namespace
; please note that we have two distinct environments:
;   1. compiler state (at compile time during macro expansion)
;   2. runtime state (in the browser)

(def ^:dynamic *invocation-form* nil)
(def ^:dynamic *invocation-env* nil)
(def ^:dynamic *invocation-opts* nil)

; -- constants for runtime state slots --------------------------------------------------------------------------------------

(defmacro state-target-object-idx [] 0)
(defmacro state-call-site-error-idx [] 1)
(defmacro state-console-reporter-idx [] 2)
(defmacro state-error-reported?-idx [] 3)
(defmacro state-key-path-idx [] 4)
(defmacro state-last-access-modifier-idx [] 5)
