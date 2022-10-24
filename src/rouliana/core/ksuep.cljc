(ns rouliana.core.ksuep
  (:require
   [rouliana.core.classify :refer [classify-data-formation]]))

(defn parse-ksuep' [{:keys [parse-ep]}]
  (comp
   vec
   (partial mapv
            parse-ep)
   rest))
