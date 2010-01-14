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
     [:div#userBar]
     [:div.content
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

(defn edit-post [post]
  (html-doc "Edit"
    (form-to [:post (str "/posts/" (:slug post) "/edit")]
      [:h1 (:title post)]
      (label "body" "Body")(text-area "body" (:body_markdown post))[:br]
      (submit-button "Submit"))))
      

(defn view-all-posts [posts]
  (html-doc "All Posts"
    (for [post posts]
      [:div [:h1 [:a {:href (str "/posts/" (:slug post))} (:title post)]]
      [:p (:body_html post)]])))

(defn view-post [post]
  (html-doc (:title post)
    [:div [:h1 (:title post)]
    [:p (:body_html post)]
    [:a {:href "/posts"} "back to directory"]]))