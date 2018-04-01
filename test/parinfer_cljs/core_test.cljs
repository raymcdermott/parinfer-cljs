(ns parinfer-cljs.core-test
  (:require [clojure.test :refer-macros [deftest is testing run-tests]]
            [parinfer-cljs.core :as parinfer]))

(def indent-result
  {:success      true
   :text         "(+ 1 2)"
   :paren-trails [{:line-no 0 :start-x 6 :end-x 7}]})

(deftest happy-path-no-options
  (testing "indent-mode"
    (is (= indent-result
           (parinfer/indent-mode "(+ 1 2)" parinfer/js->cljs-output-map))))

  (testing "paren-mode"
    (is (= indent-result
           (parinfer/paren-mode "(+ 1 2)" parinfer/js->cljs-output-map))))

  (testing "smart-mode"
    (is (= indent-result
           (parinfer/smart-mode "(+ 1 2)" parinfer/js->cljs-output-map)))))


(def unclosed-paren-result
  {:text  "(+ 1 2"
   :error {:name    :unclosed-paren
           :message "Unclosed open-paren."
           :line-no 0 :x 0}})

(deftest unbalanced-no-options
  (testing "indent-mode"
    (is (= indent-result
           (parinfer/indent-mode "(+ 1 2" parinfer/js->cljs-output-map))))

  (testing "paren-mode"
    (is (= unclosed-paren-result
           (parinfer/paren-mode "(+ 1 2" parinfer/js->cljs-output-map))))

  (testing "smart-mode"
    (is (= indent-result
           (parinfer/smart-mode "(+ 1 2" parinfer/js->cljs-output-map)))))

(def unclosed-result
  {:text  "(+ \"1 2)"
   :error {:name    :unclosed-quote
           :message "String is missing a closing quote."
           :line-no 0 :x 3}})

(deftest unclosed-quote-no-options
  (testing "indent-mode"
    (is (= unclosed-result
           (parinfer/indent-mode "(+ \"1 2)" parinfer/js->cljs-output-map))))

  (testing "paren-mode"
    (is (= unclosed-result
           (parinfer/paren-mode "(+ \"1 2)" parinfer/js->cljs-output-map))))

  (testing "smart-mode"
    (is (= unclosed-result
           (parinfer/smart-mode "(+ \"1 2)" parinfer/js->cljs-output-map)))))

; :name :eol-backslash, :message "Line cannot end in a hanging backslash."

(def eol-backslash-result
  {:text  "(+ 1 2)\\"
   :error {:name    :eol-backslash
           :message "Line cannot end in a hanging backslash."
           :line-no 0 :x 7}})

(deftest eol-backslash-no-options
  (testing "indent-mode"
    (is (= eol-backslash-result
           (parinfer/indent-mode "(+ 1 2)\\" parinfer/js->cljs-output-map))))

  (testing "paren-mode"
    (is (= eol-backslash-result
           (parinfer/paren-mode "(+ 1 2)\\" parinfer/js->cljs-output-map))))

  (testing "smart-mode"
    (is (= eol-backslash-result
           (parinfer/smart-mode "(+ 1 2)\\" parinfer/js->cljs-output-map)))))


(def unmatched-close-paren-fixed-result
  {:success      true
   :text         "(let [x 1])"
   :paren-trails [{:line-no 0 :start-x 9 :end-x 11}]})

(def unmatched-close-paren-result
  {:text  "(let [x 1)"
   :error {:name    :unmatched-close-paren
           :message "Unmatched close-paren."
           :line-no 0 :x 9
           :extra   {:line-no 0 :x 5}}})

(deftest unmatched-close-paren-test
  (testing "indent-mode"
    (is (= unmatched-close-paren-fixed-result
           (parinfer/indent-mode "(let [x 1)" parinfer/js->cljs-output-map))))

  (testing "paren-mode"
    (is (= unmatched-close-paren-result
           (parinfer/paren-mode "(let [x 1)" parinfer/js->cljs-output-map))))

  (testing "smart-mode"
    (is (= unmatched-close-paren-fixed-result
           (parinfer/smart-mode "(let [x 1)" parinfer/js->cljs-output-map)))))

(def quote-danger-result
  {:text  "(+ 1 2) ; \"bad comment"
   :error {:name    :quote-danger
           :message "Quotes must balanced inside comment blocks."
           :line-no 0 :x 10}})

(deftest quote-danger-paren-test
  (testing "indent-mode"
    (is (= quote-danger-result
           (parinfer/indent-mode "(+ 1 2) ; \"bad comment" parinfer/js->cljs-output-map))))

  (testing "paren-mode"
    (is (= quote-danger-result
           (parinfer/paren-mode "(+ 1 2) ; \"bad comment" parinfer/js->cljs-output-map))))

  (testing "smart-mode"
    (is (= quote-danger-result
           (parinfer/smart-mode "(+ 1 2) ; \"bad comment" parinfer/js->cljs-output-map)))))

(def align-result
  {:success      true
   :text         "(let) \n[x 1] \ny 2\n(+ x y)"
   :paren-trails [{:line-no 0 :start-x 4 :end-x 5}
                  {:line-no 1 :start-x 4 :end-x 5}
                  {:line-no 3 :start-x 6 :end-x 7}]})

(def align-parens-result
  {:success      true
   :text         "(let \n [x 1 \n  y 2]\n (+ x y))"
   :paren-trails [{:line-no 2 :start-x 5 :end-x 6}
                  {:line-no 3 :start-x 7 :end-x 9}]})

(deftest realign-test
  (testing "indent-mode"
    (is (= align-result
           (parinfer/indent-mode "(let \n[x 1 \ny 2]\n(+ x y))" parinfer/js->cljs-output-map))))

  (testing "paren-mode"
    (is (= align-parens-result
           (parinfer/paren-mode "(let \n[x 1 \ny 2]\n(+ x y))" parinfer/js->cljs-output-map))))

  (testing "smart-mode"
    (is (= align-result
           (parinfer/smart-mode "(let \n[x 1 \ny 2]\n(+ x y))" parinfer/js->cljs-output-map)))))


