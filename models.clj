;; Database models
(ns blog.models
  (:use clojure.contrib.sql))

(def posts
     [ :posts
       [:id :integer]
       [:id2 :integer "PRIMARY KEY"]
       [:something :text] ] )
(def oposts
     [ :posts2
       [:id :integer] ] )

(def current-models
     [posts oposts] )