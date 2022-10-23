(ns rouliana.dcompile
  (:require
   [rouliana.core.classify :refer [classify-data-formation]]
   [rouliana.core.nep :as nep :refer [parse-nep']]
   [rouliana.core.ksuep :as ksuep :refer [parse-ksuep']]
   [rouliana.core.uep :as uep :refer [parse-uep']]
   [rouliana.core.iuep :as iuep :refer [parse-iuep']]
   [rouliana.helpers :as helpers :refer [parse-route-map-symbols comp']]
   [rouliana.example-data :refer [real-world-data]]
   [rouliana.core.ctx :refer [parse-ctx']]))

(ns-unmap *ns* 'dcompile-ep)

(defmulti dcompile-ep
  classify-data-formation)

(defmethod dcompile-ep :nep [data]
  ((comp
    (partial reduce
             (fn [m [[k] & [[url route-map & [ ctx ]]]]]
               (println ":NEP" m k url route-map ctx)
               (merge m {k (merge (if ctx
                                    (dcompile-ep ctx)
                                    {})
                                  {:url url
                                   :route-map route-map})}))
             {})
    (partial partition 2)
    (partial mapv vec)
    (partial partition-by keyword?))
   data))

(defmethod dcompile-ep :ksuep [data]
  ((comp
    (fn [[k v]]
      {k {:> (reduce merge {} v)}})
    (juxt first (comp (partial mapv dcompile-ep) rest)))
   data))

(defmethod dcompile-ep :uep [data]
  ((comp
    (fn [[url idata]]
      (if (map? idata)
        (into {} (map (fn [[k v]] {k (assoc v :url url)}) idata))
        {:type :uep :> idata}))
    (juxt first (comp
                 dcompile-ep
                 rest)))
   data))

(defmethod dcompile-ep :iuep [data]
  ((comp
    (fn [args] (apply merge args))
    (partial mapv (partial merge))
    (partial flatten)
    (partial mapv (fn [[k method route-map]]
                    {k {:method method
                        :route-map route-map}}))
    (partial mapv (partial apply concat))
    (partial partition 2)
    (partial mapv vec)
    (partial partition-by keyword?))
   data))

(defmethod dcompile-ep :ctx [data]
  ((comp
    (partial assoc {} :>)
    (partial into {})
    (partial mapv dcompile-ep))
   data))

(defmethod dcompile-ep :unk [data]
  {:!!! {:type :unknown :data data}})

(comment
  (-> real-world-data dcompile-ep :api :>)

  (-> real-world-data dcompile-ep :api :> :candidate :> :comment :> :delete)

  (-> real-world-data dcompile-ep clojure.pprint/pprint)

  ((partial mapv (partial mapv (fn [[k v]] [k v]))) (-> real-world-data dcompile-ep :api :> :user (get 0) :>)))
