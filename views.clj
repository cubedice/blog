(ns blog.views
  (:use compojure.html))

(defn html-doc [title body & status]
  (html
   (doctype :html4)
   [:html
    [:head
     [:title title]
     (include-css "/static/css/main.css")
     (include-js  "http://code.jquery.com/jquery-latest.js"
		  "/static/js/jquery.cookie.js"
		  "/static/js/users.js")]
    [:body 
     [:div.content 
      [:div#userBar]
      body]]]))

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

(defn all-posts [posts]
  (html-doc "All Posts"
    (for [post posts]
      [:div [:h1 (:title post)]
      [:p (:body post)]])))