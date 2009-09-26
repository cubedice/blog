;; Start/stop server fns
(ns blog.runserver
  (:use blog.routes)
  (:require [blog.models :as models])
  (:require [blog.database :as database])
  (:use compojure))

(defserver server
  {:port 9099} "/*" (servlet blog-routes))

(defn run-init [] 
    (database/create-tables models/current-models)
    (start server))
(defn up []
  (start server))
(defn down []
  (stop server))