(defproject binaryage/oops "0.1.0-SNAPSHOT"
  :description "ClojureScript macros for convenient Javascript object access."
  :url "https://github.com/binaryage/cljs-oops"
  :license {:name         "MIT License"
            :url          "http://opensource.org/licenses/MIT"
            :distribution :repo}

  :scm {:name "git"
        :url  "https://github.com/binaryage/cljs-oops"}

  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.227" :scope "provided"]]

  :clean-targets ^{:protect false} ["target"
                                    "test/resources/_compiled"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-shell "0.5.0"]]

  :source-paths ["src/lib"]

  :test-paths ["test"]

  :cljsbuild {:builds {}}                                                                                                     ; prevent https://github.com/emezeske/lein-cljsbuild/issues/413

  :profiles {:devel
             {:cljsbuild {:builds {:devel
                                   {:source-paths ["src/lib"]
                                    :compiler     {:output-to     "target/devel/cljs_oops.js"
                                                   :output-dir    "target/devel"
                                                   :optimizations :none}}}}}

             :testing
             {:cljsbuild {:builds {:tests
                                   {:source-paths ["src/lib"
                                                   "test/src/tests"]
                                    :compiler     {:output-to     "test/resources/_compiled/tests/main.js"
                                                   :output-dir    "test/resources/_compiled/tests"
                                                   :asset-path    "_compiled/tests"
                                                   :preloads      [oops.testenv oops.runner]
                                                   :main          oops.main
                                                   :optimizations :none}}}}}
             :auto-testing
             {:cljsbuild {:builds {:tests
                                   {:notify-command ["phantomjs" "test/resources/phantom.js" "test/resources/run-tests.html"]}}}}}

  :aliases {"test"       ["do"
                          "clean,"
                          "test-tests"]
            "test-tests" ["do"
                          "with-profile" "+testing" "cljsbuild" "once" "tests,"
                          "shell" "phantomjs" "test/resources/phantom.js" "test/resources/run-tests.html"]
            "auto-test"  ["do"
                          "clean,"
                          "with-profile" "+testing,+auto-testing" "cljsbuild" "auto" "tests"]
            "release"    ["do"
                          "shell" "scripts/check-versions.sh,"
                          "clean,"
                          "test,"
                          "jar,"
                          "shell" "scripts/check-release.sh,"
                          "deploy" "clojars"]})
