(ns blog.routes
  (:use [compojure])
  (:require [blog.controller :as controller]))

(defroutes blog-routes
  (GET "/" 
    (controller/home))
  (GET "/create-post"
    (controller/create-post))
  (POST "/create-post"
    (controller/create-post params))
  (GET "/posts"
    (controller/all-posts))
  (GET "/static/*"
    (or (serve-file "/Users/kevindavenport/workspace/blog/static/" (params :*)) :next))
  (ANY "*" 2
    (page-not-found)))