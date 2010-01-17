(ns blog.routes
  (:use [compojure])
  (:require [blog.controller :as controller])
  (:require [blog.rss :as rss]))

(defroutes blog-routes
  (GET "/" 
    (controller/home))
  (GET "/create-post"
    (controller/create-post (cookies :user)))
  (POST "/create-post"
    (controller/create-post (cookies :user) (params :title) (params :body)))
  (GET "/posts"
    (controller/view-all-posts))
  (GET "/posts/:slug"
    (controller/view-post (params :slug)))
  (GET "/posts/:slug/edit"
    (controller/edit-post (first (cookies :user)) (params :slug)))
  (POST "/posts/:slug/edit"
    (controller/edit-post (first (cookies :user)) (params :slug) (params :body)))
  (POST "/posts/:slug/comment"
    (controller/post-comment (params :name) (params :link) (params :comment) (params :slug)))
  (GET "/create-account"
    (controller/new-user))
  (POST "/create-account"
    (controller/new-user (params :username) (params :password) (params :link)))
  (GET "/login" [{:headers {"Content-Type" "application/json"}}] 
    (controller/login (params :username) (params :password)))
  (GET "/logout" [{:headers {"Content-Type" "application/json"}}] 
    (controller/logout (params :sid)))
  (GET "/edit-account-info"
    (controller/edit-user-info (cookies :user)))
  (POST "/edit-account-info"
    (controller/edit-user-info (cookies :user)  (params :password) (params :link)))
  (GET "/get-user" [{:headers {"Content-Type" "application/json"}}]
    (controller/get-user-info (params :sid)))
  (GET "/feed"
    (rss/main-feed))
  (GET "/static/*"
    (or (serve-file "/Users/kevindavenport/workspace/blog/static/" (params :*)) :next))
  (ANY "*" 2
    (page-not-found)))