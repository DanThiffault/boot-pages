(ns dthiffault.boot-pages.impl
  (:require 
    [net.cgrand.enlive-html :as html]
    [clojure.java.io :as io]
    [pandect.algo.sha1 :refer [sha1-file]])) 

(defn fingerprint [asset]
  (let [filename (subs (str asset) 1)]
    (str filename "?v=" (sha1-file (str "target/" filename)))))

(defn generate-page [cfg layout-path output-dir rel-path]
  (let [template-fn (html/template 
                      (-> layout-path io/file html/html-resource)
                      [{title :title init-fn :init-fn}]
                      [:head :title] (html/content title)
                      [:body] (html/append 
                                (html/html [:script {:type "text/javascript"} init-fn]))
                      [html/any-node] (html/replace-vars fingerprint))]
    (spit (io/file output-dir rel-path)
          (reduce str (template-fn cfg)))))
