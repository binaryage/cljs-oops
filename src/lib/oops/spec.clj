(ns oops.spec
  "Some helper utils for clojure.spec.")

; clojurescript related question, I’m working on a library which uses clojure.spec to describe a data structure
; using `s/*`, the data structure can be used statically during compilation in macros, or during runtime in cljs.
; all is nice and shiny on clj side[1], but on cljs side, I want native js arrays to be treated as collections f
; or the purpose of speccing, but unfortunately `s/*` does not treat them such. Ended up writing custom predicates
; for native array case[2], just wondering if there is a better way to teach `s/*`
; to walk native js arrays the same way as cljs vectors.
; [1] https://github.com/binaryage/cljs-oops/blob/master/src/lib/oops/sdefs.clj
; [2] https://github.com/binaryage/cljs-oops/blob/master/src/lib/oops/sdefs.cljs

(defmacro native-array-aware-* [pred-form]
  `(clojure.spec.alpha/or :regex (clojure.spec.alpha/* ~pred-form)
                          :native-array #(and (cljs.core/array? %)
                                              (cljs.core/every? (partial clojure.spec.alpha/valid? ~pred-form) %))))
