(ns rouliana.core.classify)

(def classify-data-formation
  #?(:clj (comp {:nep :nep
                 :ksuep :ksuep
                 :uep :uep
                 :ctx :ctx
                 :iuep :iuep
                 nil :unk}
                {[clojure.lang.PersistentVector] :ctx
                 [clojure.lang.Keyword java.lang.String] :nep
                 [java.lang.String clojure.lang.Keyword] :uep
                 [clojure.lang.PersistentVector clojure.lang.PersistentVector] :ctx
                 [clojure.lang.Keyword clojure.lang.Keyword] :iuep
                 [clojure.lang.Keyword clojure.lang.PersistentVector] :ksuep}
                (partial mapv type)
                (partial take 2))
     :cljs (comp {:nep :nep
                  :ksuep :ksuep
                  :uep :uep
                  :ctx :ctx
                  :iuep :iuep
                  nil :unk}
                 {[[true false false]] :ctx
                  [[false false true] [false true false]] :nep
                  [[false true false] [false false true]] :uep
                  [[true false false] [true false false]] :ctx
                  [[false false true] [false false true]] :iuep
                  [[false false true] [true false false]] :ksuep}
                 (partial mapv #(mapv (fn [f] (f %)) [vector? string? keyword?]))
                 (partial take 2))))
