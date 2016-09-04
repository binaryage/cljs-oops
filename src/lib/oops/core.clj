(ns oops.core
  (:require [oops.schema :as schema]
            [oops.debug :refer [log]]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn gen-atomic-key-get [obj key]
  ; TODO: here implement optional safety-checking logic
  `(aget ~obj ~key))

(defn gen-atomic-key-set [obj key val]
  ; TODO: here implement optional safety-checking logic
  `(aset ~obj ~key ~val))

(defn gen-static-path-get [obj path]
  (if (empty? path)
    obj
    (gen-atomic-key-get (gen-static-path-get obj (butlast path)) (last path))))

(defn gen-dynamic-selector-get [obj selector]
  `(get-selector-dynamically ~obj ~@selector))

(defn gen-dynamic-selector-validation [selector]
  `(if-not (clojure.spec/valid? ::oops.sdefs/obj-selector ~selector)
     (throw (ex-info "Invalid dynamic selector"
                     {:explain (clojure.spec/explain-data ::oops.sdefs/obj-selector ~selector)}))))

(defn gen-dynamic-path-reduction [obj-sym path]
  {:pre [(symbol? obj-sym)]}
  `(reduce get-key-dynamically ~obj-sym ~path))

(defn gen-static-path-set [obj-sym path val]
  {:pre [(not (empty? path))
         (symbol? obj-sym)]}
  (let [parent-obj-path (butlast path)
        parent-obj-get-code (gen-static-path-get obj-sym parent-obj-path)
        key (last path)]
    (gen-atomic-key-set parent-obj-get-code key val)))

(defn gen-dynamic-selector-set [obj selector val]
  `(set-selector-dynamically ~obj ~selector ~val))

(defn gen-dynamic-path-set [obj-sym path val]
  {:pre [(symbol? obj-sym)]}
  (let [path-sym (gensym "path")
        key-sym (gensym "key")
        parent-obj-path-sym (gensym "parent-obj-path")
        parent-obj-sym (gensym "parent-obj")]
    `(let [~path-sym ~path
           ~parent-obj-path-sym (butlast ~path-sym)
           ~key-sym (last ~path-sym)
           ~parent-obj-sym ~(gen-dynamic-path-reduction obj-sym parent-obj-path-sym)]
       ~(gen-atomic-key-set parent-obj-sym key-sym val))))

; -- helper macros ----------------------------------------------------------------------------------------------------------

(defmacro coerce-key-dynamically-impl [key-sym]
  {:pre [(symbol? key-sym)]}
  `(name ~key-sym))

(defmacro build-path-dynamically-impl [selector-sym]
  {:pre [(symbol? selector-sym)]}
  `(if-not (sequential? ~selector-sym)
     (list (coerce-key-dynamically ~selector-sym))
     (let [reducer# (fn [path# key#]
                      (if (sequential? key#)
                        (concat path# (build-path-dynamically key#))
                        (concat path# [(coerce-key-dynamically key#)])))]
       (reduce reducer# (list) ~selector-sym))))

(defmacro get-key-dynamically-impl [obj-sym key-sym]
  {:pre [(symbol? obj-sym)
         (symbol? key-sym)]}
  (gen-atomic-key-get obj-sym key-sym))

(defmacro get-selector-dynamically-impl [obj-sym selector-sym]
  {:pre [(symbol? obj-sym)
         (symbol? selector-sym)]}
  `(do
     ~(gen-dynamic-selector-validation selector-sym)
     ~(gen-dynamic-path-reduction obj-sym `(build-path-dynamically ~selector-sym))))

(defmacro set-selector-dynamically-impl [obj-sym selector-sym val-sym]
  {:pre [(symbol? obj-sym)
         (symbol? selector-sym)
         (symbol? val-sym)]}
  `(do
     ~(gen-dynamic-selector-validation selector-sym)
     ~(gen-dynamic-path-set obj-sym `(build-path-dynamically ~selector-sym) val-sym)))

; -- public macros ----------------------------------------------------------------------------------------------------------

(defmacro oget [obj & selector]
  (let [path (schema/selector->path selector)]
    (if-not (= :invalid-path path)
      (gen-static-path-get obj path)
      (gen-dynamic-selector-get obj selector))))

(defmacro oset! [obj selector val]
  (let [obj-sym (gensym "obj")
        path (schema/selector->path selector)
        set-code (if-not (= :invalid-path path)
                   (gen-static-path-set obj-sym path val)
                   (gen-dynamic-selector-set obj-sym selector val))]
    `(let [~obj-sym ~obj]
       ~set-code
       ~obj-sym)))

(defmacro ocall [obj name & params]
  (let [obj-sym (gensym "obj")]
    `(let [~obj-sym ~obj]
       (.call (goog.object/get ~obj-sym ~name) ~obj-sym ~@params))))

(defmacro oapply [o name param-coll]
  (let [obj-sym (gensym "obj")]
    `(let [~obj-sym ~o]
       (.apply (goog.object/get ~obj-sym ~name) ~obj-sym (into-array ~param-coll)))))

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [& args]
  `(ocall ~@args))

(defmacro oapply!
  "This macro is identical to ocall, use it if you want to express a side-effecting invocation."
  [& args]
  `(oapply ~@args))
