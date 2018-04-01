(ns parinfer-cljs.parinfer-spec
  (:require [cljs.spec.alpha :as spec]))

;; Specs for the INPUT options

(spec/def ::zero-based (spec/and integer? #(>= % 0)))

(spec/def ::cursor-line ::zero-based)
(spec/def ::cursor-x ::zero-based)
(spec/def ::prev-cursor-line ::cursor-line)
(spec/def ::prev-cursor-x ::cursor-x)
(spec/def ::selection-start-line ::zero-based)

;; is changes on tuple or an array of tuples

(spec/def ::changes (spec/tuple ::zero-based ::zero-based string? string?))
(spec/def ::force-balance boolean?)
(spec/def ::partial-result boolean?)

(spec/def ::input-options (spec/keys :opt [::cursor-line ::cursor-x ::prev-cursor-line ::prev-cursor-x
                                           ::selection-start-line ::changes ::force-balance ::partial-result]))

;; Specs for the OUTPUT results

(spec/def ::name string?)
(spec/def ::message string?)
(spec/def ::line-no ::zero-based)
(spec/def ::x ::zero-based)
(spec/def ::extra (spec/tuple ::line-no ::x))

(spec/def ::parinfer-error (spec/keys :req [::name ::message ::line-no ::x]
                                      :opt [::extra]))

(spec/def ::arg-x ::zero-based)
(spec/def ::ch char?)

(spec/def ::tabstop (spec/keys :req [::x ::arg-x ::line-no ::ch]))

(spec/def ::start-x ::zero-based)
(spec/def ::end-x ::zero-based)

(spec/def ::paren-trail (spec/keys :req [::line-no ::start-x ::end-x]))

(spec/def ::success boolean?)
(spec/def ::text string?)
(spec/def ::cursor-x integer?)
(spec/def ::cursor-line integer?)
(spec/def ::error ::parinfer-error)
(spec/def ::tab-stops (spec/* ::tab-stop))
(spec/def ::paren-trails (spec/* ::paren-trail))

(spec/def ::output (spec/keys :req [::success ::text ::cursor-x ::cursor-line]
                              :opt [::error ::tab-stops ::paren-trails]))

