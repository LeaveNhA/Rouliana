(ns rouliana.core.iuep
  (:require
   [rouliana.example-data :refer [real-world-data]]
   [rouliana.core.classify :refer [classify-data-formation]]
   [rouliana.helpers :refer [if-fn]]))

(defn parse-iuep' [{:keys [parse-route-map-symbols]}]
  (comp
   (partial into {})
   (partial mapv (parse-route-map-symbols :handler))
   (partial mapv (parse-route-map-symbols :action))
   (partial mapv #(mapv % [1 2]))
   (partial mapv vec)
   (partial mapv (partial apply concat))
   (partial vec)
   (partial partition 2)
   (partial mapv vec)
   (partial partition-by keyword?)))
