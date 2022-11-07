# Rouliana
New generation Xiana routes!

## API

### New route data

It composes Clojure data structures to provide best of your routes.

### Generate Reitit Routes
It takes your Rouliana routes and compile them into Reitit routes with `rouliana.parse/parse-ep`. You can use the generated 
routers in your Xiana application config to run your application.

### Generate Informational Route Map
It takes your Roulianaroutes and de-compile them into ordinary Clojure map with `rouliana.dcompile/dcompile-ep`. Now, Rouliana doesn't provide much to 
walk, query or interect with the Informational de-compilation of your routes. But it will. 🙏🏻

## Example

### Project Based

There is an existing example usage of Rouliana with [Xiana](https://github.com/Flexiana/framework) at [this repository](https://github.com/LeaveNhA/Rouliana-Example).

### Explanatory

In a fresh Xiana project, you can do the changes and run until the Example projects will be ready.

```clojure
;; Your core.clj file
(def routes
  [:index "/" {:action :index/handle-index}
   :re-frame "/re-frame" {:action :re-frame/handle-index}
   :api "/api" {:handler nil}
   [[:user
     ["/users"
      :get-users :get {:action :index/handle-index}]]]])

(defn ->system
  [app-cfg]
  (-> (config/config app-cfg)
      (rename-key :framework.app/auth :auth)
      xsw/->swagger-data
      routes/reset
      rbac/init
      session/init-backend
      #_db/connect
      #_db/migrate!
      ws/start))

(def app-cfg
  {:routes (rouliana/parse-ep routes)
   :route-pure routes
   :router-interceptors     []
   :controller-interceptors [(interceptors/muuntaja)
                             interceptors/params
                             session/guest-session-interceptor
                             interceptors/view
                             interceptors/side-effect
                             #_db/db-access
                             rbac/interceptor]})
```

```clojure
;; in your user.clj from `dev`.
(ns use
  (:require
  ,,,
  [rouliana.dcompile :refer [dcompile-ep |>]]
  ,,,)
  
  ,,,
  ;; many lines
  ,,,
  
  ;; after you start your application and let it store things in `dev-sys`:
  (-> (:route-pure @dev-sys)
      dcompile-ep
      (|> :api :user))
  ;; `|>` is a bridg'er for de-compiled routes.
```

## NOTE
It's under heavy development.
