(ns hyauth.jsonrule
  (:require [clojure.string :as str]
  [hyauth.jsonpath :as js])
  )



(defn walkResolveJPath [path context]
  (let [pathcol (rest(str/split path #"\."))] ;; omit the $
  (reduce #(+ %1 %2) context pathcol)
  ))

;; ctxt is a map with the primary and the secondary context of evaluation
;; 
(defn evalOperand [op subjOrRess ctxt]
  (if-not (= (subs op 0 1) "$")
       op
       (case (subs op 0 2)
         "$." (js/at-path op (if (= :subject subjOrRess)
                    (:subject ctxt)
                    (:resource ctxt)))
         "$r" (js/at-path (str "$" (subs op 2)) (:resource ctxt))
         "$s" (js/at-path (str "$" (subs op 2)) (:subject ctxt))
       )))


(defn evalClause [[operator op1 op2]]
  
  )

;; a request is like : {:subject {:id "Mary", :role "Professeur"} :resource {:class "Note"} :operation "lire" :context {:date "2019-08-14T04:03:27.456"}}
(defn evaluateRule [rule request]
  
)


