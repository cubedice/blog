;; Database bindings and boilerplate fns for compojure's database model
(ns blog.database
  (:use clojure.contrib.sql) 
  (:use clojure.contrib.json.write) 
  (:use compojure)
  (:require [clojure.contrib.str-utils2 :as s-u-2])
  (:use (clojure.contrib
	 [def :only (defalias)]
	 [java-utils :only (as-str)])))

(def db 
     {:classname   "org.sqlite.JDBC"    
      :subprotocol "sqlite"    
      :subname     "/home/cubedice/database/database.sqlite"
      :user        "cubedice"})

(defn create-tables [modelcoll]
  "Create database tables"
  (doseq [model modelcoll] 
    (with-connection db  
      (let [name (first model)
	    specs (rest model)]
	(do-commands 
	  (format "CREATE TABLE IF NOT EXISTS %s (%s)"
		 (as-str name)
		 (apply str
		   (map as-str
		     (apply concat
		       (interpose [", "]
		         (map (partial interpose " ") specs)))))))))))

(defn delete-tables [modelcoll]
  "Drop database tables"
  (doseq [model modelcoll] 
    (with-connection db
      (drop-table
	 (first model)))))

(defn ask-sql
  "Returns a seq of maps with the contents of an sql command."
  [db query]
  (with-connection db
    (with-query-results rs [query]
      (doall (map identity rs)))))

(defn SELECT
  "A generic select function."
  ([what place]
     (ask-sql db (str "select " (dbsanitize what) " from " (dbsanitize place))))
  ([what place where]
     (ask-sql db (str "select " (dbsanitize what) " from " (dbsanitize place) " where " 
		      (sanitize where)))))

(defn INSERT
  "A generic insert function."
  [table map]
  (with-connection db
    (insert-values table
      (keys map)
      (vals map))))

(defn UPDATE
  "A generic update function -- based on id."
  [table id a-map]
  (with-connection db
    (update-values table ["id=?" (dbsanitize id)] a-map)))

(defn REMOVE
  "A generic delete function -- based on id."
  [table id]
  (with-connection db
    (delete-rows table ["id=?" (dbsanitize id)])))

(defn dbsanitize [txt]
  (s-u-2/replace txt #"(;)"
		 ""))