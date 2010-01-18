(ns blog.views
  (:use compojure.html))

(defn html-doc [title body & status]
  (html
   (doctype :html4)
   [:html
    [:head
     [:title "mikedavenport.net"(if (empty? title) "" (str " - " title))]
     (include-css "/static/css/main.css"
		  "/static/css/github.css")
     (include-js  "/static/js/highlight.pack.js"
		  "/static/js/showdown.js"
		  "/static/js/jquery.js"
		  "/static/js/users.js"
		  "/static/js/ui.js")]
    [:body 
     [:div.userbar]
     [:a {:href "#"} [:div.loginbutton "login/create account"]]
     [:div.content
      [:div.blogtitle [:a {:href "/"} "a weblog of" [:br] "computational enslavement"]]
      [:div.sidebar 
       [:p [:a {:href "/about"} "about blog"]]
       [:p [:a {:href "/me"} "about me"]]
       [:p [:a {:href "/posts"} "all posts"]]
       [:p [:a {:href "/feed"} "rss feed"]]]
      [:div.rightcol body]]]]))

;(defmacro table-form-to 

(defn home [recent-posts]
  (html-doc ""
	    [:div [:h2 "latest posts"]
	     (for [post recent-posts]
	       [:div [:h3 [:a {:href (str "/posts/" (:slug post))} (:title post)]]
		[:p.date (:updated post)]
		[:p (:body_html post)]])]))

(defn create-user [ & err ]
  (html-doc "create account"
    [:div [:div.error err] [:p "why? so that the comment form info can be autofilled, of course"]
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

(defn about-me []
  (html-doc "me"
	    [:div [:center [:h2 "Who?"]]
	     [:p "I am a computer engineering undergraduate at the University of Florida."
	         "  I will be finishing in May, at which point I will finally be able to live out my dream of simultaneously eating ice cream AND being a college graduate."]
	      [:p "More importantly, I want to be a maker.  All of my goals in life involve creating (not necessarily creatively, although it couldn't hurt).  It would be slightly more gratifying to see people entertained by something I have made than to see it fulfill a basic need."]
	     [:p "I would hate nothing more than to be referred to as a 'rock star' or 'ninja'.  I think of programming as akin to jazz improvisation.  The highest compliment to me is a comparison to Monk or Miles.  If one day I can achieve that sort of zen in my coding, I will then PROCEED to MELT YOUR FACE OFF with a " [:a {:href "http://www.youtube.com/watch?v=4RCI8D8avGI"} "killer guitar solo!"]]
	     [:br][:br]
	     [:p "ps - if you were thinking of contacting me, my email is davenport _DOT_ mike _AT_ gmail _DOT_ com. if you were thinking of hiring me, my resume is " [:a {:href "http://dl.dropbox.com/u/1402734/resume.pdf"} "here"]]]))

(defn about-blog []
  (html-doc "about this blog"
	    [:div [:center [:h2 "What?"]]
	     [:p "Welcome to the electron harvest, my friend!  There's a bumper crop out on the silicon plains, right past the bandgap.  All that's needed to reap the rewards is a small transistor army."]
	     [:p "*ahem*"]
	     [:p "This blog is mostly about using computers to make them serve you, not the other way around.  This is not meant to be a place for trivial code workarounds (although that can be fun).  Rather, it is a collection of insights into making lives more enjoyable with the aid of electronics."]]))