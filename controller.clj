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
        (INSERT :posts {:title title :body_markdown body :body_html (models/markdown-to-html body) 
			:author user :slug (models/slugify title)}))
      (redirect-to "/posts"))))

(defn edit-post 
  ([sid slug]
    (let [user (models/get-user sid)
	  post (models/get-post slug)]
      (if (and (not (nil? user)) (= (user :auth_level) "root"))
        (views/edit-post post)
        [403])))
  ([sid slug body]
    (let [user (models/get-user sid)
	  post (models/get-post slug)]
      (if (and (not (nil? user)) (= (user :auth_level) "root") (not (nil? post)))
        (UPDATE :posts (post :id) {:body_markdown body :body_html (models/markdown-to-html body)}))
      (redirect-to (str "/posts/" (post :slug))))))


(defn new-user
  ([]
     (views/create-user))
  ([username password]
     (models/create-user username password)
     (views/home)))

(defn view-all-posts []
  (views/view-all-posts (models/get-posts)))

(defn view-post [slug]
  (views/view-post (models/get-post slug)))

(defn login [username password]
  (models/login-attempt username password))

(defn logout [sessionid]
  (models/logout sessionid))

(defn get-user-info [sessionid]
  (models/get-user-info sessionid))