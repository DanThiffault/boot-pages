(set-env!
  :resource-paths #{"src"}
  :dependencies `[[org.clojure/clojure "1.6.0" :scope "provided"]
                  [boot/core "2.0.0-rc6" :scope "provided"]
                  [adzerk/bootlaces "0.1.8" :scope "test"]])


(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
  pom {:project     'dthiffault/boot-pages
       :version     +version+
       :description "Boot task to create static pages from a layout with asset hashing support"
       :url         "https://github.com/DanThiffault/boot-pages"
       :scm         {:url "https://github.com/DanThiffault/boot-pages"}      
       :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask dev
  "Dev process"
  []
  (comp 
    (watch)
    (repl :server true)
    (pom)
    (jar)
    (install)))
