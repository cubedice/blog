(ns blog.controller
  (:require [blog.views :as views])
  (:use blog.models)
  (:use blog.database)
  (:use compojure))

(defn home []
  (views/home))

(defn create-post 
  ([]
     (views/create-post))
  ([params]
     (do
       ;;TODO: publish post and redirect to view
       (INSERT :posts {:title (params :title) :body (params :body)})
       (redirect-to "/")
     )))