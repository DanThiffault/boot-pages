(ns dthiffault.boot-pages
  {:boot/export-tasks true}
  (:require
    [clojure.java.io :as io]
    [boot.pod :as pod]
    [boot.core :as core]
    [boot.util :as util]
    [boot.tmpdir :as tmpd]
    [clojure.edn :as edn]))

(def ^:private deps
  '[[enlive "1.1.5"]])

(defn find-path [fileset filename]
  (->> fileset core/input-files (core/by-name [filename]) first tmpd/file .getPath))

(core/deftask pages
  "Create static html pages."
  []
  (let [output-dir (core/temp-dir!)
        pf (-> (core/get-env)
              (update-in [:dependencies] into deps)
              pod/make-pod
              future)
        last-pages (atom nil)]
      (core/with-pre-wrap fileset
        (let [cfg-files (->> fileset core/input-files (core/by-name ["pages.edn"]))
              cfg (-> cfg-files first tmpd/file io/reader (java.io.PushbackReader.) edn/read)]
          (util/info (str "Generating pages from layouts..." (count (:pages cfg))))
          (doseq [p (:pages cfg)]
            (let [page-cfg (merge (:defaults cfg) p)
                  layout-path (find-path fileset (:layout page-cfg))
                  file-path (:path page-cfg)]
            (pod/with-call-in @pf
              (dthiffault.boot-pages.impl/generate-page
                ~page-cfg
                ~layout-path
                ~(.getPath output-dir)
                ~file-path)))))
        (-> fileset
            (core/add-resource output-dir)
            core/commit!))))

