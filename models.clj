;; Database models
(ns blog.models
  (:use blog.database)
  (:use clojure.contrib.sql)
  (:use clojure.contrib.json.write)
  (:use compojure.crypto))

(def posts
     [ :posts
     [:id :integer "PRIMARY KEY"]
     [:title "varchar(255)"]
     [:body :text]
     [:updated :timestamp "DEFAULT CURRENT_TIMESTAMP"]])

(defn get-posts []
     (sort-by :id > (SELECT "*" "posts")))

(def comments    
     [ :comments
     [:id :integer "PRIMARY KEY"]
     [:name "varchar(255)"]
     [:body :text]
     [:post_id :integer] ])

(def users
     [ :users
     [:id :integer "PRIMARY KEY"]
     [:username "varchar(256)"]
     [:password "varchar(256)"]
     [:auth_level "varchar(256)"]
     [:sessionid "varchar(256)"] ])

(defn create-user [username & password]
;TODO: hash pw and salt, store in db
)

(defn get-user 
  ([sessionid]
    (first (SELECT "*" "users" (str "sessionid='" sessionid "'"))))
  ([username password]
    (let [user (first (SELECT "*" "users" (str "username='" username "'")))]
      (if (and (not (nil? user)) (= (user :password) password))
        user))))
     

(defn get-user-info [sid]
  (let [userinfo (get-user sid)]
    (if (nil? userinfo)
      (json-str nil)
      (json-str {:sessionid (str sid) :username (str (userinfo :username)) :authlevel (str (userinfo :auth_level))}))))

(defn start-session [user]
  (let [key (secure-random-bytes 16)]
    (UPDATE :users (user :id) {:sessionid key})
    (json-str {:sessionid (str key) :username (str (user :username)) :authlevel (str (user :auth_level))})))

(defn end-session [sessionid]
)

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
      
    


(def current-models
     [posts comments users] )