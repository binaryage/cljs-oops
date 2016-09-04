(ns oops.core
  (:require [oops.schema :as schema]
            [oops.debug :refer [log]]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn gen-atomic-key-get [o key]
  ; TODO: here implement optional safety-checking logic
  `(aget ~o ~key))

(defn gen-atomic-key-set [o key val]
  ; TODO: here implement optional safety-checking logic
  `(aset ~o ~key ~val))

(defn gen-static-path-get [o path]
  (if (empty? path)
    o
    (gen-atomic-key-get (gen-static-path-get o (butlast path)) (last path))))

(defn gen-dynamic-selector-get [o selector]
  `(get-selector-dynamically ~o ~@selector))

(defn gen-dynamic-selector-validation [selector]
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector)
     (throw (ex-info "Invalid dynamic selector"
                     {:explain (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector)}))))

(defn gen-dynamic-path-reduction [o path]
  {:pre [(symbol? o)]}
  `(reduce get-key-dynamically ~o ~path))

(defn gen-static-path-set [obj path val]
  {:pre [(not (empty? path))
         (symbol? obj)]}
  (let [parent-obj-path (butlast path)
        parent-obj-get-code (gen-static-path-get obj parent-obj-path)
        key (last path)]
    (gen-atomic-key-set parent-obj-get-code key val)))

(defn gen-dynamic-selector-set [o selector val]
  `(set-selector-dynamically ~o ~selector ~val))

(defn gen-dynamic-path-set [o path val]
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
       ~(gen-atomic-key-set parent-obj-sym key-sym val))))

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

(defmacro get-key-dynamically-impl [o key]
  {:pre [(symbol? o)
         (symbol? key)]}
  (gen-atomic-key-get o key))

(defmacro get-selector-dynamically-impl [o selector]
  {:pre [(symbol? o)
         (symbol? selector)]}
  `(do
     ~(gen-dynamic-selector-validation selector)
     ~(gen-dynamic-path-reduction o `(build-path-dynamically ~selector))))

(defmacro set-selector-dynamically-impl [o selector val]
  {:pre [(symbol? o)
         (symbol? selector)
         (symbol? val)]}
  `(do
     ~(gen-dynamic-selector-validation selector)
     ~(gen-dynamic-path-set o `(build-path-dynamically ~selector) val)))

; -- public macros ----------------------------------------------------------------------------------------------------------

(defmacro oget [o & selector]
  (let [path (schema/selector->path selector)]
    (if-not (= :invalid-path path)
      (gen-static-path-get o path)
      (gen-dynamic-selector-get o selector))))

(defmacro oset! [o selector val]
  (let [o-sym (gensym "o")
        path (schema/selector->path selector)
        set-code (if-not (= :invalid-path path)
                   (gen-static-path-set o-sym path val)
                   (gen-dynamic-selector-set o-sym selector val))]
    `(let [~o-sym ~o]
       ~set-code
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
