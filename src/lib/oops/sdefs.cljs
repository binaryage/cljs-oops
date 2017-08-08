(ns oops.sdefs
  "Spec definitions for our dynamic code."
  (:require-macros [oops.constants :refer [get-dot-access get-soft-access get-punch-access]]
                   [oops.spec :refer [native-array-aware-*]])
  (:require [clojure.spec.alpha :as s]))

; --- specs -----------------------------------------------------------------------------------------------------------------

(s/def ::obj-key (s/or :string string?
                       :keyword keyword?))
(s/def ::obj-selector (s/or :key ::obj-key
                            :selector (native-array-aware-* ::obj-selector)))

; note: ::obj-path is a native array for performance reasons
;       it is not a sequence of tuples, but it is flat sequence of pairs mode-key
;       for an example clj path [[0 "key1"] [0 "key2"] ...]
;       the equivalent cljs path is [0 "key1" 0 "key2"]
(s/def ::obj-path-mode #{(get-dot-access) (get-soft-access) (get-punch-access)})
(s/def ::obj-path-key string?)
(s/def ::obj-path-item (s/tuple ::obj-path-mode ::obj-path-key))
(s/def ::obj-path (s/and array? (fn [arr]
                                  (let [pairs (map vec (partition-all 2 arr))]
                                    (s/valid? (s/* ::obj-path-item) pairs)))))
