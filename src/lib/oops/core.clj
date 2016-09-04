(ns oops.core
  (:require [oops.schema :as schema]
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
  {:pre [(symbol? o)
         (symbol? key)]}
  `(if (sequential? ~key)
     (apply dynamic-selector-fetch ~o ~key)
     (do
       (assert (or (string? ~key)) (keyword? ~key))                                                                           ; TODO: this should be validated by specs on cljs side
       ~(gen-key-fetch o `(name ~key)))))

(defn gen-dynamic-selector-validation [selector]
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector)
     (throw (ex-info "Invalid dynamic selector"
                     {:explain (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector)}))))

(defn gen-dynamic-selector-reduction [o selector]
  {:pre [(symbol? o)
         (symbol? selector)]}
  `(reduce dynamic-selector-reducer ~o ~selector))

(defmacro dynamic-selector-reducer-impl [o selector-segment]
  {:pre [(symbol? o)
         (symbol? selector-segment)]}
  `(dynamic-key-fetch ~o ~selector-segment))

(defmacro dynamic-selector-fetch-impl [o selector]
  {:pre [(symbol? o)
         (symbol? selector)]}
  `(do
     ~(gen-dynamic-selector-validation selector)
     ~(gen-dynamic-selector-reduction o selector)))

(defmacro oget
  ([o & selector]
   (let [path (schema/selector->path selector)]
     (if-not (= :invalid-path path)
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
