;; Start/stop server fns
(ns blog.runserver
  (:use blog.routes)
  (:require [blog.models :as models])
  (:require [blog.database :as database])
  (:use compojure))

(defserver server
  {:port 9095} "/*" (servlet blog-routes))

(defn run-init [] 
  (database/create-tables models/current-models)
    ;(Println "returned from create-tables fn")
  (start server))
    ;(println "started server"))
(defn up []
  (start server))
(defn down []
  (stop server))