(defproject binaryage/oops "0.5.3"
  :description "ClojureScript macros for convenient Javascript object access."
  :url "https://github.com/binaryage/cljs-oops"
  :license {:name         "MIT License"
            :url          "http://opensource.org/licenses/MIT"
            :distribution :repo}

  :scm {:name "git"
        :url  "https://github.com/binaryage/cljs-oops"}

  :dependencies [[org.clojure/clojure "1.9.0-alpha15" :scope "provided"]
                 [org.clojure/clojurescript "1.9.494" :scope "provided"]
                 [funcool/cuerdas "2.0.3"]
                 [binaryage/env-config "0.2.0"]

                 [environ "1.1.0" :scope "test"]
                 [binaryage/devtools "0.9.2" :scope "test"]
                 [binaryage/dirac "1.2.2" :scope "test"]
                 [figwheel "0.5.9" :scope "test"]
                 [org.clojure/tools.logging "0.3.1" :scope "test"]
                 [clj-logging-config "1.9.12" :scope "test"]
                 [clansi "1.0.0" :scope "test"]]

  :clean-targets ^{:protect false} ["target"
                                    "test/resources/.compiled"]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-shell "0.5.0"]
            [lein-figwheel "0.5.9"]]

  ; this is just for IntelliJ + Cursive to play well
  :source-paths ["src/lib"]
  :test-paths ["test/src"]
  :resource-paths ^:replace ["test/resources"
                             "scripts"]

  :cljsbuild {:builds {}}                                                                                                     ; prevent https://github.com/emezeske/lein-cljsbuild/issues/413

  :profiles {:nuke-aliases
             {:aliases ^:replace {}}

             :lib
             ^{:pom-scope :provided}                                                                                          ; ! to overcome default jar/pom behaviour, our :dependencies replacement would be ignored for some reason
             [:nuke-aliases
              {:dependencies   ~(let [project (->> "project.clj" slurp read-string (drop 3) (apply hash-map))
                                      test-dep? #(->> % (drop 2) (apply hash-map) :scope (= "test"))
                                      non-test-deps (remove test-dep? (:dependencies project))]
                                  (with-meta (vec non-test-deps) {:replace true}))                                            ; so ugly!
               :source-paths   ^:replace ["src/lib"]
               :resource-paths ^:replace []
               :test-paths     ^:replace []}]

             :clojure18
             {:dependencies [[org.clojure/clojure "1.8.0" :scope "provided" :upgrade false]
                             [clojure-future-spec "1.9.0-alpha14" :scope "provided"]]}

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
                             "test/src/tests"]
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
                                                   "test/src/tests"]
                                    :compiler     {:output-to       "test/resources/.compiled/basic_onone/main.js"
                                                   :output-dir      "test/resources/.compiled/basic_onone"
                                                   :asset-path      ".compiled/basic_onone"
                                                   :main            oops.runner
                                                   :optimizations   :none
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
                                                   :external-config {:oops/config {:debug   true
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
                                                   :external-config {:oops/config {:debug   true
                                                                                   :key-get :goog
                                                                                   :key-set :goog}}}}}}}

             :auto-testing
             {:cljsbuild {:builds {:basic-onone          {:notify-command ["scripts/rerun-tests.sh" "basic_onone"]}
                                   :basic-oadvanced      {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced_core"]}
                                   :basic-oadvanced-goog {:notify-command ["scripts/rerun-tests.sh" "basic_oadvanced_goog"]}}}}

             :dirac
             {:cljsbuild {:builds {:basic-onone {:compiler {:preloads [dirac.runtime.preload]}}}}}


             :dev-basic-onone
             {:cooper {"server"     ["scripts/launch-fixtures-server.sh"]
                       "figwheel"   ["lein" "fig-basic-onone"]
                       "repl-agent" ["scripts/launch-repl-with-agent.sh"]
                       "browser"    ["scripts/launch-test-browser.sh"]}}}

  :aliases {"test"                 ["do"
                                    ["clean"]
                                    ["shell" "scripts/test.sh"]]
            "test-all"             ["shell" "scripts/test-all.sh"]
            "dev-functional-tests" ["shell" "scripts/dev-functional-tests.sh"]
            "run-functional-tests" ["do"
                                    ["clean"]
                                    ["shell" "scripts/run-functional-tests.sh"]]
            "run-circus-tests"     ["do"
                                    ["clean"]
                                    ["shell" "scripts/run-circus-tests.sh"]]
            "build-tests"          ["do"
                                    ["with-profile" "+testing-basic-onone" "cljsbuild" "once" "basic-onone"]
                                    ["with-profile" "+testing-basic-oadvanced-core" "cljsbuild" "once" "basic-oadvanced-core"]
                                    ["with-profile" "+testing-basic-oadvanced-goog" "cljsbuild" "once" "basic-oadvanced-goog"]]
            "auto-build-tests"     ["do"
                                    ["with-profile" "+testing-basic-onone,+auto-testing" "cljsbuild" "once" "basic-onone"]
                                    ["with-profile" "+testing-basic-oadvanced-core,+auto-testing" "cljsbuild" "once" "basic-oadvanced-core"]
                                    ["with-profile" "+testing-basic-oadvanced-goog,+auto-testing" "cljsbuild" "once" "basic-oadvanced-goog"]]
            "fig-basic-onone"      ["with-profile" "+testing-basic-onone,+dirac,+figwheel" "figwheel"]
            "auto-basic-onone"     ["with-profile" "+testing-basic-onone,+auto-testing" "cljsbuild" "auto" "basic-onone"]
            "auto-test"            ["do"
                                    ["clean"]
                                    ["auto-build-tests"]]
            "toc"                  ["shell" "scripts/generate-toc.sh"]
            "install"              ["do"
                                    ["shell" "scripts/prepare-jar.sh"]
                                    ["shell" "scripts/local-install.sh"]]
            "jar"                  ["shell" "scripts/prepare-jar.sh"]
            "deploy"               ["shell" "scripts/deploy-clojars.sh"]
            "release"              ["do"
                                    ["clean"]
                                    ["shell" "scripts/check-versions.sh"]
                                    ["shell" "scripts/prepare-jar.sh"]
                                    ["shell" "scripts/check-release.sh"]
                                    ["shell" "scripts/deploy-clojars.sh"]]})
