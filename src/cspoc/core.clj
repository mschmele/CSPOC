(ns cspoc.core
  (:gen-class))
(require '[clojure.spec :as s])

(def select-values (comp vals select-keys))


(defn get-account-from-id
  [accounts id]
  (first (filter #(= (% :id) id) accounts)))


(defn account-balance
  [accounts id]
  (first (select-values (get-account-from-id accounts id) [:balance])))

(defn deposit
  [amount account]
  (update account :balance + amount))


(defn withdraw
 "Basic withdraw function. Does NOT handle validation."
 [amount account]
 (update account :balance - amount))


(defn transfer
  "Transfer money from one account to another.
   Returns the updated accounts collection."
  [accounts from-id to-id amount]
  (if (< (account-balance accounts from-id) amount)
      (println "This will cause an overdraft!")
      (do
       (conj '[]
         (withdraw amount (get-account-from-id accounts from-id))
         (deposit amount (get-account-from-id accounts to-id))))))

; This is just a bit of test data. Nothing to see here
(def accounts
  [{:id "jason" :balance 100}
   {:id "chris" :balance 0}
   {:id "mark" :balance 50000}])
