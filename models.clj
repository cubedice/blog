;; Database models
(ns blog.models
  (:use blog.database)
  (:use clojure.contrib.sql)
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
     [:sessionid "varchar(256)"] ])

(defn create-user [username & password]
;TODO: hash pw and salt, store in db
)

(defn verify-user [username password]
  (let [user (first (SELECT "*" "users" (str "username='" username "'")))]
    (if (and (not (nil? user)) (= (user :password password)))
      user)))

(defn get-user [sessionid]
  ())

(defn start-session [user]
;TODO: get random hash for sessionID
  (let [key (secure-random-bytes 16)]
    (UPDATE :users (user :id) {:sessionid key})
    key))

(defn end-session [sessionid]
)

(defn login-attempt [username password]
  (let
      [user (verify-user username password)]
    (if (not (nil? user))
      (start-session user))))

(defn logout [sessionid]
  ())


      
    


(def current-models
     [posts comments users] )