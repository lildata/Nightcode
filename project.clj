(defproject nightcode "1.1.0-SNAPSHOT"
  :description "An IDE for Clojure and Java"
  :url "https://github.com/oakes/Nightcode"
  :license {:name "Public Domain"
            :url "http://unlicense.org/UNLICENSE"}
  :dependencies [[com.fifesoft/autocomplete "2.5.8"]        ; keep
                 [com.fifesoft/rsyntaxtextarea "2.5.8"]     ; keep
                 [com.github.insubstantial/substance "7.3"] ; remove
                 [compliment "0.2.5"] ; keep completion library
                 [gwt-plugin "0.1.6"] ; remove A Leiningen plugin run and build gwt applications.
                 [hiccup "1.0.5"] ; probably keep
                 [leiningen "2.5.3"
                  :exclusions [leiningen.search]] ; keep
                 [lein-ancient "0.5.4"
                  :exclusions [clj-aws-s3]] ; remove
                 [lein-cljsbuild "1.1.2"] ; ?
                 [lein-clr "0.2.2"] ; remove
                 [lein-droid "0.4.3"] ; remove
                 [lein-typed "0.3.5"] ; ...
                 [lein-ring "0.9.7"] ; ...
                 [net.cgrand/parsley "0.9.3"
                  :exclusions [org.clojure/clojure]] ; keep a DSL for creating total and truly incremental parsers in Clojure
                 [net.java.balloontip/balloontip "1.2.4.1"] ; probably need to be kept...
                 [org.clojure/clojure "1.8.0"] ; keep of course...
                 [org.clojure/core.incubator "0.1.3"] ; keep
                 [org.clojure/tools.cli "0.3.3"] ; keep
                 [org.clojure/tools.namespace "0.2.10"] ; keep
                 [org.eclipse.jgit "3.5.3.201412180710-r"
                  :exclusions [org.apache.httpcomponents/httpclient]] ; remove
                 [org.flatland/ordered "1.5.3"] ; keep Ordered sets and maps, implemented in pure clojure
                 [org.lpetit/paredit.clj "0.19.3"
                  :exclusions [net.cgrand/parsley
                               org.clojure/clojure]] ; keep
                 [play-clj/lein-template "1.0.0"] ; remove
                 [seesaw "1.4.5"]]
  :uberjar-exclusions [#"PHPTokenMaker\.class"
                       #"org\/apache\/lucene"]
  :resource-paths ["resources"]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [clojure.main nightcode.core nightcode.lein]
  :main ^:skip-aot nightcode.Nightcode)
