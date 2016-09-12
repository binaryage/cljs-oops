(ns oops.sdefs
  (:require-macros [oops.constants :refer [get-dot-access get-soft-access get-punch-access]])
  (:require [clojure.spec :as s]))

; --- specs -----------------------------------------------------------------------------------------------------------------

(s/def ::obj-key (s/or :string string? :keyword keyword?))
(s/def ::obj-array-selector (s/and array? (fn [arr]
                                            (every? #(s/valid? ::obj-selector %) arr))))
(s/def ::obj-selector (s/or :key ::obj-key :selector (s/* ::obj-selector) :array ::obj-array-selector))

(s/def ::obj-path-item (s/tuple #{(get-dot-access) (get-soft-access) (get-punch-access)} string?))
(s/def ::obj-path (s/and array? (fn [arr]
                                  (let [pairs (map vec (partition-all 2 arr))]
                                    (s/valid? (s/* ::obj-path-item) pairs)))))
