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
             {:cljsbuild {:builds {:basic-optimizations-none
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to     "test/resources/_compiled/basic_optimizations_none/main.js"
                                                   :output-dir    "test/resources/_compiled/basic_optimizations_none"
                                                   :asset-path    "_compiled/basic_optimizations_none"
                                                   :main          oops.runner
                                                   :optimizations :none}}
                                   :basic-optimizations-advanced
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to     "test/resources/_compiled/basic_optimizations_advanced/main.js"
                                                   :output-dir    "test/resources/_compiled/basic_optimizations_advanced"
                                                   :asset-path    "_compiled/basic_optimizations_advanced"
                                                   :main          oops.runner
                                                   :optimizations :advanced}}}}}
             :auto-testing
             {:cljsbuild {:builds {:basic-optimizations-none     {:notify-command ["scripts/rerun-tests.sh" "basic_optimizations_none"]}
                                   :basic-optimizations-advanced {:notify-command ["scripts/rerun-tests.sh" "basic_optimizations_advanced"]}}}}}

  :aliases {"test"             ["do"
                                ["clean"]
                                ["build-tests"]
                                ["shell" "scripts/run-tests.sh"]]
            "build-tests"      ["with-profile" "+testing" "cljsbuild" "once"
                                "basic-optimizations-none"
                                "basic-optimizations-advanced"]
            "auto-build-tests" ["with-profile" "+testing,+auto-testing" "cljsbuild" "auto"
                                "basic-optimizations-none"
                                "basic-optimizations-advanced"]
            "auto-test"        ["do"
                                ["clean"]
                                ["auto-build-tests"]]
            "release"          ["do"
                                "shell" "scripts/check-versions.sh,"
                                "clean,"
                                "test,"
                                "jar,"
                                "shell" "scripts/check-release.sh,"
                                "deploy" "clojars"]})
