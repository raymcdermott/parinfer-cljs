(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'parinfer-test.core
   :output-to "out/parinfer_test.js"
   :output-dir "out"
   :foreign-libs [{:file "parinfer.js"
                   :provides ["parinfer"]
                   :module-type :commonjs}]})
