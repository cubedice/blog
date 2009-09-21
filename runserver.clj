;; Start/stop server fns
(ns blog.runserver
  (:use blog.routes)
  (:use blog.models)
  (:use blog.database)
  (:use compojure))

(defserver server
  {:port 9099} "/*" (servlet blog-routes))

(defn run-init [] 
  (do
    (create-db current-models)
    (start server)))
(defn up []
  (do (start server)))
(defn down []
  (do (stop server)))