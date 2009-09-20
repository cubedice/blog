;; Start/stop server fns
(ns blog.runserver
  (:require [blog.routes :as routes])
  (:use compojure))

(defserver server
  {:port 9099} "/*" (servlet routes/blog-routes))
(defn run-init [] 
  (do ;;TODO: db init 
    (start server)))
(defn up []
  (do (start server)))
(defn down []
  (do (stop server)))