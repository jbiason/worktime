(ns worktime.datetime
  (:gen-class)
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))


;; output format definitions
(def output-formatter (f/formatter "HH:mm"))
(def elapsed-formatter (f/formatter "(HH'h'mm'm'"))


(defn break-time-string
  "Break strings into their time components."
  [clock]
  (map (fn [string-time] (Integer/parseInt string-time))
       (clojure.string/split clock #":")))


(defn convert-time-list-into-datetime-list
  "Convert times into a full datetime."
  [times]
  (map (fn [string-pairs] (apply datetime-today-at
                                 (break-time-string string-pairs)))
       times))

(defn today-at
  "Return a datetime object with today's date and the specified hour and
   minute, in the current timezone."
  [hour minute]
  (t/today-at hour minute))


(defn now
  "Return a datetime object with the time right now, in the current timezone."
  []
  (t/local-now))


(defn before?
  "Return true if the first datetime element occurs before the second."
  [first second]
  (< (t/in-minutes first) (t/in-minutes second)))
  ; this is basically "before?", but I want to avoid predicates at the moment


(defn elapsed
  "Return the minutes elapsed between the first datetime and the second."
  [first second]
  (t/in-minutes (t/interval enter exit)))


(defn format-hours
  "Return a string representing only the hour/minute of a datetime."
  [datetime]
  (f/unparse output-formatter datetime))
