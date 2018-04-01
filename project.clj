(defproject parinfer-cljs "3.11.0-0"
  :description "Simpler Lisp Editing"
  :url "https://github.com/shaunlebron/parinfer-cljs"

  :min-lein-version "2.7.1"

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-doo "0.1.10"]]

  :dependencies [[org.clojure/clojure "1.10.0-alpha4"]
                 [org.clojure/clojurescript "1.10.238" :scope "provided"]
                 [cljsjs/parinfer "3.11.0-0"]]

  :cljsbuild {:builds
              {:test
               {:source-paths ["src" "test"]
                :compiler     {:output-to     "out/testable.js"
                               :main          parinfer-cljs.doo-test
                               :optimizations :none
                               :target        :nodejs}}}})