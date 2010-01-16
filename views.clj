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
		  "/static/js/showdown.js"
		  "/static/js/users.js"
		  "/static/js/ui.js")]
    [:body 
     [:div#userBar]
     [:div.content
      [:div.blogtitle "a weblog of computational enslavement"]
      [:div.sidebar "linkage"]
      [:div.rightcol body]]]]))

;(defmacro table-form-to 

(defn home []
  (html-doc "Welcome"
	    "some text!"))

(defn create-user []
  (html-doc "New User"
    (form-to [:post "/new-user"]
      [:table {:border 0}
       [:tr [:td (label "username" "username")][:td (text-field "username")]]
       [:tr [:td (label "password" "password")][:td (text-field "password")]]
       [:tr [:td (submit-button "create")]]])))
      

(defn create-post []
  (html-doc "Create"
    (form-to [:post "/create-post"] 
      [:table {:border 0}
       [:tr [:td (label "title" "title" )][:td (text-field "title")]]
       [:tr [:td (label "body" "body")][:td (text-area "body")]]
       [:tr [:td (label "preview" "preview")][:td {:class "markdownprev"}]]
       [:tr [:td (submit-button "submit")]]]
      )))

(defn edit-post [post]
  (html-doc "Edit"
    (form-to [:post (str "/posts/" (:slug post) "/edit")]
      [:h1 (:title post)]
      [:table {:border 0}
       [:tr [:td (label "body" "body")][:td(text-area "body" (:body_markdown post))]]
       [:tr [:td (label "preview" "preview")][:td {:class "markdownprev"}]]
       [:tr [:td (submit-button "submit")]]])))
      

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