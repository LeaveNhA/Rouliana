(ns rouliana.core.nep
  (:require
   [rouliana.example-data :refer [real-world-data]]
   [rouliana.core.classify :refer [classify-data-formation]]
   [rouliana.helpers :refer [if-fn
                                      parse-route-map-symbols]]))

(def default-concat-fn
  (comp
   (partial mapv #(if (vector? (last %))
                    (vec (concat (drop-last %)
                                 ((comp vec
                                        (partial map vec)
                                        (partial map (partial apply concat))
                                        (partial partition 2)
                                        (partial partition-by string?)
                                        flatten
                                        last)
                                  %)))
                    %))))

(defn parse-nep' [{:keys [parse-route-map-symbols
                          parse-fn
                          concat-fn
                          parse-ep]}]
  (let [node-fn (comp
                 (partial if-fn
                          first
                          (comp
                           vec
                           (partial apply conj))
                          (comp (partial = 1) count))
                 vec
                 (partial mapv (parse-route-map-symbols :handler))
                 (partial mapv (parse-route-map-symbols :action))
                 (partial filter identity)
                 (juxt (comp
                        (partial vec)
                        (partial take 2)
                        rest)
                       (comp
                        (partial if-fn
                                 parse-ep
                                 (constantly nil)
                                 vector?)
                        last)))]
    (comp
     vec
     concat-fn
     (partial mapv node-fn)
     parse-fn)))

(def default-parse-fn
  (comp
   (partial mapv vec)
   (partial mapv #(concat [(first %)] (second %)))
   (partial mapv (partial apply conj))
   (partial partition 2)
   (partial mapv vec)
   (partial partition-by keyword?)))

#_(def parse-nep
  (parse-nep' {:parse-fn default-parse-fn
               :parse-route-map-symbols (constantly identity) #_parse-route-map-symbols
               :parse-ep classify-data-formation}))

#_(defmethod parse-ep :nep [data]
  ((comp
    vec
    (fn [[url route-map route-data]]
      (if route-data
        (concat [url route-map]
                ((comp (partial mapv vec) (partial partition 2))
                 route-data))
        [url route-map]))
    parse-nep)
   data))
