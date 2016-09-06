(ns oops.sdefs
  (:require [clojure.spec :as s]))

; --- specs -----------------------------------------------------------------------------------------------------------------

(s/def ::obj-key (s/or :string string? :keyword keyword?))
(s/def ::obj-selector (s/or :key ::obj-key :selector (s/* ::obj-selector)))

; note: in CLJS paths are native JS arrays of strings (optimization)
(s/def ::obj-path (s/or :invalid #{:invalid-path} :path #?(:clj  (s/* string?)
                                                           :cljs (s/and array? #(every? string? %)))))                        ; TODO: s/* does not seem to walk js arrays, is there a better way how to express this?
