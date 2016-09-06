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
                 [org.clojure/clojurescript "1.9.229" :scope "provided"]
                 [binaryage/devtools "0.8.1" :scope "test"]
                 [org.clojure/tools.logging "0.3.1" :scope "test"]
                 [clj-logging-config "1.9.12" :scope "test"]
                 [environ "1.1.0" :scope "test"]
                 [clansi "1.0.0" :scope "test"]
                 [funcool/cuerdas "1.0.1" :scope "test"]]

  :clean-targets ^{:protect false} ["target"
                                    "test/resources/_compiled"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-shell "0.5.0"]]

  :source-paths ["src/lib"]

  :test-paths []

  :cljsbuild {:builds {}}                                                                                                     ; prevent https://github.com/emezeske/lein-cljsbuild/issues/413

  :profiles {:circus
             {:source-paths ["test/src/circus"
                             "test/src/arena"
                             "test/src/tools"]}
             :devel
             {:cljsbuild {:builds {:devel
                                   {:source-paths ["src/lib"]
                                    :compiler     {:output-to     "target/devel/cljs_oops.js"
                                                   :output-dir    "target/devel"
                                                   :optimizations :none}}}}}

             :testing-basic-onone
             {:cljsbuild {:builds {:basic-onone
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to       "test/resources/_compiled/basic_onone/main.js"
                                                   :output-dir      "test/resources/_compiled/basic_onone"
                                                   :asset-path      "_compiled/basic_onone"
                                                   :external-config {:devtools/config {:dont-detect-custom-formatters true}}
                                                   :main            oops.runner
                                                   :optimizations   :none}}}}}
             :testing-basic-oadvanced
             {:cljsbuild {:builds {:basic-oadvanced
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to     "test/resources/_compiled/basic_oadvanced/main.js"
                                                   :output-dir    "test/resources/_compiled/basic_oadvanced"
                                                   :asset-path    "_compiled/basic_oadvanced"
                                                   :main          oops.runner
                                                   :optimizations :advanced}}}}}
             :testing-basic-oadvanced-goog
             {:cljsbuild {:builds {:basic-oadvanced-goog
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to       "test/resources/_compiled/basic_oadvanced_goog/main.js"
                                                   :output-dir      "test/resources/_compiled/basic_oadvanced_goog"
                                                   :asset-path      "_compiled/basic_oadvanced_goog"
                                                   :main            oops.runner
                                                   :optimizations   :advanced
                                                   :external-config {:oops/config {:atomic-get-mode :goog
                                                                                   :atomic-set-mode :goog}}}}}}}

             :auto-testing
             {:cljsbuild {:builds {:basic-onone          {:notify-command ["scripts/rerun-tests.sh" "basic_onone"]}
                                   :basic-oadvanced      {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced"]}
                                   :basic-oadvanced-goog {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced_goog"]}}}}}

  :aliases {"test"                   ["do"
                                      ["clean"]
                                      ["shell" "scripts/run-tests.sh"]]
            "run-functional-tests"   ["do"
                                      ["clean"]
                                      ["shell" "scripts/run-functional-tests.sh"]]
            "run-circus-tests"       ["do"
                                      ["clean"]
                                      ["shell" "scripts/run-circus-tests.sh"]]
            "build-tests"            ["do"
                                      ["with-profile" "+testing-basic-onone" "cljsbuild" "once" "basic-onone"]
                                      ["with-profile" "+testing-basic-oadvanced" "cljsbuild" "once" "basic-oadvanced"]
                                      ["with-profile" "+testing-basic-oadvanced-goog" "cljsbuild" "once" "basic-oadvanced-goog"]]
            "auto-build-tests"       ["do"
                                      ["with-profile" "+testing-basic-onone,+auto-testing" "cljsbuild" "once" "basic-onone"]
                                      ["with-profile" "+testing-basic-oadvanced,+auto-testing" "cljsbuild" "once" "basic-oadvanced"]
                                      ["with-profile" "+testing-basic-oadvanced-goog,+auto-testing" "cljsbuild" "once" "basic-oadvanced-goog"]]
            "auto-build-basic-onone" ["with-profile" "+testing-basic-onone,+auto-testing" "cljsbuild" "auto" "basic-onone"]
            "auto-test"              ["do"
                                      ["clean"]
                                      ["auto-build-tests"]]
            "release"                ["do"
                                      "shell" "scripts/check-versions.sh,"
                                      "clean,"
                                      "test,"
                                      "jar,"
                                      "shell" "scripts/check-release.sh,"
                                      "deploy" "clojars"]})
