(ns rouliana.helpers)

(defn debug! [debug-message x]
  (println debug-message x)
  x)

(defn if-fn [succ-fn fail-fn check-fn value]
  (if (check-fn value)
    (succ-fn value)
    (fail-fn value)))

(defn parse-route-map-symbols [s-key]
  (partial if-fn
           #(update-in % [1 s-key] (comp resolve
                                         symbol))
           identity
           #(get-in % [1 s-key])))
