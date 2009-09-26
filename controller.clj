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
  (let [sessionid (models/login-attempt username password)]
    (if (not (nil? sessionid))
    [200 (str sessionid)]
    [200 "Incorrect Credentials"])))