(ns parinfer-cljs.core-test
  (:require [clojure.test :refer-macros [deftest is testing run-tests]]
            [parinfer-cljs.core :as parinfer]))

(def indent-result
  {:success      true
   :text         "(+ 1 2)"
   :paren-trails [{:line-no 0 :start-x 6 :end-x 7}]})

(deftest happy-path-no-options
  (testing "indent-mode"
    (is (= indent-result (parinfer/indent-mode "(+ 1 2)"))))

  (testing "paren-mode"
    (is (= indent-result (parinfer/paren-mode "(+ 1 2)"))))

  (testing "smart-mode"
    (is (= indent-result (parinfer/smart-mode "(+ 1 2)")))))


(def unclosed-paren-result
  {:text  "(+ 1 2"
   :error {:name    :unclosed-paren
           :message "Unclosed open-paren."
           :line-no 0 :x 0}})

(deftest unbalanced-no-options
  (testing "indent-mode"
    (is (= indent-result (parinfer/indent-mode "(+ 1 2"))))

  (testing "paren-mode"
    (is (= unclosed-paren-result (parinfer/paren-mode "(+ 1 2"))))

  (testing "smart-mode"
    (is (= indent-result (parinfer/smart-mode "(+ 1 2")))))

(def unclosed-result
  {:text  "(+ \"1 2)"
   :error {:name    :unclosed-quote
           :message "String is missing a closing quote."
           :line-no 0 :x 3}})

(deftest unclosed-quote-no-options
  (testing "indent-mode"
    (is (= unclosed-result (parinfer/indent-mode "(+ \"1 2)"))))

  (testing "paren-mode"
    (is (= unclosed-result (parinfer/paren-mode "(+ \"1 2)"))))

  (testing "smart-mode"
    (is (= unclosed-result (parinfer/smart-mode "(+ \"1 2)")))))

; :name :eol-backslash, :message "Line cannot end in a hanging backslash."

(def eol-backslash-result
  {:text  "(+ 1 2)\\"
   :error {:name    :eol-backslash
           :message "Line cannot end in a hanging backslash."
           :line-no 0 :x 7}})

(deftest eol-backslash-no-options
  (testing "indent-mode"
    (is (= eol-backslash-result (parinfer/indent-mode "(+ 1 2)\\"))))

  (testing "paren-mode"
    (is (= eol-backslash-result (parinfer/paren-mode "(+ 1 2)\\"))))

  (testing "smart-mode"
    (is (= eol-backslash-result (parinfer/smart-mode "(+ 1 2)\\")))))


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
    (is (= unmatched-close-paren-fixed-result (parinfer/indent-mode "(let [x 1)"))))

  (testing "paren-mode"
    (is (= unmatched-close-paren-result (parinfer/paren-mode "(let [x 1)"))))

  (testing "smart-mode"
    (is (= unmatched-close-paren-fixed-result (parinfer/smart-mode "(let [x 1)")))))

(def quote-danger-result
  {:text  "(+ 1 2) ; \"bad comment"
   :error {:name    :quote-danger
           :message "Quotes must balanced inside comment blocks."
           :line-no 0 :x 10}})

(deftest quote-danger-paren-test
  (testing "indent-mode"
    (is (= quote-danger-result (parinfer/indent-mode "(+ 1 2) ; \"bad comment"))))

  (testing "paren-mode"
    (is (= quote-danger-result (parinfer/paren-mode "(+ 1 2) ; \"bad comment"))))

  (testing "smart-mode"
    (is (= quote-danger-result (parinfer/smart-mode "(+ 1 2) ; \"bad comment")))))

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
    (is (= align-result (parinfer/indent-mode "(let \n[x 1 \ny 2]\n(+ x y))"))))

  (testing "paren-mode"
    (is (= align-parens-result (parinfer/paren-mode "(let \n[x 1 \ny 2]\n(+ x y))"))))

  (testing "smart-mode"
    (is (= align-result (parinfer/smart-mode "(let \n[x 1 \ny 2]\n(+ x y))")))))

(def cursor-pos-result
  {:success true
   :text "(+ 1 2)"
   :cursor-x 5
   :cursor-line 0
   :paren-trails [{:line-no 0 :start-x 6 :end-x 7}]})

(def cursor-pos-options {:cursor-x 5 :cursor-line 0})

(deftest happy-path-cursor-options
  (testing "indent-mode"
    (is (= cursor-pos-result (parinfer/indent-mode "(+ 1 2)" cursor-pos-options))))

  (testing "paren-mode"
    (is (= cursor-pos-result (parinfer/paren-mode "(+ 1 2)" cursor-pos-options))))

  (testing "smart-mode"
    (is (= cursor-pos-result (parinfer/smart-mode "(+ 1 2)" cursor-pos-options)))))


(def cursor-nl-pos-result
  {:success true
   :text "(let [x 1]) \n(+ x 2)"
   :cursor-x 5
   :cursor-line 1
   :tab-stops [{:x 0 :line-no 0 :ch "("}
               {:x 5 :arg-x 8 :line-no 0 :ch "["}]
   :paren-trails [{:line-no 0 :start-x 9 :end-x 11}
                  {:line-no 1 :start-x 6 :end-x 7}]})

(def cursor-nl-pos-result-paren-mode
  {:success true
   :text "(let [x 1] \n (+ x 2))"
   :cursor-x 6
   :cursor-line 1
   :tab-stops [{:x 0 :line-no 0 :ch "("}
               {:x 5 :arg-x 8 :line-no 0 :ch "["}]
   :paren-trails [{:line-no 0 :start-x 9 :end-x 10}
                  {:line-no 1 :start-x 7 :end-x 9}]})

(def cursor-nl-pos-options {:cursor-x 5 :cursor-line 1})

(deftest happy-path-cursor-nl-options
  (testing "indent-mode"
    (is (= cursor-nl-pos-result (parinfer/indent-mode "(let [x 1] \n(+ x 2))" cursor-nl-pos-options))))

  (testing "paren-mode"
    (is (= cursor-nl-pos-result-paren-mode (parinfer/paren-mode "(let [x 1] \n(+ x 2))" cursor-nl-pos-options))))

  (testing "smart-mode"
    (is (= cursor-nl-pos-result (parinfer/smart-mode "(let [x 1] \n(+ x 2))" cursor-nl-pos-options)))))

(def fix-result {:success      true
                 :text         "(def foo [a b])"
                 :paren-trails [{:line-no 0 :start-x 13 :end-x 15}]})

(def fix-paren {:text  "(def foo [a b"
                :error {:name :unclosed-paren :message "Unclosed open-paren." :line-no 0 :x 9}})

(def partial-result {:success      true
                     :text         "(let [x 1]\n (+ x 2))"
                     :paren-trails [{:line-no 0 :start-x 9 :end-x 10}
                                    {:line-no 1 :start-x 7 :end-x 9}]})

(deftest happy-path-fix-unmatched
  (testing "indent-mode"
    (is (= fix-result (parinfer/indent-mode "(def foo [a b"))))

  (testing "paren-mode"
    (is (= fix-paren (parinfer/paren-mode "(def foo [a b"))))

  (testing "smart-mode"
    (is (= fix-result (parinfer/smart-mode "(def foo [a b"))))

  (testing "partial paren-mode"
    (is (= partial-result (parinfer/paren-mode "(let [x 1]\n(+ x 2))" {:partial-result true})))))



(def cursor-before-fixed-result
  {:success      true
   :text         "(def foo [a b])"
   :cursor-x     0
   :cursor-line  0
   :paren-trails [{:line-no 0, :start-x 13, :end-x 15}]})

(def cursor-before-fixed-paren
  {:text "(def foo [a b"
   :cursor-x 0
   :cursor-line 0
   :error {:name :unclosed-paren
           :message "Unclosed open-paren."
           :line-no 0
           :x 9}})

(def cursor-before-fixed-options {:cursor-x 0 :cursor-line 0})

(deftest happy-path-fix-unmatched-options
  (testing "indent-mode"
    (is (= cursor-before-fixed-result (parinfer/indent-mode "(def foo [a b" cursor-before-fixed-options))))

  (testing "paren-mode"
    (is (= cursor-before-fixed-paren (parinfer/paren-mode "(def foo [a b" cursor-before-fixed-options))))

  (testing "smart-mode"
    (is (= cursor-before-fixed-result
          (parinfer/smart-mode "(def foo [a b" cursor-before-fixed-options)))))

