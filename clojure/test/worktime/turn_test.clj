(ns worktime.turn-test
  (:require [clojure.test :refer :all]
            [worktime.turn :refer :all]
            [worktime.datetime :as datetime]))

(deftest something-test
  (let [turn (new-turn (datetime/today-at 8 15) (datetime/today-at 12 0))]
    (testing "Enter"
      (= (print-enter turn) "08:15"))
    (testing "Exit"
      (= (print-exit turn) "12:00"))
    (testing "Elapsed"
      (= (print-elapsed turn) "3h45m"))))
