(ns oops.sdefs
  (:require [clojure.spec :as s]
            [oops.constants :refer [dot-access soft-access punch-access]]))

; --- specs -----------------------------------------------------------------------------------------------------------------

(s/def ::obj-key (s/or :string string? :keyword keyword?))
(s/def ::obj-selector (s/or :key ::obj-key :selector (s/* ::obj-selector)))

(s/def ::obj-path-item (s/tuple #{dot-access soft-access punch-access} string?))
(s/def ::obj-path (s/* ::obj-path-item))
