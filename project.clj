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
                 [binaryage/dirac "0.6.6" :scope "test"]
                 [figwheel "0.5.7" :scope "test"]
                 [org.clojure/tools.logging "0.3.1" :scope "test"]
                 [clj-logging-config "1.9.12" :scope "test"]
                 [environ "1.1.0" :scope "test"]
                 [clansi "1.0.0" :scope "test"]
                 [funcool/cuerdas "1.0.1" :scope "test"]]

  :clean-targets ^{:protect false} ["target"
                                    "test/resources/_compiled"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-shell "0.5.0"]
            [lein-figwheel "0.5.7"]]

  ; this is just for IntelliJ + Cursive to play well
  :source-paths ["src"]
  :test-paths ["test/src"]
  :resource-paths ["test/resources"
                   "scripts"]

  :cljsbuild {:builds {}}                                                                                                     ; prevent https://github.com/emezeske/lein-cljsbuild/issues/413

  :profiles {:nuke-aliases
             {:aliases ^:replace {}}

             :cooper
             {:plugins [[lein-cooper "1.2.2"]]}

             :figwheel
             {:figwheel {:server-port          7118
                         :server-logfile       ".figwheel/log.txt"
                         :validate-interactive false
                         :repl                 false}}

             :repl-with-agent
             {:source-paths ["src/lib"
                             "test/src/circus"
                             "test/src/arena"
                             "test/src/tools"
                             "test/src/tests-basic"]
              :repl-options {:port             8230
                             :nrepl-middleware [dirac.nrepl/middleware]
                             :init             (do
                                                 (require 'dirac.agent)
                                                 (dirac.agent/boot!))}}

             :circus
             {:source-paths ["src/lib"
                             "test/src/circus"
                             "test/src/arena"
                             "test/src/tools"]}

             :testing-basic-onone
             {:cljsbuild {:builds {:basic-onone
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to       "test/resources/_compiled/basic_onone/main.js"
                                                   :output-dir      "test/resources/_compiled/basic_onone"
                                                   :asset-path      "_compiled/basic_onone"
                                                   :external-config {:devtools/config {:dont-detect-custom-formatters true}
                                                                     :oops/config     {:debug                    true
                                                                                       :dynamic-selector-usage   false
                                                                                       :static-nil-target-object false}}
                                                   :main            oops.runner
                                                   :optimizations   :none}
                                    :figwheel     true}}}}
             :testing-basic-oadvanced-core
             {:cljsbuild {:builds {:basic-oadvanced
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to       "test/resources/_compiled/basic_oadvanced/main.js"
                                                   :output-dir      "test/resources/_compiled/basic_oadvanced"
                                                   :asset-path      "_compiled/basic_oadvanced"
                                                   :elide-asserts   true
                                                   :external-config {:oops/config {:debug   true
                                                                                   :key-get :core
                                                                                   :key-set :core}}
                                                   :main            oops.runner
                                                   :optimizations   :advanced}}}}}
             :testing-basic-oadvanced-goog
             {:cljsbuild {:builds {:basic-oadvanced-goog
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests-basic"]
                                    :compiler     {:output-to       "test/resources/_compiled/basic_oadvanced_goog/main.js"
                                                   :output-dir      "test/resources/_compiled/basic_oadvanced_goog"
                                                   :asset-path      "_compiled/basic_oadvanced_goog"
                                                   :elide-asserts   true
                                                   :main            oops.runner
                                                   :optimizations   :advanced
                                                   :external-config {:oops/config {:debug   true
                                                                                   :key-get :goog
                                                                                   :key-set :goog}}}}}}}

             :auto-testing
             {:cljsbuild {:builds {:basic-onone          {:notify-command ["scripts/rerun-tests.sh" "basic_onone"]}
                                   :basic-oadvanced      {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced"]}
                                   :basic-oadvanced-goog {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced_goog"]}}}}

             :dirac
             {:cljsbuild {:builds {:basic-onone {:compiler {:preloads [dirac.runtime.preload]}}}}}


             :dev-basic-onone
             {:cooper {"server"     ["scripts/launch-fixtures-server.sh"]
                       "figwheel"   ["lein" "fig-basic-onone"]
                       "repl-agent" ["scripts/launch-repl-with-agent.sh"]
                       "browser"    ["scripts/launch-test-browser.sh"]}}
             }


  :aliases {"test"                 ["do"
                                    ["clean"]
                                    ["shell" "scripts/run-tests.sh"]]
            "dev-functional-tests" ["shell" "scripts/dev-functional-tests.sh"]
            "run-functional-tests" ["do"
                                    ["clean"]
                                    ["shell" "scripts/run-functional-tests.sh"]]
            "run-circus-tests"     ["do"
                                    ["clean"]
                                    ["shell" "scripts/run-circus-tests.sh"]]
            "build-tests"          ["do"
                                    ["with-profile" "+testing-basic-onone" "cljsbuild" "once" "basic-onone"]
                                    ["with-profile" "+testing-basic-oadvanced-core" "cljsbuild" "once" "basic-oadvanced"]
                                    ["with-profile" "+testing-basic-oadvanced-goog" "cljsbuild" "once" "basic-oadvanced-goog"]]
            "auto-build-tests"     ["do"
                                    ["with-profile" "+testing-basic-onone,+auto-testing" "cljsbuild" "once" "basic-onone"]
                                    ["with-profile" "+testing-basic-oadvanced-core,+auto-testing" "cljsbuild" "once" "basic-oadvanced"]
                                    ["with-profile" "+testing-basic-oadvanced-goog,+auto-testing" "cljsbuild" "once" "basic-oadvanced-goog"]]
            "fig-basic-onone"      ["with-profile" "+testing-basic-onone,+dirac,+figwheel" "figwheel"]
            "auto-basic-onone"     ["with-profile" "+testing-basic-onone,+auto-testing" "cljsbuild" "auto" "basic-onone"]
            "auto-test"            ["do"
                                    ["clean"]
                                    ["auto-build-tests"]]
            "release"              ["do"
                                    "shell" "scripts/check-versions.sh,"
                                    "clean,"
                                    "test,"
                                    "jar,"
                                    "shell" "scripts/check-release.sh,"
                                    "deploy" "clojars"]})
