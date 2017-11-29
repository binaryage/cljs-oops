(ns oops.helpers
  "Various helpers to be available to our code at runtime."
  (:require-macros [oops.helpers :refer [unchecked-aget]]
                   [oops.debug :refer [debug-assert]]))

(defn is-prototype? [o]
  (identical? (.-prototype (.-constructor o)) o))

(defn get-constructor [o]
  (unchecked-aget o "constructor"))

; IRC #clojurescript @ freenode.net on 2015-01-27:
; [13:40:09] darwin_: Hi, what is the best way to test if I'm handled ClojureScript data value or plain javascript object?
; [14:04:34] dnolen: there is a very low level thing you can check
; [14:04:36] dnolen: https://github.com/clojure/clojurescript/blob/c2550c4fdc94178a7957497e2bfde54e5600c457/src/clj/cljs/core.clj#L901
; [14:05:00] dnolen: this property is unlikely to change - still it's probably not something anything anyone should use w/o a really good reason
(defn cljs-type? [f]
  (and (goog/isObject f)                                                                                                      ; see http://stackoverflow.com/a/22482737/84283
       (not (is-prototype? f))
       (unchecked-aget f "cljs$lang$type")))

(defn cljs-instance? [value]
  (and (goog/isObject value)                                                                                                  ; see http://stackoverflow.com/a/22482737/84283
       (cljs-type? (get-constructor value))))

(defn to-native-array [coll]
  (if (array? coll)
    coll
    (let [arr (array)]
      (loop [items (seq coll)]                                                                                                ; note: items is either a seq or nil
        (if-not (nil? items)
          (let [item (-first items)]
            (.push arr item)
            (recur (next items)))
          arr)))))

(defn repurpose-error [error msg info]
  (debug-assert (instance? js/Error error))
  (debug-assert (string? msg))
  (set! (.-message error) msg)
  (specify! error
    IPrintWithWriter                                                                                                          ; nice to have for cljs-devtools and debug printing
    (-pr-writer [_obj writer opts]
      (-write writer msg)
      (when (some? info)
        (-write writer " ")
        (pr-writer info writer opts)))))
