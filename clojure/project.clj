(defproject worktime "0.1.0-SNAPSHOT"
  :description "Simple time tracking app"
  :url "http://example.com/FIXME"
  :license {:name "GPLv3"
            :url "https://www.gnu.org/licenses/gpl.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] [clj-time "0.8.0"]]
  :main ^:skip-aot worktime.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
