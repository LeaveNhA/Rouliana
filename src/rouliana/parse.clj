(ns rouliana.parse
  (:require
   [rouliana.core.classify :refer [classify-data-formation]]
   [rouliana.core.nep :as nep :refer [parse-nep']]
   [rouliana.core.ksuep :as ksuep :refer [parse-ksuep']]
   [rouliana.core.uep :as uep :refer [parse-uep']]
   [rouliana.core.iuep :as iuep :refer [parse-iuep']]
   [rouliana.helpers :as helpers :refer [parse-route-map-symbols]]
   [rouliana.core.ctx :refer [parse-ctx']]))

(defmulti parse-ep
  classify-data-formation)

(defmethod parse-ep :nep [data]
  ((parse-nep' {:parse-fn nep/default-parse-fn
                :concat-fn nep/default-concat-fn
                :parse-route-map-symbols parse-route-map-symbols
                :parse-ep parse-ep})
   data))

(defmethod parse-ep :ksuep [data]
  ((parse-ksuep' {:parse-ep parse-ep})
   data))

(defmethod parse-ep :uep [data]
  ((parse-uep' {:parse-ep parse-ep})
   data))

(defmethod parse-ep :iuep [data]
  ((parse-iuep' {:parse-route-map-symbols
                 parse-route-map-symbols})
   data))

(defmethod parse-ep :ctx [data]
  ((parse-ctx' {:parse-ep parse-ep})
   data))
