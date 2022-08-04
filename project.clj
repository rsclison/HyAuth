(defproject hyauth "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [compojure "1.6.2"]
                 [com.appsflyer/donkey "0.5.1"]
                 [luposlip/json-schema "0.3.2"]
                 [org.clojure/data.json "2.4.0"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [clojure.java-time "0.3.3"]
                 [org.clojure/core.cache "1.0.217"]
                 [clj-http "3.12.3"]
                 [puppetlabs/clj-ldap "0.3.0"]
                 [com.taoensso/timbre "5.1.2"]
                 [hiccup "1.0.5"]
                 [clj-json "0.5.3"]
                 [com.h2database/h2 "1.4.200"]
                 [com.brunobonacci/mulog "0.9.0"]
                 
                 ]
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        ]}})
