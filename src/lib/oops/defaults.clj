(ns oops.defaults
  "Default configuration + specs."
  (:require [cljs.env]
            [oops.state]
            [clojure.spec.alpha :as s]))

(def config                                                                                                                   ; falsy below means 'nil' or 'false'
  {; -- compiler config -----------------------------------------------------------------------------------------------------
   :diagnostics                                true                                                                           ; #{true falsy}
   :key-get                                    :core                                                                          ; #{:core :goog}
   :key-set                                    :core                                                                          ; #{:core :goog}
   :strict-punching                            true                                                                           ; #{true falsy}
   :skip-config-validation                     false                                                                          ; #{true falsy}
   :macroexpand-selectors                      true                                                                           ; #{true falsy}

   ; compile-time warnings/errors
   :dynamic-selector-usage                     :warn                                                                          ; #{:error :warn falsy}
   :static-nil-target-object                   :warn                                                                          ; #{:error :warn falsy}
   :static-unexpected-empty-selector           :warn                                                                          ; #{:error :warn falsy}
   :static-unexpected-punching-selector        :warn                                                                          ; #{:error :warn falsy}
   :static-unexpected-soft-selector            :warn                                                                          ; #{:error :warn falsy}

   ; -- runtime config ------------------------------------------------------------------------------------------------------

   ; run-time warnings/errors
   :runtime-unexpected-object-value            :error                                                                         ; #{:error :warn falsy}
   :runtime-expected-function-value            :error                                                                         ; #{:error :warn falsy}
   :runtime-invalid-selector                   :error                                                                         ; #{:error :warn falsy}
   :runtime-missing-object-key                 :error                                                                         ; #{:error :warn falsy}
   :runtime-object-key-not-writable            :error                                                                         ; #{:error :warn falsy}
   :runtime-object-is-sealed                   :error                                                                         ; #{:error :warn falsy}
   :runtime-object-is-frozen                   :error                                                                         ; #{:error :warn falsy}
   :runtime-unexpected-empty-selector          :warn                                                                          ; #{:error :warn falsy}
   :runtime-unexpected-punching-selector       :warn                                                                          ; #{:error :warn falsy}
   :runtime-unexpected-soft-selector           :warn                                                                          ; #{:error :warn falsy}

   ; reporting modes
   :runtime-error-reporting                    :throw                                                                         ; #{:throw :console falsy}
   :runtime-warning-reporting                  :console                                                                       ; #{:throw :console falsy}

   :runtime-throw-errors-from-macro-call-sites true                                                                           ; #{true falsy}
   :runtime-child-factory                      :js-obj                                                                        ; #{:js-obj :js-array}
   :runtime-use-envelope                       true                                                                           ; #{true falsy}

   ; -- development ---------------------------------------------------------------------------------------------------------
   ; enable debug if you want to debug/hack oops itself
   :debug                                      false                                                                          ; #{true falsy}
   })

(def advanced-mode-config-overrides
  {:diagnostics false})

; -- config validation specs ------------------------------------------------------------------------------------------------

; please note that we want this code to be co-located with default config for easier maintenance
; but formally we want config spec to reside in oops.config namespace
(create-ns 'oops.config)
(alias 'config 'oops.config)

; -- config helpers ---------------------------------------------------------------------------------------------------------

(s/def ::config/boolish #(contains? #{true false nil} %))
(s/def ::config/key-impl #(contains? #{:goog :core} %))
(s/def ::config/message #(contains? #{:error :warn false nil} %))
(s/def ::config/reporting #(contains? #{:throw :console false nil} %))
(s/def ::config/child-factory #(contains? #{:js-obj :js-array} %))

; -- config keys ------------------------------------------------------------------------------------------------------------

(s/def ::config/diagnostics ::config/boolish)
(s/def ::config/key-get ::config/key-impl)
(s/def ::config/key-set ::config/key-impl)
(s/def ::config/strict-punching ::config/boolish)
(s/def ::config/skip-config-validation ::config/boolish)
(s/def ::config/macroexpand-selectors ::config/boolish)

(s/def ::config/dynamic-selector-usage ::config/message)
(s/def ::config/static-nil-target-object ::config/message)
(s/def ::config/static-unexpected-empty-selector ::config/message)
(s/def ::config/static-unexpected-punching-selector ::config/message)
(s/def ::config/static-unexpected-soft-selector ::config/message)

(s/def ::config/runtime-unexpected-object-value ::config/message)
(s/def ::config/runtime-expected-function-value ::config/message)
(s/def ::config/runtime-invalid-selector ::config/message)
(s/def ::config/runtime-missing-object-key ::config/message)
(s/def ::config/runtime-object-key-not-writable ::config/message)
(s/def ::config/runtime-object-is-sealed ::config/message)
(s/def ::config/runtime-object-is-frozen ::config/message)
(s/def ::config/runtime-unexpected-empty-selector ::config/message)
(s/def ::config/runtime-unexpected-punching-selector ::config/message)
(s/def ::config/runtime-unexpected-soft-selector ::config/message)

(s/def ::config/runtime-error-reporting ::config/reporting)
(s/def ::config/runtime-warning-reporting ::config/reporting)

(s/def ::config/runtime-throw-errors-from-macro-call-sites ::config/boolish)
(s/def ::config/runtime-child-factory ::config/child-factory)
(s/def ::config/runtime-use-envelope ::config/boolish)

(s/def ::config/debug ::config/boolish)

; -- config map -------------------------------------------------------------------------------------------------------------

(s/def ::config/config
  (s/keys :req-un [::config/diagnostics
                   ::config/key-get
                   ::config/key-set
                   ::config/strict-punching
                   ::config/skip-config-validation
                   ::config/macroexpand-selectors
                   ::config/dynamic-selector-usage
                   ::config/static-nil-target-object
                   ::config/static-unexpected-empty-selector
                   ::config/static-unexpected-punching-selector
                   ::config/static-unexpected-soft-selector
                   ::config/runtime-unexpected-object-value
                   ::config/runtime-invalid-selector
                   ::config/runtime-missing-object-key
                   ::config/runtime-object-key-not-writable
                   ::config/runtime-object-is-sealed
                   ::config/runtime-object-is-frozen
                   ::config/runtime-unexpected-empty-selector
                   ::config/runtime-unexpected-punching-selector
                   ::config/runtime-unexpected-soft-selector
                   ::config/runtime-error-reporting
                   ::config/runtime-warning-reporting
                   ::config/runtime-throw-errors-from-macro-call-sites
                   ::config/runtime-child-factory
                   ::config/runtime-use-envelope
                   ::config/debug]))
