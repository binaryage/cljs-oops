(ns oops.core
  (:require [oops.schema :as schema]
            [oops.debug :refer [log]]))

; -- helper code generators -------------------------------------------------------------------------------------------------

(defn gen-atomic-key-fetch [o key]
  ; TODO: here implement optional safety-checking logic
  `(aget ~o ~key))

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

(defn gen-dynamic-selector-reduction [o selector]
  {:pre [(symbol? o)
         (symbol? selector)]}
  `(let [reducer# (fn [o# key#]
                    (fetch-key-dynamically o# key#))]
     (reduce reducer# ~o ~selector)))

; -- helper macros ----------------------------------------------------------------------------------------------------------

(defmacro fetch-key-dynamically-impl [o key]
  {:pre [(symbol? o)
         (symbol? key)]}
  `(if (sequential? ~key)
     (apply fetch-selector-dynamically ~o ~key)
     ~(gen-atomic-key-fetch o `(coerce-key-dynamically ~key))))

(defmacro fetch-selector-dynamically-impl [o selector]
  {:pre [(symbol? o)
         (symbol? selector)]}
  `(do
     ~(gen-dynamic-selector-validation selector)
     ~(gen-dynamic-selector-reduction o selector)))

(defmacro coerce-key-dynamically-impl [key]
  {:pre [(symbol? key)]}
  `(name ~key))

; -- public macros ----------------------------------------------------------------------------------------------------------

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

; -- convenience macros -----------------------------------------------------------------------------------------------------

(defmacro ocall!
  "This macro is identical to ocall, use it if you want to express a side-effecting call."
  [& args]
  `(ocall ~@args))

(defmacro oapply!
  "This macro is identical to ocall, use it if you want to express a side-effecting invocation."
  [& args]
  `(oapply ~@args))
