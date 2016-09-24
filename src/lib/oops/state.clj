(ns oops.state
  "Here we gather compile-time state.")

; we want to isolate all state into this namespace
; please note that we have two distinct environments:
;   1. compiler state (at compile time during macro expansion)
;   2. runtime state (in the browser)

(def ^:dynamic *invocation-form* nil)
(def ^:dynamic *invocation-env* nil)
(def ^:dynamic *invocation-opts* nil)
