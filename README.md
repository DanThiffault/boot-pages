# boot-pages
Clojure static page generation using boot. Generate multiple pages from a description and one or more layout files. 
Also takes care of appending cache-busting fingerprints (hashes) to marked static assets.

Include in an existing boot project by adding the following lines to build.boot
```clojure
(require
   '[dthiffault.boot-pages :refer [pages]])
```

Then create a file named pages.edn in your resource folder
```clojure
{:defaults {:layout "default.html"}
 :pages [{:path "index.html" :init-fn "todoapp.run()" :title "Welcome to the site"}
         {:path "myotherpage.html" :init-fn "settings.run()" :title "Another page another title"}]}
```
Here :path is the target path you'd like a file created at. :init-fn will be placed within a script tag and appended to the bottom of the body tag. 
:title will be used for the page title. The :layout should be under resources/layouts/. All attributes can be defined in the :defaults section and overriden per page.

In order to generate fingerprinted urls include something like the following in your layout:
```html
<script type="text/javascript" src="${scripts/myscript.js}"></script> 
```
Everything withing ${} will be treated as an asset and replaced with a string including a hash of the filename. The file should be relative to the target directory.
