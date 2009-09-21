;; Database models
(ns blog.models
  (:use blog.database)
  (:use clojure.contrib.sql))

(def posts
     [ :posts
     [:id :integer "PRIMARY KEY"]
     [:title "varchar(255)"]
     [:body :text]
     [:updated :timestamp "DEFAULT CURRENT_TIMESTAMP"]])

(def get-posts
	(sort-by :id > (SELECT "*" "posts")))

(def comments    
     [ :comments
     [:id :integer "PRIMARY KEY"]
     [:name "varchar(255)"]
     [:body :text]
     [:post_id :integer] ])

(def current-models
     [posts comments] )