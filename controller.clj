(ns blog.controller
;;  (:require [blog.view :as view])
;;  (:require [blog.model :as model])
  (:use compojure.html)
)

(defn home []
  (html
   (doctype :html4)
      [:html
        [:head
          [:title "Hello World"]]
        [:body
          "Some body"]])) 