(ns worktime.core
  (:gen-class)
  (:require [worktime.datetime :as datetime]))


(defn clear-invalid-times
  "Remove invalid times in the list."
  [time-list]
  (filter (fn [time] (datetime/before? time (datetime/now))) time-list))


;; Need to move those to turn-list.clj

; (defn single-turn
;   "Create a single turn, based either on the enter/exit times,
;   only the enter time or building an expected time based on
;   the current turns (the last one)."
;  [last-turn working-time enter exit]
;  (cond
;    (not (nil? exit))  (last-turn enter exit)
;    (not (nil? enter)) (let [guessed-exit (guess-exit enter working-time)]
;                         (turn-data enter guessed-exit))
;    :else              (let [guessed-enter (guess-enter last-turn)
;                             guessed-exit  (guess-exit
;                                            guessed-enter
;                                            working-time)]
;                         (turn-data guessed-enter guessed-exit))))


; (defn build-turns
;  "Build the list of work turns"
;  [turn-list [enter exit & rest]]
;  (let [working-time (work-total turn-list)]
;    (if (and (nil? enter)              ; there is not an open turn
;             (>= working-time 510))    ; 510 mins = 8.5h
;      turn-list
;      (do (let [previous-turn (last turn-list)
;                new-turn (single-turn previous-turn working-time enter exit)
;                new-turn-list (conj turn-list new-turn)]
;            (build-turns new-turn-list
;                         rest))))))


;; turn list functions

(defn turn-list-work-total
  "Return the amount of working time in the turn list."
  [turn-list]
  (reduce (fn [result record] (+ result (:elapsed record)))
          0
          turn-list))


(defn turn-list-print
  "Print all the turns."
  [turn-list]
  (doseq [record turn-list]
    (println record)))
    ; (print-turn-data record)))


(defn -main
  "Do the whole thing, getting values from the command line and
   building the turns with either the values or guessing the next
   ones."
  [& args]
  (let [working-list (->> args
                          ; (convert-time-list-into-datetime-list)
                          ; (clear-invalid-times)
                          ; (build-turns [])
                          )]
    (turn-list-print working-list)
    ))
