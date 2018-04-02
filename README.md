__NOTE__: This doesn't work for Node yet.  Need to resolve [boot-cljsjs issue #29](https://github.com/cljsjs/boot-cljsjs/issues/29)

# Parinfer for ClojureScript

A idiomatic ClojureScript wrapper for [parinfer.js].

Lein dependency:

```clj
[parinfer-cljs "3.11.0-0"]
```

For usage, see [API for parinfer.js].  JavaScript objects are replaced with
clojure maps.  All names are converted from `camelCase` to `kebab-case`.

```clj
(ns your-ns
  (:require
    [parinfer-cljs.core :refer [indent-mode paren-mode smart-mode]]))

(println (:text (indent-mode "(foo\nbar)")))
(println (:text (paren-mode "(foo\nbar)")))
(println (:text (smart-mode "(foo\nbar)")))
```

[parinfer.js]:https://github.com/shaunlebron/parinfer/tree/master/lib
[API for parinfer.js]:https://github.com/shaunlebron/parinfer/tree/master/lib#api
