(ns blog.controller
  (:require [blog.views :as views])
;;  (:require [blog.model :as model])
  (:use compojure.html)
)

(defn home []
  (views/home))