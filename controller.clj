(ns blog.controller
  (:require [blog.views :as views])
  (:require [blog.models :as models])
  (:use blog.database)
  (:use compojure))

(defn home []
  (views/home))

(defn create-post 
  ([sid]
    (let [user (models/get-user sid)]
     (if (and (not (nil? user)) (= (user :auth_level) "root"))
       (views/create-post)
       [403])))
  ([sid title body]
    (let [user (models/get-user sid)]
      (if (and (not (nil? user)) (= (user :auth_level) "root"))
        (INSERT :posts {:title title :body body}))
      (redirect-to "/posts"))))

(defn create-user
  ([]
     (views/create-user))
  ([username password]
     (models/create-user username password)
     (views/home)))

(defn all-posts []
  (views/all-posts (models/get-posts)))

(defn login [username password]
  (models/login-attempt username password))

(defn logout [sessionid]
  (models/logout sessionid))

(defn get-user-info [sessionid]
  (models/get-user-info sessionid))