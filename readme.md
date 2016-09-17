# cljs-oops

[![GitHub license](https://img.shields.io/github/license/binaryage/cljs-oops.svg)](license.txt) 
[![Clojars Project](https://img.shields.io/clojars/v/binaryage/oops.svg)](https://clojars.org/binaryage/oops) 
[![Travis](https://img.shields.io/travis/binaryage/cljs-oops.svg)](https://travis-ci.org/binaryage/cljs-oops) 
[![Sample Project](https://img.shields.io/badge/project-example-ff69b4.svg)](https://github.com/binaryage/cljs-oops-sample)

```
Boss: "Ship it!"
You:  "Let me compile it with :advanced optimizations..."
Boss: "Sounds good!"
...one coffee later
You:  "Oops! It just broke! And I don't know why." 
Boss: "Don't tell me that a random person on the Internet was wrong again."
You:  (sad face) "Yep, they provided slightly outdated externs!"
```

This is a ClojureScript micro-library providing few essential macros for convenient Javascript object access.

### Object operations 

Add these new power-macros to your tool belt:
 
 1. `oget` is a flexible, safe and guilt-free replacement for `aget`
 2. `oset!` is `aset` on steroids
 3. `ocall` is a replacement for `(.call ...)` built on top of `oget`
 4. `oapply` is a replacement for `(.apply ...)` built on top of `oget`
 
Let's see some code examples first and then discuss the concepts:

<a href="https://dl.dropboxusercontent.com/u/559047/cljs-oops-intro-oget.png"><img src="https://dl.dropboxusercontent.com/u/559047/cljs-oops-intro-oget.png"></a>

<a href="https://dl.dropboxusercontent.com/u/559047/cljs-oops-intro-oset.png"><img src="https://dl.dropboxusercontent.com/u/559047/cljs-oops-intro-oset.png"></a>

### Installation

#### Integrate with your project

Add oops dependency into your Leiningen's `project.clj` or boot file. 

[![Clojars Project](https://img.shields.io/clojars/v/binaryage/oops.svg)](https://clojars.org/binaryage/oops)

Require macros in your namespaces via `oops.core` ClojureScript namespace:

```clojure
(ns your.project.namespace
  (:require [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]]))

(oset! (js-obj) :mood "a happy camper")
```

Please be aware that oops uses [clojure.spec][14] which is available since Clojure 1.9.
If you cannot upgrade to Clojure 1.9 yet, you may stick with Clojure 1.8 and add [backported version of clojure.spec][15].

Otherwise pretty standard stuff. If in doubts, look at the [sample project][13].

### Motivation 

> I don't always do Javascript interops, but when I do, I call them by names.
>
> -- <cite>Darwin (with sunglasses on)</cite>

ClojureScript developers should quickly learn how to inter-operate with native Javascript objects via [the dot operator][1].
This was modelled to closely follow [Cojure's Java interop][4] story.

For example, the ClojureScript form `(.-nativeProp obj)` will compile to `obj.nativeProp` Javascript code.
 
It works pretty well [during development][3] but there is a catch! When you naively write code like that, it might 
not survive [advanced optimizations][2]. Closure Compiler needs some information about which property names are safe to rename 
and which cannot be renamed because they might be referenced externally or dynamically via strings.

Someone at Google had a quick and bad idea. We could provide a separate file which would describe this information. 
Let's call it an "externs file"! 

#### Externs from hell

I'm pretty opinionated about using externs. I hate it with passion. Here is the list of my reasons:

1. Development behaviour is disconnected from production behaviour - discovering breakages only after switching to :advanced mode.
I know, I should continuously run tests against :advanced mode. But :advanced builds are pretty slow and it is no fun to 
fish for "Cannot read property 'j349s' of null"-kind of errors in minified raw Javascript files which often balloon to multi-MB sizes.
Have to wait for quantum computers to provide our IDEs with enough computational power to parse and syntax-highlight 
multi-megabyte one-line Javascript files ;)

2. Say, authors of a useful (native) library libX don't provide externs file (usually simply because they don't use Closure Compiler).
 So there must come [someone else][7] who is willing to maintain an externs file for their library by following their changes in libX. 
 You want to use libX so now you made yourself dependent on two sources of truth and they don't usually move in a lock-step. 
 Also that someone will probably sooner or later lose interest in maintaining the extern file and you have no way of telling 
 if it is outdated/incomplete without doing a full code-review. And the worst part is that "someone" is very often you.
 
3. Incomplete (or outdated) extern files provide no feedback. Except that you suddenly discover that a new build is broken again and 
 you are back to "[pseudo-names][8] fishing".
 
4. Externs have to be configured. Paths must be specified. Externs are not co-located with the code they are describing.
It is not always clear where individual externs are coming from. Some "default" externs for standard browser/DOM APIs are baked-in 
Closure Compiler by default, which might give you false sense of security or confuse assumptions how this whole thing works.
 
#### Side-stepping the whole externs mess

What if I told you to ditch your externs because there is a simpler way? 

Simply [use string names][5] to access object properties in Javascript.
Instead of `(.-nativeProp obj)` write `(aget obj "nativeProp")` which compiles to `obj["nativeProp"]`. String names are not
subject of renaming in advanced mode. And practically same code runs in development and advanced mode. 

I hear you. This looks dirty. We are abusing `aget` which was [explicitly documented][6] to be for native array only.
Alternatively we could use `goog.object/get` or the multi-arity `goog.object/getValueByKeys` which looks a bit better, 
but kinda verbose.

Instead of investing your energy into externs you can incrementally write a lightweight Clojure-style wrapper functions to access 
native APIs by string names directly. For example:

```clojure
(defn get-element-by-id [id]
  (.call (aget js/document "getElementById") js/document id))
```

It is much more flexible than externs. You have full powers of ClojureScript code here. And who knows, maybe later you will 
extract the code and publish it as a nice ClojureScript wrapper library.

Sounds good? What if we had something like `aget` but safer and more flexible?

### Benefits

#### Be more expressive with selectors

The signature for `oget` is `(oget obj & selector)`. 

Selector is a data structure describing exact path for traversing into the native object `obj`.
Selectors can be plain strings, keywords or for convenience [arbitrarily nested collections of those][9].

Selectors are pretty flexible, following selectors describe the same path:

```clojure
(oget o "k3.?k31.k311")
(oget o "k3" "?k31" :k311)
(oget o ["k3" "?k31" "k311"])
(oget o [["k3"] "?k31"] "k311")
```

##### Access modifiers

Please note the ".?" is a modifier for "soft dot" access. We expect that the key 'k31' might not be present and want
`oget` to stop and silently return nil in that case.

In case of `oset!` you can use a "punching dot" for creation of missing keys on path. For example:

```clojure
(oset! (js-obj) "!k1.!k2.k3" "val")
```

That will create `k1` and `k2` on the path to setting final `k3` key to `val`. If you didn't specify the exclamation modifiers
 `oset!` would complain about missing keys. This makes sense because if you know the path exists you don't want to use punching
 and that will ultimately lead to simpler code generated in :advanced mode (without any checks for missing keys).

#### Static vs. dynamic selectors

Dynamic selector is a selector which is not fully known at compile-time. For example result of a function call
 is a dynamic selector:
 
```clojure
(oget o (identity "key"))
```

The result at runtime is the same but generated code is less effective. Dynamic selectors should be very rare. 
By default, oops assumes that you want to prefer static selectors and dynamic selectors are a mistake. 
Compiler will issue a compile-time warning about "Unexpected dynamic property access". 
To silence this warnings use "plus" version of `oget` like this:

```clojure
(oget+ o (identity "key"))
```

This way you express explicit consent with dynamic selector code-path.

#### Be safe during development

By default, oops generates diagnostics code and does pretty intensive safe-checking in non-advanced builds.
As you can see on the screenshots above you might get compile-time or run-time warnings and errors when unexpected things happen,
like accessing missing keys or traversing non-objects.

#### Produce efficient bare-bone code in :advanced builds

By default, all diagnostics code is elided in :advanced builds and oops produces code similar hand-written `aget` usage
(without any safety-checks).

You can inspect our test [compilation transcripts][10] to see what code is generated in different compiler modes.

#### Tailor oops behaviour

I believe oops has sensible defaults and there should be no need to tweak it under normal circumstances.
Anyways, look at possible configuration options in [config.clj][11].

As you can see you can provide your own config overrides in ClojureScript compiler options map via `:external-config > :oops/config`.
See example in [cljs-oops-sample][11] project.

[1]: http://cljs.github.io/api/syntax/dot
[2]: https://github.com/clojure/clojurescript/wiki/Advanced-Compilation
[3]: https://github.com/clojure/clojurescript/wiki/Compiler-Options#optimizations
[4]: http://clojure.org/reference/java_interop
[5]: https://github.com/clojure/clojurescript/wiki/Dependencies#using-string-names
[6]: http://cljs.github.io/api/cljs.core/aget
[7]: http://cljsjs.github.io
[8]: https://github.com/clojure/clojurescript/wiki/Compiler-Options#pseudo-names
[9]: https://github.com/binaryage/cljs-oops/blob/master/src/lib/oops/sdefs.clj
[10]: https://github.com/binaryage/cljs-oops/tree/master/test/transcripts/expected
[11]: https://github.com/binaryage/cljs-oops/blob/master/src/lib/oops/config.clj
[12]: https://github.com/binaryage/cljs-oops-sample/blob/932fc322ff5ab1cb26e48c136b63d24c8c5c1615/project.clj#L43
[13]: https://github.com/binaryage/cljs-oops-sample
[14]: http://clojure.org/guides/spec
[15]: https://github.com/tonsky/clojure-future-spec
