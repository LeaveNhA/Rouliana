(ns rouliana.core.ctx)

(defn parse-ctx' [{:keys [parse-ep]}]
  (comp
   vec
   (partial mapv #(if (vector? (first %))
                    (reduce concat %)
                    %))
   (partial mapv parse-ep)))
