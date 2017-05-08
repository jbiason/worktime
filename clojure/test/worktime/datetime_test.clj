(ns worktime.datetime-test
  (:require [clojure.test :refer :all]
            [worktime.datetime :refer :all]))

(deftest break-time-test
  (testing "Tests if breaking time actually break the components down
            correctly."
    (let [[broke-hour broke-minute] (break-time-string "08:15")]
      (and (is (= broke-hour 8))
           (is (= broke-minute 15))))))
