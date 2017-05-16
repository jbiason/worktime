(ns worktime.datetime
  (:gen-class)
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]))


;; output format definitions
(def output-formatter (f/formatter "HH:mm"))
(def elapsed-formatter (f/formatter "(HH'h'mm'm'"))


(defn break-time-string
  "Break strings into their time components."
  [time-as-string]
  (map (fn [string-time] (Integer/parseInt string-time))
       (clojure.string/split time-as-string #":")))


(defn today-at
  "Return a datetime object with today's date and the specified hour and
   minute, in the current timezone."
  [hour minute]
  (t/today-at hour minute))


(defn time-to-datetime
  "Convert times into a full datetime."
  [string-pairs]
  (apply today-at (break-time-string string-pairs)))


(defn now
  "Return a datetime object with the time right now, in the current timezone."
  []
  (l/local-now))


(defn before?
  "Return true if the first datetime element occurs before the second."
  [first second]
  (let [midnight (t/today-at 0 0)
        first-minutes (t/in-minutes (t/interval midnight first))
        second-minutes (t/in-minutes (t/interval midnight second))]
    (< first-minutes second-minutes)))


(defn elapsed
  "Return the minutes elapsed between the first datetime and the second."
  [first second]
  (t/in-minutes (t/interval first second)))


(defn format-hours
  "Return a string representing only the hour/minute of a datetime."
  [datetime]
  (f/unparse output-formatter datetime))


(defn add
  "Add minutes to a datetime object."
  [datetime minutes]
  (t/plus datetime (t/minutes minutes)))


(defn format-elapsed
  "Return a string representating only the elapsed minutes in a human-readable
   format."
  [minutes]
  (let [elapsed-date (add (today-at 0 0) minutes)]
    (f/unparse elapsed-formatter elapsed-date)))
