(ns hyauth.clara
  (:require [clara.rules :refer :all])
  )


(defrecord SupportRequest [client level])

(defrecord ClientRepresentative [name client])

(defn zz [nn]
  (println nn)
  (+ 1 2)
  )

(defrule is-important
  "Find important support requests."
  [SupportRequest (= :high level)]
  =>
  (println "High support requested!"))

(defrule notify-client-rep
  "Find the client representative and request support."
  [SupportRequest (= ?client client)]
  [ClientRepresentative (= ?client client) (= ?name name)(= (zz ?name) 5)]
  =>
  (println "Notify" ?name "that"
           ?client "has a new support request!"))

