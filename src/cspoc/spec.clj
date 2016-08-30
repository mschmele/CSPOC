(ns spec-demo
  (:require [clojure.spec :as s])
  (:require [cspoc.core :as cspoc]))

(s/check-asserts true)

;; Here is an account. It is a map that requires an id and balance.
;; The type field is optional according to this spec.
(s/def ::id #(string? %))
(s/def ::balance #(number? %))
(s/def ::type #(string? %))
(s/def ::account (s/keys :req [::id ::balance]
                         :opt [::type]))

(s/def ::accounts (s/coll-of ::account))

(s/fdef cposc/get-account-from-id
        :args [#(s/valid? ::accounts %)
               #(s/valid? ::id %)]
        :ret #(s/valid? ::account %))

(s/fdef cposc/account-balance
        :args [#(s/valid? ::accounts %)
               #(s/valid? ::id %)]
        :ret #(number? %))

(s/fdef cposc/deposit
        :args [#(s/assert ::amount %)
               #(s/assert ::account %)]
        :ret #(s/valid? ::account %))

(s/fdef cposc/withdraw
        :args [#(s/assert ::amount %)
               #(s/assert ::account %)]
        :ret #(s/valid? ::account %))

(s/fdef cposc/transfer
        :args [#(s/assert ::accounts %)
               #(s/assert ::from-id %)
               #(s/assert ::to-id %)
               #(s/assert ::amount %)]
        :ret #(s/valid? ::accounts %))
