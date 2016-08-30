(ns cspoc.core
  (:gen-class)
  (:require [clojure.spec :as s])
  (require [clojure.spec.gen :as gen])
  (require [clojure.spec.test :as stest]))

(def select-values (comp vals select-keys))

(defn get-account-from-id [accounts id]
  (first (filter #(= (% :id) id) accounts)))

(defn account-balance [accounts id]
  (first (select-values (get-account-from-id accounts id) [:balance])))

(defn deposit [amount account]
  (update account :balance + amount))

(defn withdraw [amount account]
  (update account :balance - amount))

(defn transfer [accounts from-id to-id amount]
  "Transfer money from one account to another.
   Returns the updated accounts collection."
  (conj '[]
    (withdraw amount (get-account-from-id accounts from-id))
    (deposit amount (get-account-from-id accounts to-id))
    (filter
     #(and
       (not= from-id (% :id)) (not= to-id (% :id)))
     accounts)))

; This is just a bit of test data. Nothing to see here
(def accounts
  [{:id "jason" :balance 100}
   {:id "chris" :balance 0}
   {:id "mark" :balance 50000}])

(s/check-asserts true)

;; Here is an account. It is a map that requires an id and balance.
;; The type field is optional according to this spec.
(s/def ::id #(string? %))
(s/def ::balance #(number? %))
(s/def ::type #(string? %))
(s/def ::account (s/keys :req [::id ::balance]
                         :opt [::type]))

(s/def ::accounts (s/coll-of ::account))

(s/fdef get-account-from-id
        ;; Are we getting a valid collection of accounts?
        ;; What about a valid account id?
        :args [#(s/valid? ::accounts %)
               #(s/valid? ::id %)]
        ;; Are we returning a valid account?
        :ret #(s/valid? ::account %))

(s/fdef account-balance
        :args [#(s/valid? ::accounts %)
               #(s/valid? ::id %)]
        :ret #(number? %))

(s/fdef deposit
        :args [#(s/assert ::amount %)
               #(s/assert ::account %)]
        :ret #(s/valid? ::account %))

(s/fdef withdraw
        :args [#(s/assert ::amount %)
               #(s/assert ::account %)]
        :ret #(s/valid? ::account %))

(s/fdef transfer
        :args [#(s/assert ::accounts %)
               #(s/assert ::from-id %)
               #(s/assert ::to-id %)
               #(s/assert ::amount %)]
        :ret #(s/valid? ::accounts %)
        :fn #(< ::amount (account-balance ::accounts ::from-id)))
