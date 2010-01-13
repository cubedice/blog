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
      [:div.blogtitle "a weblog of computational enslavement"]
      [:div.sidebar "linkage"]
      [:div.rightcol body]]]]))

(defn home []
  (html-doc "Welcome"
	    "some text!"))

(defn create-user []
  (html-doc "New User"
    (form-to [:post "/new-user"]
      (label "username" "Username")(text-field "username")[:br]
      (label "password" "Password")(text-field "password")[:br]
      (submit-button "Create"))))
      

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