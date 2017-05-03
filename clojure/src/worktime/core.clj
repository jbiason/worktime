(ns worktime.core
  (:gen-class)
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))


(def output-formatter (f/formatter "HH:mm"))
(def elapsed-formatter (f/formatter "HH'h'mm'm'"))


(defn break-time-string
  "Break strings into their time components"
  [clock]
  (map (fn [string-time] (Integer/parseInt string-time))
       (clojure.string/split clock #":")))


(defn conv-time
  "Convert times into a full datetime"
  [times]
  (map (fn [string-pairs] (apply t/today-at (break-time-string string-pairs)))
       times))


(defn clear-invalid-times
  "Remove invalid times in the list"
  [time-list]
  (let [right-now (t/now)]
    (filter (fn [time] (t/before? time right-now)) time-list)))


(defn turn-data
  "Return an assoc with the turn information"
  [enter exit]
  (let [result {:enter enter
                :exit exit
                :elapsed (t/in-minutes (t/interval enter exit))}]
    result))


(defn work-total
  "Return the amount of working time in the turn list"
  [turn-list]
  (reduce (fn [result record] (+ result (:elapsed record)))
          0
          turn-list))


(defn print-turn-data
  "Pretty print the turn data"
  [turn]
  (let [elapsed (t/plus (t/today-at 00 00)
                        (t/minutes (:elapsed turn)))]
    (println (f/unparse output-formatter (:enter turn))
             (f/unparse output-formatter (:exit  turn))
             (f/unparse elapsed-formatter elapsed))))


(defn print-turns
  "Print all the turns"
  [turn-list]
  (doseq [record turn-list]
    (print-turn-data record)))


(defn guess-enter
  "Tries to guess the exit time based on the enter time"
  [last-turn]
  (let [exit (:exit last-turn)]
    (if (t/within? (t/interval (t/today-at 11 00)
                               (t/today-at 12 30))
                   exit)
      (t/plus exit (t/hours 1))           ; lunch break is 1 hour
      (t/plus exit (t/minutes 15)))))     ; turn break is 15 minutes


(defn guess-exit
  "Tries to guess the enter time based on the time in the previews turn"
  [enter-time working-time]
  (if (t/before? enter-time (t/today-at 12 00))
    (let [exit-time (t/today-at 12 00)] exit-time)
    (let [remaining-time (- 510 working-time)]
      (if (> remaining-time 360)
        (let [full-turn (t/plus enter-time (t/hours 6))] full-turn)
        (let [full-turn (t/plus enter-time (t/minutes remaining-time))]
          full-turn)))))


(defn single-turn
  "Create a single turn, based either on the enter/exit times,
   only the enter time or building an expected time based on
   the current turns (the last one)."
  [last-turn working-time enter exit]
  (cond
    (not (nil? exit))  (turn-data enter exit)
    (not (nil? enter)) (let [guessed-exit (guess-exit enter working-time)]
                         (turn-data enter guessed-exit))
    :else              (let [guessed-enter (guess-enter last-turn)
                             guessed-exit  (guess-exit
                                            guessed-enter
                                            working-time)]
                         (turn-data guessed-enter guessed-exit))))


(defn build-turns
  "Build the list of work turns"
  [turn-list [enter exit & rest]]
  (let [working-time (work-total turn-list)]
    (if (and (nil? enter)              ; there is not an open turn
             (>= working-time 510))    ; 510 mins = 8.5h
      turn-list
      (do (let [previous-turn (last turn-list)
                new-turn (single-turn previous-turn working-time enter exit)
                new-turn-list (conj turn-list new-turn)]
            (build-turns new-turn-list
                         rest))))))


(defn -main
  "Do the whole thing, getting values from the command line and
   building the turns with either the values or guessing the next
   ones."
  [& args]
  (->> args
       (conv-time)
       (clear-invalid-times)
       (build-turns [])
       (print-turns)
       ))
