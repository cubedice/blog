;; Database bindings and boilerplate fns for compojure's database model
(ns blog.database
  (:use clojure.contrib.sql) 
  (:use clojure.contrib.json.write) 
  (:use compojure)
  (:use (clojure.contrib
	 [def :only (defalias)]
	 [java-utils :only (as-str)])))

(def db 
     {:classname   "org.sqlite.JDBC"    
      :subprotocol "sqlite"    
      :subname     "/Users/kevindavenport/workspace/blog/database.sqlite"
      :user        "cubedice"
      :password    "blipvert"})

(defn create-tables [modelcoll]
  "Create database tables"
  (for [model modelcoll] 
    (with-connection blog.database/db
      (let [name (first model)
	    specs (rest model)]
	(do-commands 
	 (format "CREATE TABLE %s (%s)"
		 (as-str name)
		 (apply str
		   (map as-str
		     (apply concat
		       (interpose [", "]
		         (map (partial interpose " ") specs)))))))))))

(defn delete-tables [modelcoll]
  "Drop database tables"
  (for [model modelcoll] 
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
     (ask-sql db (str "select " what " from " place)))
  ([what place where]
     (ask-sql db (str "select " what " from " place " where " where))))

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
    (update-values table ["id=?" id] a-map)))

(defn REMOVE
  "A generic delete function -- based on id."
  [table id]
  (with-connection db
    (delete-rows table ["id=?" id])))
