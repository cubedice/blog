(ns blog.routes
  (:use [compojure])
  (:require [blog.controller :as controller]))

(defroutes blog-routes
  (GET "/" 
    (controller/home))
  (GET "/create-post"
    (controller/create-post (cookies :user)))
  (POST "/create-post"
    (controller/create-post (cookies :user) (params :title) (params :body)))
  (GET "/posts"
    (controller/all-posts))
  (GET "/new-user"
    (controller/new-user))
  (POST "/new-user"
    (controller/new-user (params :username) (params :password)))
  (GET "/login" [{:headers {"Content-Type" "application/json"}}] 
    (controller/login (params :username) (params :password)))
  (GET "/logout" [{:headers {"Content-Type" "application/json"}}] 
    (controller/logout (params :sid)))
  (GET "/get-user" [{:headers {"Content-Type" "application/json"}}]
    (controller/get-user-info (params :sid)))
  (GET "/static/*"
    (or (serve-file "/Users/kevindavenport/workspace/blog/static/" (params :*)) :next))
  (ANY "*" 2
    (page-not-found)))