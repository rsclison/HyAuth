(ns hyauth.jsonrule
  (:require [clojure.string :as str]
            [hyauth.jsonpath :as js]
            [hyauth.pip :as pip]
            [hyauth.prp :as prp]
            [hashp.core]
            )
  (:use clojure.test)
  )

;; a context is like {:class :student :name "john" :age 21}
;; callPip return a map which is like a context. In fact alaways return the object map with the attribute included
;; so for a person pip which is called for the age attribute the result of callPip should be like {:class :person :id "fziffo2343" :name "John" :age 33 }
;; resolveAttr then gets the right attribute in this map
(defn resolveAttr [ctxt att]
  (if (map? ctxt)
    (get (pip/callPip (prp/findPip (:class ctxt) att) ctxt att) (keyword att))
    (throw (Exception. "Not an object"))))



(defn walkResolveJPath [path context]
  (let [pathcol (rest (str/split path #"\."))] ;; omit the $
    (reduce #(resolveAttr %1 %2) context pathcol)))

;; ctxt is a map with the primary and the secondary context of evaluation
;; 
#_(defn evalOperand [op subjOrRess ctxt]
  (if-not (= (subs op 0 1) "$")
    op
    (case (subs op 0 2)
      "$." (js/at-path op (if (= :subject subjOrRess)
                            (:subject ctxt)
                            (:resource ctxt)))
      "$r" (js/at-path (str "$" (subs op 2)) (:resource ctxt))
      "$s" (js/at-path (str "$" (subs op 2)) (:subject ctxt)))))

(defn evalOperand [op subjOrRess ctxt]
  (if-not (= (subs op 0 1) "$")
    op  ;; scalar value
    (case (subs op 0 2)
      "$." (walkResolveJPath op (if (= :subject subjOrRess)
                                  (:subject ctxt)
                                  (:resource ctxt)))
      "$r" (walkResolveJPath (str "$" (subs op 2)) (:resource ctxt))
      "$s" (walkResolveJPath (str "$" (subs op 2)) (:subject ctxt)))))
  


(defn evalClause [[operator op1 op2] ctxt subjOrRess]
  (let [opv1 (evalOperand op1 subjOrRess ctxt)
        opv2 (evalOperand op2 subjOrRess ctxt)
        func (resolve(symbol "hyauth.attfun" operator))
        ]
    (apply func [op1 op2])
    )
  )

(deftest evalClause-testscalar
        (is (= (evalClause [">" "1" "2"] {:class :toto :a 1 :b 2} :subject)
               true)))

;; a request is like : {:subject {:id "Mary", :role "Professeur"} :resource {:class "Note"} :operation "lire" :context {:date "2019-08-14T04:03:27.456"}}
;; catch Exception while evaluating
(defn evaluateRule [rule request]
  (let [subject (:subject rule) resource (:resource rule)]
    
    )
)


