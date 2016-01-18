(ns parinfer-test.core
  (:require
    [parinfer.core :refer [indent-mode paren-mode]]))

(enable-console-print!)

(println "Hello world!")
(println (:text (indent-mode "(foo\nbar)")))
(println (:text (paren-mode "(foo\nbar)")))
