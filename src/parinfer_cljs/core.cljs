(ns parinfer-cljs.core
  (:require [cljsjs.parinfer]))

(defn convert-result
  "Convert the results of parinfer JS to idiomatic kebab-style Clojure maps."
  [result x-map]
  (into {} (map (fn [[k v]]
                  (cond
                    (= :name k) (when-let [from-js (aget result (name k))]
                                  [k (get v from-js)])
                    (keyword? v) (when-let [from-js (aget result (name k))]
                                   [v from-js])

                    (map? v) (when-let [from-js (aget result (name k))]
                               [k (convert-result from-js v)])

                    (coll? v) (when-let [from-js (aget result (name k))]
                                (let [result (if (= (type from-js) js/Object)
                                               (convert-result from-js (last v))
                                               (mapv #(convert-result % (last v)) from-js))]
                                  [(first v) result]))))
                x-map)))

(defn- convert-options
  "Convert a map of options expressed with idiomatic CLJS kebab keywords to a JS object."
  [options mapping]
  (js-obj (into {} (map (fn [[k v]]
                          (when-let [mapping (k mapping)]
                            [mapping v])) options))))

;; Options mappings as of 3.x
(def ^:private cljs->js-options-map
  {:cursor-line          :cursorLine
   :cursor-x             :cursorX
   :prev-cursor-line     :prevCursorLine
   :prev-cursor-x        :prevCursorX
   :selection-start-line :selectionStartLine
   :changes              :changes
   :force-balance        :forceBalance
   :partial-result       :partialResult})

;; Output mappings as of 3.x
(def ^:private js->cljs-extra
  {:lineNo :line-no
   :x      :x})

(def ^:private js->cljs-error
  {:name    {"quote-danger"          :quote-danger
             "eol-backslash"         :eol-backslash
             "unclosed-quote"        :unclosed-quote
             "unclosed-paren"        :unclosed-paren
             "unmatched-close-paren" :unmatched-close-paren
             "unhandled"             :unhandled}
   :message :message
   :lineNo  :line-no
   :x       :x
   :extra   [:extra js->cljs-extra]})

(def ^:private js->cljs-tabstop
  {:x      :x
   :argX   :arg-x
   :lineNo :line-no
   :ch     :ch})

(def ^:private js->cljs-paren-trails
  {:lineNo :line-no
   :startX :start-x
   :endX   :end-x})

(def ^:private js->cljs-output-map
  {:success     :success
   :text        :text
   :cursorX     :cursor-x
   :cursorLine  :cursor-line
   :error       [:error js->cljs-error]
   :tabStops    [:tab-stops js->cljs-tabstop]
   :parenTrails [:paren-trails js->cljs-paren-trails]})

(def indent-mode* js/parinfer.indentMode)
(def paren-mode* js/parinfer.parenMode)
(def smart-mode* js/parinfer.smartMode)

(defn- mode
  ([text parinfer-mode]
   (-> text
       parinfer-mode
       (convert-result js->cljs-output-map)))
  ([text parinfer-mode options]
   (-> text
       (parinfer-mode (convert-options options cljs->js-options-map))
       (convert-result js->cljs-output-map))))

(defn indent-mode
  ([text]
   (mode text indent-mode*))
  ([text options]
   (mode text indent-mode* options)))

(defn paren-mode
  ([text]
   (mode text paren-mode*))
  ([text options]
   (mode text paren-mode* options)))

(defn smart-mode
  ([text]
   (mode text smart-mode*))
  ([text options]
   (mode text smart-mode* options)))






