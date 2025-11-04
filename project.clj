(defproject snake "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.2"]
                 [uk.co.electronstudio.jaylib/jaylib "5.5.0-2"]
                 [org.clojure/core.match "1.1.0"]]
  :jvm-opts ["-XstartOnFirstThread" "--enable-native-access=ALL-UNNAMED"]
  :main ^:skip-aot snake.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
