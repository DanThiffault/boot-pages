(ns dthiffault.boot-pages.impl
  (:require 
    [net.cgrand.enlive-html :as html]
    [clojure.java.io :as io])) 

(defn generate-page [cfg layout-path output-dir rel-path]
  (let [template-fn (html/template 
                      (-> layout-path io/file html/html-resource)
                      [{title :title init-fn :init-fn}]
                      [:head :title] (html/content title)
                      [:body] (html/append 
                                (html/html [:script {:type "text/javascript"} init-fn])))]
    (spit (io/file output-dir rel-path)
          (reduce str (template-fn cfg)))))
