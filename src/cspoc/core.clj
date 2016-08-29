(ns cspoc.core
  (:gen-class)
  (:require [clojure.spec :as s]))

(def select-values (comp vals select-keys))

(defn get-account-from-id [accounts id]
  (s/assert string? id)
  (s/assert vector? accounts)
  (first (filter #(= (% :id) id) accounts)))

(defn account-balance [accounts id]
  (s/assert string? id)
  (s/assert vector? accounts)
  (first (select-values (get-account-from-id accounts id) [:balance])))

(defn deposit [amount account]
  (s/assert number? amount)
  (s/assert map? account)
  (update account :balance + amount))

(defn withdraw [amount account]
  (s/assert number? amount)
  (s/assert map? account)
  (s/assert (> (account :balance) amount))
  (update account :balance - amount))

(defn transfer [accounts from-id to-id amount]
  "Transfer money from one account to another.
   Returns the updated accounts collection."
      (conj '[]
         (withdraw amount (get-account-from-id accounts from-id))
         (deposit amount (get-account-from-id accounts to-id))))

; This is just a bit of test data. Nothing to see here
(def accounts
  [{:id "jason" :balance 100}
   {:id "chris" :balance 0}
   {:id "mark" :balance 50000}])
