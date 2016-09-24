(ns oops.defaults
  "Default configuration + specs."
  (:require [cljs.env]
            [oops.state]
            [clojure.spec :as s]))

(def config                                                                                                                   ; falsy below means 'nil' or 'false'
  {; -- compiler config -----------------------------------------------------------------------------------------------------
   :diagnostics                                true                                                                           ; #{true falsy}
   :key-get                                    :goog                                                                          ; #{:core :goog}
   :key-set                                    :goog                                                                          ; #{:core :goog}
   :strict-punching                            true                                                                           ; #{true falsy}
   :skip-config-validation                     false                                                                          ; #{true falsy}
   :macroexpand-selectors                      true                                                                           ; #{true falsy}

   ; compile-time warnings/errors
   :dynamic-selector-usage                     :warn                                                                          ; #{:error :warn falsy}
   :static-nil-target-object                   :warn                                                                          ; #{:error :warn falsy}
   :static-empty-selector-access               :warn                                                                          ; #{:error :warn falsy}
   :static-unexpected-punching-access          :warn                                                                          ; #{:error :warn falsy}
   :static-unexpected-soft-access              :warn                                                                          ; #{:error :warn falsy}

   ; -- runtime config ------------------------------------------------------------------------------------------------------

   ; run-time warnings/errors
   :runtime-unexpected-object-value            :error                                                                         ; #{:error :warn falsy}
   :runtime-expected-function-value            :error                                                                         ; #{:error :warn falsy}
   :runtime-invalid-selector                   :error                                                                         ; #{:error :warn falsy}
   :runtime-missing-object-key                 :error                                                                         ; #{:error :warn falsy}
   :runtime-empty-selector-access              :warn                                                                          ; #{:error :warn falsy}

   ; reporting modes
   :runtime-error-reporting                    :throw                                                                         ; #{:throw :console falsy}
   :runtime-warning-reporting                  :console                                                                       ; #{:throw :console falsy}

   :runtime-throw-errors-from-macro-call-sites true                                                                           ; #{true falsy}
   :runtime-child-factory                      :js-obj                                                                        ; #{:js-obj :js-array}

   ; -- development ---------------------------------------------------------------------------------------------------------
   ; enable debug if you want to debug/hack oops itself
   :debug                                      false                                                                          ; #{true falsy}
   })

(def advanced-mode-config-overrides
  {:diagnostics false})

; -- config validation specs ------------------------------------------------------------------------------------------------

; please note that we want this code to be co-located with default config for easier maintenance
; but formally we want config spec to reside in oops.config namespace
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
(s/def ::config/static-empty-selector-access ::config/message)

(s/def ::config/runtime-unexpected-object-value ::config/message)
(s/def ::config/runtime-expected-function-value ::config/message)
(s/def ::config/runtime-invalid-selector ::config/message)
(s/def ::config/runtime-missing-object-key ::config/message)
(s/def ::config/runtime-empty-selector-access ::config/message)

(s/def ::config/runtime-error-reporting ::config/reporting)
(s/def ::config/runtime-warning-reporting ::config/reporting)

(s/def ::config/runtime-throw-errors-from-macro-call-sites ::config/boolish)
(s/def ::config/runtime-child-factory ::config/child-factory)

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
                   ::config/static-empty-selector-access
                   ::config/runtime-unexpected-object-value
                   ::config/runtime-invalid-selector
                   ::config/runtime-missing-object-key
                   ::config/runtime-empty-selector-access
                   ::config/runtime-error-reporting
                   ::config/runtime-warning-reporting
                   ::config/runtime-throw-errors-from-macro-call-sites
                   ::config/runtime-child-factory
                   ::config/debug]))
