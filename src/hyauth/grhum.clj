(ns hyauth.grhum
  (:require [clojure.java.jdbc :as jdbc]
            [hyauth.personne :as pers]
         )
  )


(def db {:classname "oracle.jdbc.OracleDriver"  ; must be in classpath
         :subprotocol "oracle"
         :subname "thin:@bdoradev4.in.asso-cocktail.org:5221:btagfc01"  ; If that does not work try:   thin:@172.27.1.7:1521/SID
         :user "GRHUM"
         :password "pwd"})


(defn membreDeStructure [subj struct]
  (let [res (jdbc/query db [(str "SELECT * FROM STRUCTURE_ULR,REPART_STRUCTURE WHERE STRUCTURE_ULR.LL_STRUCTURE=" struct
                                 " AND REPART_STRUCTURE.PERS_ID=" subj " AND REPORT_STRUCTURE.c_structure=STRUCTURE_ULR.C_STRUCTURE")])
        ]
    )
  )

(defrecord GrhumPersonneRepository [dbconn]
  pers/PersonneRepository
  (getById [id]
    (let [res (jdbc/query db [(str "SELECT * FROM INDIVIDU_ULR WHERE PERS_ID='" id "'")])])
    )
  )
