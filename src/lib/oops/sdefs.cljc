(ns oops.sdefs
  (:require [clojure.spec :as s]))

; --- specs -----------------------------------------------------------------------------------------------------------------

(s/def ::obj-key (s/or :string string? :keyword keyword?))
(s/def ::obj-selector (s/or :key ::obj-key :selector (s/* ::obj-selector)))
(s/def ::obj-path-segment string?)
(s/def ::obj-path (s/or :invalid #{:invalid-path} :path (s/* ::obj-path-segment)))
