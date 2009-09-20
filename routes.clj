(ns blog.routes
  (:use [compojure])
  (:require [blog.controller :as controller]))
(defroutes blog-routes
  (GET "/*" 
    (html
     [:body "some text"])))
