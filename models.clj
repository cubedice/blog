;; Database models
(ns blog.models
  (:require [blog.database :as db])
  (:use clojure.contrib.sql)
  (:use clojure.contrib.json.write)
  (:require [clojure.contrib.str-utils2 :as s-u-2])
  (:use compojure.crypto)
  (:import BCrypt)
  (:import (org.mozilla.javascript Context ContextFactory ScriptableObject)))

(def posts
     [ :posts
     [:id :integer "PRIMARY KEY"]
     [:title "varchar(255)"]
     [:author "varchar(255)"]
     [:slug "varchar(255)"]
     [:body_markdown :text]
     [:body_html :text]
     [:updated :timestamp "DEFAULT CURRENT_TIMESTAMP"]])

(def comments    
     [ :comments
     [:id :integer "PRIMARY KEY"]
     [:name "varchar(255)"]
     [:url "varchar(255)"]
     [:body_markdown :text]
     [:body_html :text]
     [:post_slug "varchar(255)"] ])

(def users
     [ :users
     [:id :integer "PRIMARY KEY"]
     [:username "varchar(256) NOT NULL UNIQUE"]
     [:password "varchar(256)"]
     [:url "varchar(256)"]
     [:auth_level "varchar(256)"]
     [:last_login :integer]
     [:sessionid "varchar(256)"] ])


;; user f'ns

(defn create-user [username password link]
  (let [user {:username username :password (BCrypt/hashpw password (BCrypt/gensalt 12))
	      :url link}]
    (if (empty? (SELECT "*" "users" (str "username='" username "'")))
      (do
	(INSERT :users user)
	user)
      nil)))

(defn get-user 
  ([sessionid]
    (let [user (first (SELECT "*" "users" (str "sessionid='" sessionid "'")))]
      (if (and (not (nil? user)) (< (- (user :last_login) (. System currentTimeMillis)) 604800000))
	user)))
  ([username password]
    (let [user (first (SELECT "*" "users" (str "username='" username "'")))]
      (if (and (not (nil? user)) (BCrypt/checkpw password (user :password)))
        user))))     

(defn get-user-info [sid]
  (let [userinfo (get-user sid)]
    (if (nil? userinfo)
      (json-str nil)
      (json-str {:sessionid (str sid) :username (str (userinfo :username)) :authlevel (str (userinfo :auth_level)) :url (str (userinfo :url))}))))

(defn start-session [user]
  (let [key (gen-uuid) ctime (. System currentTimeMillis)]
    (UPDATE :users (user :id) {:sessionid key :last_login ctime})
    (json-str {:sessionid (str key) :username (str (user :username)) :authlevel (str (user :auth_level))})))

(defn login-attempt [username password]
  (let
      [user (get-user username password)]
    (if (not (nil? user))
      (start-session user)
      (json-str nil))))

(defn logout [sessionid]
  (let [user (get-user sessionid)]
    (UPDATE :users (user :id) {:sessionid ""})
    (json-str nil)))

;; post f'ns

;; from http://briancarper.net/blog/clojure-and-markdown-and-javascript-and-java-and
(defn markdown-to-html [txt]
  (let [safetxt (s-u-2/replace txt #"([^<]*\`.*\`[^<]*|[^\`^<]*)<[^>]*>" "$1")
	cfact (ContextFactory/getGlobal)
	context (.enterContext cfact)
	scope (.initStandardObjects context)
	input (Context/javaToJS txt scope)
	script (str (slurp "/home/cubedice/public_html/mikedavenport.net/htdocs/static/js/showdown.js")
		    "new Showdown.converter().makeHtml(input);")]
    (try
      (ScriptableObject/putProperty scope "input" input)
      (let [result (.evaluateString context scope script "<cmd>" 1 nil)]
	(Context/toString result))
      (finally (Context/exit)))))

(defn msanitize [txt]
  (s-u-2/replace txt #"<[^>]*>[^<]*<[^>]*>" ""))

(defn slugify [title]
  (s-u-2/replace (.toLowerCase title) #"\s+" "-"))
    
(defn get-posts []
  (sort-by :id > (SELECT "*" "posts")))

(defn get-post [slug]
  (first (SELECT "*" "posts" (str "slug='" slug "'"))))

(defn get-comments [post]
  (sort-by :id > (SELECT "*" "comments" (str "post_slug='" (post :slug) "'"))))


;; currently used models

(def current-models
     (vector posts comments users))