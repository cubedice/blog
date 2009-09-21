;; Database models
(ns blog.models
  (:use clojure.contrib.sql))
;; TODO: models should be... map, collection?
(def post
     [ :post
       [:id :integer "PRIMARY KEY"]
       [:title "varchar(255)"]
       [:body :text] ])

(def current-models
     [post] )