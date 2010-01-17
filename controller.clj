(ns blog.controller
  (:require [blog.views :as views])
  (:require [blog.models :as models])
  (:use blog.database)
  (:use compojure)
  (:import BCrypt))

(defn home []
  (views/home (take 5 (models/get-posts))))

(defn create-post 
  ([sid]
    (let [user (models/get-user sid)]
      (if (and (not (nil? user)) (= (user :auth_level) "root"))
        (views/create-post)
        [403])))
  ([sid title body]
    (let [user (models/get-user sid)]
      (if (and (not (nil? user)) (= (user :auth_level) "root"))
        (INSERT :posts {:title title :body_markdown body 
			:body_html (models/markdown-to-html body) 
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
        (UPDATE :posts (post :id) {:body_markdown body 
				   :body_html (models/markdown-to-html body)}))
      (redirect-to (str "/posts/" (post :slug))))))

(defn post-comment [name url comment post-slug]
  (INSERT :comments {:name (if (blank? name) "anonymous coward" name)
		     :url (if (blank? url) "#" url)
		     :body_markdown comment 
		     :body_html (models/markdown-to-html comment) :post_slug post-slug})
  (redirect-to (str "/posts/" post-slug)))


(defn new-user
  ([]
     (views/create-user))
  ([username password link]
     (let [user (models/create-user username password link)]
       (if (not (nil? user))
	 (redirect-to "/")
	 (views/create-user "username already exists")))))

(defn view-all-posts []
  (views/view-all-posts (models/get-posts)))

(defn view-post [slug]
  (let [post (models/get-post slug)
	comments (models/get-comments post)]
    (views/view-post post comments)))

(defn login [username password]
  (models/login-attempt username password))

(defn logout [sessionid]
  (models/logout sessionid))

(defn get-user-info [sessionid]
  (models/get-user-info sessionid))

(defn edit-user-info 
  ([sessionid]
     (let [user (models/get-user sessionid)]
       (if (not (nil? user))
	 (views/edit-user-info user)
	 (redirect-to "/"))))
  ([sessionid password link]
     (let [user (models/get-user sessionid)]
       (if (not (nil? user))
	 (if (not (empty? password))
	   (UPDATE :users (user :id) {:password (BCrypt/hashpw password (BCrypt/gensalt 12))
				      :url link})
	   (UPDATE :users (user :id) {:url link})))
       (edit-user-info sessionid))))