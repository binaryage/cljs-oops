(defproject binaryage/oops "0.1.0-SNAPSHOT"
  :description "ClojureScript macros for convenient Javascript object access."
  :url "https://github.com/binaryage/cljs-oops"
  :license {:name         "MIT License"
            :url          "http://opensource.org/licenses/MIT"
            :distribution :repo}

  :scm {:name "git"
        :url  "https://github.com/binaryage/cljs-oops"}

  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [clojure-future-spec "1.9.0-alpha11" :scope "provided"]
                 [org.clojure/clojurescript "1.9.227" :scope "provided"]]

  :clean-targets ^{:protect false} ["target"
                                    "test/resources/_compiled"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-shell "0.5.0"]]

  :source-paths ["src/lib"]

  :test-paths []

  :cljsbuild {:builds {}}                                                                                                     ; prevent https://github.com/emezeske/lein-cljsbuild/issues/413

  :profiles {:devel
             {:cljsbuild {:builds {:devel
                                   {:source-paths ["src/lib"]
                                    :compiler     {:output-to     "target/devel/cljs_oops.js"
                                                   :output-dir    "target/devel"
                                                   :optimizations :none}}}}}

             :testing-basic-optimizations-none
             {:cljsbuild {:builds {:basic-optimizations-none
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to     "test/resources/_compiled/basic_optimizations_none/main.js"
                                                   :output-dir    "test/resources/_compiled/basic_optimizations_none"
                                                   :asset-path    "_compiled/basic_optimizations_none"
                                                   :main          oops.runner
                                                   :optimizations :none}}}}}
             :testing-basic-optimizations-advanced
             {:cljsbuild {:builds {:basic-optimizations-advanced
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to     "test/resources/_compiled/basic_optimizations_advanced/main.js"
                                                   :output-dir    "test/resources/_compiled/basic_optimizations_advanced"
                                                   :asset-path    "_compiled/basic_optimizations_advanced"
                                                   :main          oops.runner
                                                   :optimizations :advanced}}}}}
             :testing-prefer-warnings
             {:cljsbuild {:builds {:prefer-warnings
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-prefer-warnings"]
                                    :compiler     {:output-to       "test/resources/_compiled/prefer_warnings/main.js"
                                                   :output-dir      "test/resources/_compiled/prefer_warnings"
                                                   :asset-path      "_compiled/prefer_warnings"
                                                   :main            oops.runner
                                                   :optimizations   :none
                                                   :external-config {:oops/config {:object-access-validation :warn}}}}}}}
             :auto-testing
             {:cljsbuild {:builds {:basic-optimizations-none     {:notify-command ["scripts/rerun-tests.sh" "basic_optimizations_none"]}
                                   :basic-optimizations-advanced {:notify-command ["scripts/rerun-tests.sh" "basic_optimizations_advanced"]}}}}}

  :aliases {"test"                        ["do"
                                           ["clean"]
                                           ["build-tests"]
                                           ["shell" "scripts/run-tests.sh"]]
            "build-tests"                 ["do"
                                           ["with-profile" "+testing-basic-optimizations-none" "cljsbuild" "once" "basic-optimizations-none"]
                                           ["with-profile" "+testing-basic-optimizations-advanced" "cljsbuild" "once" "basic-optimizations-advanced"]
                                           ["with-profile" "+testing-prefer-warnings" "cljsbuild" "once" "prefer-warnings"]]
            "auto-build-tests"            ["do"
                                           ["with-profile" "+testing-basic-optimizations-none,+auto-testing" "cljsbuild" "once" "basic-optimizations-none"]
                                           ["with-profile" "+testing-basic-optimizations-advanced,+auto-testing" "cljsbuild" "once" "basic-optimizations-advanced"]
                                           ["with-profile" "+testing-prefer-warnings" "cljsbuild,+auto-testing" "once" "prefer-warnings"]]
            "auto-build-basic-none-tests" ["with-profile" "+testing,+auto-testing" "cljsbuild" "auto"
                                           "basic-optimizations-none"]
            "auto-test"                   ["do"
                                           ["clean"]
                                           ["auto-build-tests"]]
            "release"                     ["do"
                                           "shell" "scripts/check-versions.sh,"
                                           "clean,"
                                           "test,"
                                           "jar,"
                                           "shell" "scripts/check-release.sh,"
                                           "deploy" "clojars"]})
