(ns rouliana.core.classify)

(def classify-data-formation
  (comp {:nep :nep
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
        (partial take 2)))
