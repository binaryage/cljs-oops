(ns oops.core
  (:require [clojure.spec :as s]
            [oops.schema :as schema]
            [oops.debug :refer [log]]))

(defn gen-key-fetch [o key]
  ; TODO: here implement optional safety-checking logic
  `(aget ~o ~key))

(defn gen-static-path-fetch [o path]
  (if (empty? path)
    o
    (gen-key-fetch (gen-static-path-fetch o (butlast path)) (last path))))

(defn gen-dynamic-selector-fetch [o selector]
  `(dynamic-selector-fetch ~o ~@selector))

(defmacro dynamic-key-fetch-impl [o key]
  (let [o-sym (gensym)
        key-sym (gensym)]
    `(let [~o-sym ~o
           ~key-sym ~key]
       (if (sequential? ~key-sym)
         (apply dynamic-selector-fetch ~o-sym ~key-sym)
         (do
           (assert (or (string? ~key-sym)) (keyword? ~key-sym))                                                               ; TODO: this should be validated by specs on cljs side
           ~(gen-key-fetch o-sym `(name ~key-sym)))))))

(defmacro dynamic-selector-fetch-impl [o selector]
  `(let [selector# ~selector
         o# ~o]
     (if (empty? selector#)
       o#
       (let [next-o# (dynamic-key-fetch o# (first selector#))
             remaining-selector# (rest selector#)]
         (recur next-o# remaining-selector#)))))

(defmacro oget
  ([o & selector]
   (let [path (schema/selector->path selector)]
     (if-not (= ::schema/invalid-path path)
       (gen-static-path-fetch o path)
       (gen-dynamic-selector-fetch o selector)))))

(defmacro oset! [o ks val]
  (let [keys (butlast ks)
        obj-sym (gensym)]
    `(let [~obj-sym ~o
           target# ~(if (seq keys) `(oget ~obj-sym ~@keys) obj-sym)]
       (assert target# (str "unable to locate object path " ~keys " in " ~obj-sym))
       (goog.object/set target# (last ~ks) ~val)
       ~obj-sym)))

(defmacro ocall [o name & params]
  `(let [o# ~o]
     (.call (goog.object/get o# ~name) o# ~@params)))

(defmacro oapply [o name param-coll]
  `(let [o# ~o]
     (.apply (goog.object/get o# ~name) o# (into-array ~param-coll))))

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [& args]
  `(ocall ~@args))

(defmacro oapply!
  "This macro is identical to ocall, use it if you want to express a side-effecting invocation."
  [& args]
  `(oapply ~@args))
