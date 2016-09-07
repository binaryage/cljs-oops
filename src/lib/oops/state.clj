(ns oops.state)

; we want to isolate all state into this namespace
; please note that we have two distinct environments:
;   1. compiler state (at compile time during macro expansion)
;   2. runtime state (in the browser)

(def ^:dynamic *invoked-form* nil)
(def ^:dynamic *invoked-env* nil)
(def ^:dynamic *invoked-opts* nil)
