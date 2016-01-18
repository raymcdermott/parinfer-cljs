(ns parinfer-test.core
  (:require
    [clojure.browser.repl :as repl]
    [parinfer.core :refer [indent-mode paren-mode]]))

(def parinfer js/parinfer)

;; (defonce conn
;;   (repl/connect "http://localhost:9000/repl"))

(enable-console-print!)

(println "Hello world!")
(println (:text (indent-mode "(foo\nbar)")))
(println (:text (paren-mode "(foo\nbar)")))
