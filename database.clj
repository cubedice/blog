;; Database bindings and boilerplate fns for compojure's database model
(ns blog.database
  (:require [clojure.contrib.sql :as sql]) 
  (:use clojure.contrib.json.write) 
  (:use compojure))

(def db 
     {:classname   "org.sqlite.JDBC"    
      :subprotocol "sqlite"    
      :subname     "/Users/kevindavenport/workspace/blog/database.sqlite"
      :user        "cubedice"
      :password    "blipvert"})

(defn create-db [modelcoll]
  "Create database tables" 
  (for [model modelcoll] 
    (sql/with-connection blog.database/db
      (sql/create-table
	 (first model)
	 (rest model)))))
