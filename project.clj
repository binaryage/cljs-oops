(def clojurescript-version (or (System/getenv "CANARY_CLOJURESCRIPT_VERSION") "1.11.4"))
(def required-deps
  [['binaryage/env-config "0.2.2"]])
(def provided-deps
  [['org.clojure/clojure "1.10.3" :scope "provided"]
   ['org.clojure/clojurescript clojurescript-version :scope "provided"]])
(def test-deps
  [['environ "1.2.0" :scope "test"]
   ['funcool/cuerdas "2022.01.14-391"]
   ['binaryage/devtools "RELEASE" :scope "test"]
   ['figwheel "RELEASE" :scope "test"]
   ['org.clojure/tools.logging "1.2.4" :scope "test"]
   ['clj-logging-config "1.9.12" :scope "test"]
   ['clansi "1.0.0" :scope "test"]])
(def lib-deps (concat provided-deps required-deps))
(def all-deps (concat lib-deps test-deps))
(defproject binaryage/oops "0.7.2"
  :description "ClojureScript macros for convenient Javascript object access."
  :url "https://github.com/binaryage/cljs-oops"
  :license {:name         "MIT License"
            :url          "http://opensource.org/licenses/MIT"
            :distribution :repo}

  :scm {:name "git"
        :url  "https://github.com/binaryage/cljs-oops"}

  :dependencies ~all-deps

  :clean-targets ^{:protect false} ["target"
                                    "test/resources/.compiled"]

  :plugins [[lein-cljsbuild "1.1.8"]
            [lein-figwheel "RELEASE"]]

  ; this is just for IntelliJ + Cursive to play well
  :source-paths ["src/lib"]
  :test-paths ["test/src/arena"
               "test/src/circus"
               "test/src/runner"
               "test/src/tests"
               "test/src/tools"]
  :resource-paths ^:replace ["test/resources"
                             "scripts"]

  :cljsbuild {:builds {}}                                                                                                     ; prevent https://github.com/emezeske/lein-cljsbuild/issues/413

  :profiles {
             :lib
             ^{:pom-scope :provided}                                                                                          ; ! to overcome default jar/pom behaviour, our :dependencies replacement would be ignored for some reason
             [{:dependencies   ~(with-meta lib-deps {:replace true})
               :source-paths   ^:replace ["src/lib"]
               :resource-paths ^:replace []
               :test-paths     ^:replace []}]

             :clojure18
             {:dependencies [[org.clojure/clojure "1.8.0" :scope "provided" :upgrade false]
                             [clojure-future-spec "1.9.0-beta4" :scope "provided"]]}

             :clojure19
             {:dependencies [[org.clojure/clojure "1.9.0" :scope "provided" :upgrade false]]}

             :cooper
             {:plugins [[lein-cooper "1.2.2"]]}

             :figwheel
             {:figwheel {:server-port          7118
                         :server-logfile       ".figwheel/log.txt"
                         :validate-interactive false
                         :repl                 false}}

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
                                                   "test/src/tests"]
                                    :compiler     {:output-to       "test/resources/.compiled/basic_onone/main.js"
                                                   :output-dir      "test/resources/.compiled/basic_onone"
                                                   :asset-path      ".compiled/basic_onone"
                                                   :main            oops.runner
                                                   :optimizations   :none
                                                   :checked-arrays  :warn                                                     ; see https://github.com/binaryage/cljs-oops/issues/14
                                                   :external-config {:devtools/config {:dont-detect-custom-formatters true}
                                                                     :oops/config     {:debug                    true
                                                                                       :dynamic-selector-usage   false
                                                                                       :static-nil-target-object false}}}
                                    :figwheel     true}}}}
             :testing-basic-oadvanced-core
             {:cljsbuild {:builds {:basic-oadvanced-core
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests"]
                                    :compiler     {:output-to       "test/resources/.compiled/basic_oadvanced_core/main.js"
                                                   :output-dir      "test/resources/.compiled/basic_oadvanced_core"
                                                   :asset-path      ".compiled/basic_oadvanced_core"
                                                   :main            oops.runner
                                                   :pseudo-names    true
                                                   :optimizations   :advanced
                                                   :checked-arrays  :warn                                                     ; see https://github.com/binaryage/cljs-oops/issues/14
                                                   :external-config {:devtools/config {:silence-optimizations-warning true}
                                                                     :oops/config     {:debug   true
                                                                                       :key-get :core
                                                                                       :key-set :core}}}}}}}
             :testing-basic-oadvanced-goog
             {:cljsbuild {:builds {:basic-oadvanced-goog
                                   {:source-paths ["src/lib"
                                                   "test/src/runner"
                                                   "test/src/tools"
                                                   "test/src/tests"]
                                    :compiler     {:output-to       "test/resources/.compiled/basic_oadvanced_goog/main.js"
                                                   :output-dir      "test/resources/.compiled/basic_oadvanced_goog"
                                                   :asset-path      ".compiled/basic_oadvanced_goog"
                                                   :main            oops.runner
                                                   :pseudo-names    true
                                                   :optimizations   :advanced
                                                   :checked-arrays  :warn                                                     ; see https://github.com/binaryage/cljs-oops/issues/14
                                                   :external-config {:devtools/config {:silence-optimizations-warning true}
                                                                     :oops/config     {:debug   true
                                                                                       :key-get :goog
                                                                                       :key-set :goog}}}}}}}

             :auto-testing
             {:cljsbuild {:builds {:basic-onone          {:notify-command ["scripts/rerun-tests.sh" "basic_onone"]}
                                   :basic-oadvanced      {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced_core"]}
                                   :basic-oadvanced-goog {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced_goog"]}}}}

             :dev-basic-onone
             {:cooper {"server"   ["scripts/launch-fixtures-server.sh"]
                       "figwheel" ["scripts/fig-basic-onone.sh"]}}})
