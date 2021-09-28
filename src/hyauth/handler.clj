(ns hyauth.handler
 ;; (:use ring.middleware.json-params)
 ;; (:use ring.adapter.jetty)
  (:use ring.middleware.json-params)
  (:use ring.middleware.params)
  (:require [hyauth.pdp :as pdp])
  (:require [hyauth.prp :as prp])
  (:require [compojure.core :refer :all]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-json.core :as json-core]
            [clojure.data.json :as json]
            [hyauth.routes.home :refer [home-routes]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]))

(defn init []
  (println "hyauth is starting"))

(defn destroy []
  (println "hyauth is shutting down"))

(defn json-response [data & [status]]
  {:status (or status (:code data))
   :headers {"Content-Type" "application/json"}
   :body (json-core/generate-string data)})

(defmacro format-response [resp]
  `(try (let [res# ~resp]
          {:status 200 :headers {"Content-Type" "application/json"} :body res#}
          )
          (catch Exception e# (println (.getMessage e#)) {:status 500 :body (.getMessage e#)}))
  )

(defroutes app-routes
           (route/resources "/")
           (GET "/test" req
             (do (println "HEAD" (:headers req))(println "GET /test " (slurp(:body req)))(json-response {"coucou" "lala"})))
           (GET "/init" []
             (pdp/init)
             )
           (GET "/policies" []
             (format-response (json/write-str (prp/get-policies)))
             )
           (GET "/policy/:resourceClass" [resourceClass]
             (format-response (json/write-str (prp/getPolicy resourceClass nil))))

           (GET "/whoAuthorized/:resourceClass" [resourceClass]
             (do (format-response (json/write-str (pdp/whoAuthorized {:resource {:class resourceClass}}))))
             )

           (POST "/isAuthorized" {body :body}
               (format-response (json/write-str (pdp/isAuthorized (json/read-str (slurp body) :key-fn keyword))))
               )

           (POST "/whoAuthorized" {body :body}
             (let [input-json (json/read-str (slurp body) :key-fn keyword)
                   result (pdp/whoAuthorized input-json)
                   ]
               (format-response (json/write-str result))
               ))

           (POST "/whichAuthorized" {body :body}
               (format-response (json/write-str (pdp/whichAuthorized (json/read-str (slurp body) :key-fn keyword))))
               )


           (PUT "/policy/:resourceClass"
             {params :params body :body}
             (format-response (json/write-str (prp/submit-policy (:resourceClass params) (slurp body))))
                                                         )
           (DELETE "/policy/:resourceClass" [resourceClass]
             (format-response (json/write-str (prp/delete-policy resourceClass)))
                   )

           (POST "/astro" {body :body}
             (println (slurp body))
             (format-response (json/write-str {:signe "poisson" :ascendant "poisson"}))
             )

           (GET "/explain" {body :body}
             (println "TODO") ;; //TODO
             )
           (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      wrap-params
      wrap-json-params
      wrap-multipart-params
      (wrap-base-url)))
