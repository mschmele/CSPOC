(ns cspoc.core-test
  (:require [clojure.test :refer :all]
            [cspoc.core :refer :all]))

(deftest a-test
  (testing "SHOW ME THE MONEY"
    (is (= (account-balance accounts "jason") 100))))
