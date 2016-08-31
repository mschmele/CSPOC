;; Here is a simple little banking app with simple specs
;; To test all spec'd functions in a namespace, simply run (stest/check)

(ns cspoc.core
  (:require
   [clojure.spec :as s]
   [clojure.spec.gen :as gen]
   [clojure.spec.test :as stest]))

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
(s/def ::id (s/cat :id (s/and string? #(not= % clojure.string/blank?))))
(s/def ::balance number?)
(s/def ::amount nat-int?)
(s/def ::account (s/map-of ::id ::balance))
(s/def ::accounts (s/coll-of ::account))

(s/fdef get-account-from-id
        ;; Are we getting a valid collection of accounts?
        ;; What about a valid account id?
        :args (s/cat :accounts ::accounts :id ::id)
        ;; Are we returning a valid account?
        :ret (s/nilable ::account))

(s/fdef account-balance
        :args (s/cat :accounts ::accounts :id ::id)
        :ret ::balance)

(s/fdef deposit
        :args (s/cat :amount ::amount :account ::account)
        :ret ::account)

(s/fdef withdraw
        :args (s/cat :amount ::amount :account ::account)
        :ret ::account)

(s/fdef transfer
        :args (s/cat
               :accounts ::accounts
               :from-id ::id
               :to-id ::id
               :amount ::amount)
        :ret ::accounts
        :fn (s/or
             :not-found #(nil? (:ret %))
             :found #(< :amount (account-balance :accounts :from-id))))
