(ns oops.defaults
  (:require [cljs.env]
            [oops.state]))

(def config                                                                                                                   ; falsy below means 'nil' or 'false'
  {; -- compiler config -----------------------------------------------------------------------------------------------------
   :diagnostics                     true                                                                                      ; #{true falsy}
   :key-get                         :core                                                                                     ; #{:core :goog}
   :key-set                         :core                                                                                     ; #{:core :goog}
   :strict-punching                 true                                                                                      ; #{true falsy}

   ; compile-time warnings/errors
   :dynamic-selector-usage          :warn                                                                                     ; #{:error :warn falsy}
   :static-nil-target-object        :warn                                                                                     ; #{:error :warn falsy}
   :static-empty-selector-access    :warn                                                                                     ; #{:error :warn falsy}

   ; -- runtime config ------------------------------------------------------------------------------------------------------

   ; run-time warnings/errors
   :runtime-unexpected-object-value :error                                                                                    ; #{:error :warn falsy}
   :runtime-invalid-selector        :error                                                                                    ; #{:error :warn falsy}
   :runtime-missing-object-key      :error                                                                                    ; #{:error :warn falsy}
   :runtime-empty-selector-access   :warn                                                                                     ; #{:error :warn falsy}

   ; reporting modes
   :runtime-error-reporting         :throw                                                                                    ; #{:throw :console falsy}
   :runtime-warning-reporting       :console                                                                                  ; #{:throw :console falsy}

   :runtime-child-factory           :js-obj                                                                                   ; #{:js-obj :js-array}

   ; -- development ---------------------------------------------------------------------------------------------------------
   ; enable debug if you want to debug/hack oops itself
   :debug                           false                                                                                     ; #{true falsy}
   })

(def advanced-mode-config-overrides
  {:diagnostics false})
