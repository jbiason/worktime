(ns worktime.datetime-test
  (:require [clojure.test :refer :all]
            [worktime.datetime :refer :all]))


(deftest break-time-test
  (testing "Tests if breaking time actually break the components down
            correctly."
    (let [[broke-hour broke-minute] (break-time-string "08:15")]
      (and (is (= broke-hour 8))
           (is (= broke-minute 15))))))


(deftest today-at-test
  (testing "Tests if we get a date in the current timezone correctly."
    (let [date (today-at 8 15)]
      (= (format-hours date) "08:15"))))


(deftest before-test
  (testing "Tests if before detects dates before each other."
    (let [earlier (today-at 9 15)
          later (today-at 10 15)]
      (do (before? earlier later)
          (not (before? later earlier))))))


(deftest elapsed-test
  (testing "Tests if elapsed calculates the elapsed time between two events
            correctly."
    (= (elapsed (today-at 9 15) (today-at 10 15)) 60)))


(deftest elapsed-print-test
  (testing "Tests if the format-elapsed renders content correctly."
    (do (= (format-elapsed 50) "00h50m")
        (= (format-elapsed 120) "02h00m"))))


(deftest add-test
  (testing "Checks if adding minutes to a date actually adds minutes."
    (= (format-hours (add (today-at 00 15) 60)) "01:15")))
