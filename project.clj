(defproject cspoc "0.1.0-SNAPSHOT"
  :description "A Proof-Of-Concept Clojure app to test clojure.spec"
  :url "https://github.com/jasongilman/proto-repl-demo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha10"]
                 [proto-repl "0.3.1"]
                 [proto-repl-charts "0.3.1"]]

  :profiles
  {:dev {:source-paths ["dev" "src" "test"]
         :dependencies [[org.clojure/tools.namespace "0.2.11"]
                        [org.clojure/test.check "0.9.0"]]}})
