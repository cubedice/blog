;; Start/stop server fns
(ns blog.runserver
  (:use blog.routes)
  (:require [blog.models :as models])
  (:require [blog.database :as database])
  (:use compojure))

(defserver server
  {:port 9098} "/*" (servlet blog-routes))

(defn run-init [] 
  (do
    (database/create-tables models/current-models)
    (start server)))
(defn up []
  (do (start server)))
(defn down []
  (do (stop server)))