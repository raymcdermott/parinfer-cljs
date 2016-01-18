(ns parinfer.core
  (:require
    cljsjs.parinfer))

(defn- convert-changed-line [e]
  {:line-no (.-lineNo e)
   :line (.-line e)})

(defn- convert-error [e]
  (when e
    {:name (.-name e)
     :message (.-message e)
     :line-no (.-lineNo e)
     :x (.-x e)}))

(defn- convert-result [result]
  {:text (.-text result)
   :success? (.-success result)
   :changed-lines (mapv convert-changed-line (.-changedLines result))
   :error (convert-error (.-error result))})

(defn- convert-options [option]
  #js {:cursorX (:cursor-x option)
       :cursorLine (:cursor-line option)
       :cursorDx (:cursor-dx option)})

(def indent-mode* js/parinfer.indentMode)
(def paren-mode* js/parinfer.parenMode)

(defn indent-mode
  ([text] (convert-result (indent-mode* text)))
  ([text options] (convert-result (indent-mode* text (convert-options options)))))

(defn paren-mode
  ([text] (convert-result (paren-mode* text)))
  ([text options] (convert-result (paren-mode* text (convert-options options)))))
