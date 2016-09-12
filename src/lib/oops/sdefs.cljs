(ns oops.sdefs
  (:require-macros [oops.constants :refer [get-dot-access get-soft-access get-punch-access]])
  (:require [clojure.spec :as s]
            [clojure.spec :as s]))

; --- specs -----------------------------------------------------------------------------------------------------------------

(s/def ::obj-key (s/or :string string? :keyword keyword?))
(s/def ::obj-selector (s/or :key ::obj-key :selector (s/* ::obj-selector)))

(s/def ::obj-path-item (s/tuple #{(get-dot-access) (get-soft-access) (get-punch-access)} string?))
; TODO: s/* does not seem to walk js arrays, is there a better way how to express this?
(s/def ::obj-path (s/and array? (fn [arr]
                                  (let [coll (map vec (partition-all 2 arr))]
                                    (s/valid? (s/* ::obj-path-item) coll)))))
