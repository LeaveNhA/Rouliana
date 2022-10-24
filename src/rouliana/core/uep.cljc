(ns rouliana.core.uep
  (:require
   [rouliana.example-data :refer [real-world-data]]
   [rouliana.core.classify :refer [classify-data-formation]]
   [rouliana.helpers :refer [if-fn]]))

(defn parse-uep' [{:keys [parse-ep]}]
  (comp
   vec
   (juxt first (comp
                parse-ep
                rest))))
