;; rss feed
(ns blog.rss
  (:use blog.models)
  (:use (compojure.html (gen)))
  (:import (java.text SimpleDateFormat)))

(defn rss [title url description & body]
  (html "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
        [:rss {:version "2.0"               
	       :xmlns:content "http://purl.org/rss/1.0/modules/content/"
               :xmlns:wfw "http://wellformedweb.org/CommentAPI/"
               :xmlns:dc " http://purl.org/dc/elements/1.1/"}
	 [:channel
	  [:title title]
	  [:link url]
	  [:description description]
	  body]]))

(defn rss-item [post]
  (html
   [:item
    [:title (:title post)]
    [:link (str "http://www.mikedavenport.net/posts/" (:slug post))]
    [:guid (str "http://www.mikedavenport.net/posts/" (:slug post))]
    [:pubDate (.format (SimpleDateFormat. "EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z") 
		       (.parse (SimpleDateFormat. "yyyy-MM-dd hh:mm:ss") (:updated post)))]
    [:description (escape-html (:body_html post))]]))

(defn main-feed []
  (rss
      "mikedavenport.net"
      "http://www.mikedavenport.net"
      "a weblog of computational enslavement"
    (map rss-item (take 25 (get-posts)))))