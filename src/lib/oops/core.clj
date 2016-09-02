(ns oops.core)

(defmacro oget
  ([o k1]
   (if (sequential? k1)
     `(oget ~o ~@k1)
     `(goog.object/get ~o ~k1)))
  ([o k1 k2] `(when-let [o# (goog.object/get ~o ~k1)]
                (goog.object/get o# ~k2)))
  ([o k1 k2 & ks] `(when-let [o# (goog.object/get ~o ~k1)]
                     (oget o# ~k2 ~@ks))))

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
