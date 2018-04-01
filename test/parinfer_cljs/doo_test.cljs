(ns parinfer-cljs.doo-test
  (:require [doo.runner :refer-macros [doo-tests]]
            [parinfer-cljs.core-test]))

(doo-tests 'parinfer-cljs.core-test)