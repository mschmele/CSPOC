(ns spec-demo
  (:require [clojure.spec :as s])
  (:require [cspoc.core :as cspoc]))

(s/fdef cposc/get-account-from-id
        :args [(s/assert vector? ::accounts)
               (s/assert string? ::id)]
        :ret map?)

(s/fdef cposc/account-balance
        :args [(s/assert vector? ::accounts)
               (s/assert string? ::id)]
        :ret number?)

(s/fdef cposc/deposit
        :args [(s/assert number? ::amount)
               (s/assert map? :account)]
        :ret map?)

(s/fdef cposc/withdraw
        :args [(s/assert number? ::amount)
               (s/assert map? ::account)]
        :ret map?)

(s/fdef cposc/transfter
        :args [(s/assert vector? ::accounts)
               (s/assert string? ::from-id)
               (s/assert string? ::to-id)
               (s/assert number? ::amount)]
        :ret vector?)
