# Parinfer for ClojureScript

A idiomatic ClojureScript wrapper for [parinfer.js].

Lein dependency:

```clj
[parinfer-cljs "1.4.0-1"]
```

For usage, see [API for parinfer.js].  JavaScript objects are replaced with
clojure maps.  All names are converted from `camelCase` to `kebab-case`.

```clj
(ns your-ns
  (:require
    [parinfer.core :refer [indent-mode paren-mode]]))

(println (:text (indent-mode "(foo\nbar)")))
(println (:text (paren-mode "(foo\nbar)")))
```

[parinfer.js]:https://github.com/shaunlebron/parinfer/tree/master/lib
[API for parinfer.js]:https://github.com/shaunlebron/parinfer/tree/master/lib#api
