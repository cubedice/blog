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

(defn create-post []
  (html-doc "Create"
    (form-to [:post "/create-post"] 
      (label "title" "Title" )(text-field "title")[:br]
      (label "body" "Body") (text-area "body")[:br]
      (submit-button "Submit")
      )))