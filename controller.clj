(ns blog.controller
  (:require [blog.views :as views])
  (:require [blog.models :as models])
  (:use blog.database)
  (:use compojure))

(defn home []
  (views/home))

(defn create-post 
  ([]
     (views/create-post))
  ([params]
       (INSERT :posts {:title (params :title) :body (params :body)})
       (redirect-to "/posts")
     ))
(defn all-posts []
  (views/all-posts (models/get-posts)))

(defn login [username password]
  (models/login-attempt username password))

(defn logout [sessionid]
  (models/logout sessionid))

(defn get-user-info [sessionid]
  (models/get-user-info sessionid))