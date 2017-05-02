(ns oops.sdefs
  "Spec definitions for our static code."
  (:require [clojure.spec.alpha :as s]
            [oops.constants :refer [dot-access soft-access punch-access]]))

; --- specs -----------------------------------------------------------------------------------------------------------------

(s/def ::obj-key (s/or :string string?
                       :keyword keyword?))
(s/def ::obj-selector (s/or :key ::obj-key
                            :selector (s/* ::obj-selector)))

(s/def ::obj-path-key string?)
(s/def ::obj-path-mode #{dot-access soft-access punch-access})
(s/def ::obj-path-item (s/tuple ::obj-path-mode ::obj-path-key))
(s/def ::obj-path (s/* ::obj-path-item))
