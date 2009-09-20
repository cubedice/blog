(ns blog.views
  (:use compojure.html))

(defn html-doc [title & body]
  (html
   (doctype :html4)
   [:html
    [:head
     [:title title]
     (include-css "/static/css/main.css")]
    [:body body]]))

(defn home []
  (html-doc "Welcome"
	    "some text!"))