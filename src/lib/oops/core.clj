(ns oops.core
  (:require [oops.schema :as schema]
            [oops.debug :refer [log]]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn gen-atomic-key-fetch [o key]
  ; TODO: here implement optional safety-checking logic
  `(aget ~o ~key))

(defn gen-atomic-key-store [o key val]
  ; TODO: here implement optional safety-checking logic
  `(aset ~o ~key ~val))

(defn gen-static-path-fetch [o path]
  (if (empty? path)
    o
    (gen-atomic-key-fetch (gen-static-path-fetch o (butlast path)) (last path))))

(defn gen-dynamic-selector-fetch [o selector]
  `(fetch-selector-dynamically ~o ~@selector))

(defn gen-dynamic-selector-validation [selector]
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector)
     (throw (ex-info "Invalid dynamic selector"
                     {:explain (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector)}))))

(defn gen-dynamic-path-reduction [o path]
  {:pre [(symbol? o)]}
  `(reduce fetch-key-dynamically ~o ~path))

(defn gen-static-path-store [obj path val]
  {:pre [(not (empty? path))
         (symbol? obj)]}
  (let [parent-obj-path (butlast path)
        parent-obj-fetch-code (gen-static-path-fetch obj parent-obj-path)
        key (last path)]
    (gen-atomic-key-store parent-obj-fetch-code key val)))

(defn gen-dynamic-selector-store [o selector val]
  `(store-selector-dynamically ~o ~selector ~val))

(defn gen-dynamic-path-store [o path val]
  {:pre [(symbol? o)
         (symbol? val)]}
  (let [path-sym (gensym "path")
        key-sym (gensym "key")
        parent-obj-path-sym (gensym "parent-obj-path")
        parent-obj-sym (gensym "parent-obj")]
    `(let [~path-sym ~path
           ~parent-obj-path-sym (butlast ~path-sym)
           ~key-sym (last ~path-sym)
           ~parent-obj-sym ~(gen-dynamic-path-reduction o parent-obj-path-sym)]
       ~(gen-atomic-key-store parent-obj-sym key-sym val))))

; -- helper macros ----------------------------------------------------------------------------------------------------------

(defmacro coerce-key-dynamically-impl [key]
  {:pre [(symbol? key)]}
  `(name ~key))

(defmacro build-path-dynamically-impl [selector]
  {:pre [(symbol? selector)]}
  `(if-not (sequential? ~selector)
     (list (coerce-key-dynamically ~selector))
     (let [reducer# (fn [path# key#]
                      (if (sequential? key#)
                        (concat path# (build-path-dynamically key#))
                        (concat path# [(coerce-key-dynamically key#)])))]
       (reduce reducer# (list) ~selector))))

(defmacro fetch-key-dynamically-impl [o key]
  {:pre [(symbol? o)
         (symbol? key)]}
  (gen-atomic-key-fetch o key))

(defmacro fetch-selector-dynamically-impl [o selector]
  {:pre [(symbol? o)
         (symbol? selector)]}
  `(do
     ~(gen-dynamic-selector-validation selector)
     ~(gen-dynamic-path-reduction o `(build-path-dynamically ~selector))))

(defmacro store-selector-dynamically-impl [o selector val]
  {:pre [(symbol? o)
         (symbol? selector)
         (symbol? val)]}
  `(do
     ~(gen-dynamic-selector-validation selector)
     ~(gen-dynamic-path-store o `(build-path-dynamically ~selector) val)))

; -- public macros ----------------------------------------------------------------------------------------------------------

(defmacro oget [o & selector]
  (let [path (schema/selector->path selector)]
    (if-not (= :invalid-path path)
      (gen-static-path-fetch o path)
      (gen-dynamic-selector-fetch o selector))))

(defmacro oset! [o selector val]
  (let [o-sym (gensym "o")
        path (schema/selector->path selector)
        store-code (if-not (= :invalid-path path)
                     (gen-static-path-store o-sym path val)
                     (gen-dynamic-selector-store o-sym selector val))]
    `(let [~o-sym ~o]
       ~store-code
       ~o-sym)))

(defmacro ocall [o name & params]
  `(let [o# ~o]
     (.call (goog.object/get o# ~name) o# ~@params)))

(defmacro oapply [o name param-coll]
  `(let [o# ~o]
     (.apply (goog.object/get o# ~name) o# (into-array ~param-coll))))

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [& args]
  `(ocall ~@args))

(defmacro oapply!
  "This macro is identical to ocall, use it if you want to express a side-effecting invocation."
  [& args]
  `(oapply ~@args))
