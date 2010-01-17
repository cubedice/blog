(ns blog.views
  (:use compojure.html))

(defn html-doc [title body & status]
  (html
   (doctype :html4)
   [:html
    [:head
     [:title title]
     (include-css "/static/css/main.css"
		  "/static/css/github.css")
     (include-js  "http://code.jquery.com/jquery-latest.js"
		  "/static/js/showdown.js"
		  "/static/js/highlight.pack.js"
		  "/static/js/users.js"
		  "/static/js/ui.js")]
    [:body 
     [:div.userbar]
     [:a {:href "#"} [:div.loginbutton "login/create account"]]
     [:div.content
      [:div.blogtitle [:a {:href "/"} "a weblog of computational enslavement"]]
      [:div.sidebar 
       [:p [:a {:href "/about"} "about blog"]]
       [:p [:a {:href "/me"} "about me"]]
       [:p [:a {:href "/posts"} "all posts"]]
       [:p [:a {:href "/feed"} "rss feed"]]]
      [:div.rightcol body]]]]))

;(defmacro table-form-to 

(defn home [recent-posts]
  (html-doc "welcome"
	    [:div [:h2 "latest posts"]
	     (for [post recent-posts]
	       [:div [:h3 [:a {:href (str "/posts/" (:slug post))} (:title post)]]
		[:p.date (:updated post)]
		[:p (:body_html post)]])]))

(defn create-user [ & err ]
  (html-doc "New User"
    [:div [:div.error err]
    (form-to [:post "/create-account"]
      [:table {:border 0}
       [:tr [:td (label "username" "username")][:td (text-field "username")]]
       [:tr [:td (label "password" "password")][:td (password-field "password")]]
       [:tr [:td (label "url" "url")][:td "http://" (text-field "link")]]
       [:tr [:td (submit-button "create")]]])]))

(defn edit-user-info [user]
  (html-doc "edit account"
   [:div [:h2 (:username user)]
   (form-to [:post "/edit-account-info"]
      [:table {:border 0}
       [:tr [:td (label "password" "password")][:td (password-field "password")]]
       [:tr [:td (label "url" "url")][:td (text-field "link")]]
       [:tr [:td (submit-button "submit changes")]]])]))
      

(defn create-post []
  (html-doc "Create"
    (form-to [:post "/create-post"] 
      [:table {:border 0}
       [:tr [:td (label "title" "title" )][:td (text-field "title")]]
       [:tr [:td (label "body" "body")][:td (text-area "body")]]
       [:tr [:td (label "preview" "preview")][:td {:class "markdownprev"}]]
       [:tr [:td (submit-button "submit")]]])))

(defn edit-post [post]
  (html-doc "Edit"
    (form-to [:post (str "/posts/" (:slug post) "/edit")]
      [:h1 (:title post)]
      [:table {:border 0}
       [:tr [:td (label "body" "body")][:td(text-area "body" (:body_markdown post))]]
       [:tr [:td (label "preview" "preview")][:td {:class "markdownprev"}]]
       [:tr [:td (submit-button "submit")]]])))
      

(defn view-all-posts [posts]
  (html-doc "all posts"
    (for [post posts]
      [:div [:h1 [:a {:href (str "/posts/" (:slug post))} (:title post)]]
      [:p.date (:updated post)]
      [:p (:body_html post)]])))

(defn view-post [post comments]
  (html-doc (:title post)
    [:div [:h1 (:title post)]
    [:p (:body_html post)]
     (for [comment comments]
      [:p [:a {:href (str "http://" (:url comment))} (:name comment)][:br]
       (:body_html comment)])
    (form-to [:post (str "/posts/" (:slug post) "/comment")]
      [:table {:border 0}
       [:tr [:td (label "name" "name")][:td (text-field "name")]]
       [:tr [:td (label "url" "url")][:td "http://" (text-field "link")]]
       [:tr [:td (label "comment" "comment")][:td (text-area "comment")]]
       [:tr [:td (label "preview" "preview")][:td {:class "markdownprev"}]]
       [:tr [:td (submit-button "submit")]]])
    [:a {:href "/posts"} "back to directory"]]))