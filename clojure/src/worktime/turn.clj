(ns worktime.turn
  (:gen-class)
  (:require [worktime.datetime :as datetime]))


(defn new
  "Return an assoc with the turn information."
  [enter exit]
  (let [result {:enter enter
                :exit exit
                :elapsed (datetime/elapsed enter exit)}]
    result))


(defn print-enter
  "Return the enter time in its hour/minute format."
  [turn]
  (datetime/format-hours (:enter turn)))


(defn print-exit
  "Return the exit time in its hour/minute format."
  [turn]
  (datetime/format-hours (:exit turn)))


(defn print-elapsed
  "Return the elapsed time between the enter/exit times."
  [turn]
  (datetime/format-elapsed (:elapsed turn)))


(defn turn-print
  "Pretty print the turn data."
  [turn]
  (println (print-enter   turn)
           (print-exit    turn)
           (print-elapsed turn)))


;(defn turn-guess-next-enter
;  "Tries to guess the exit time based on the enter time from the previous
;   turn."
;  [turn]
;  (let [exit (:exit turn)]
;    (if (t/within? (t/interval (datetime-today-at 11 00)
;                               (datetime-today-at 12 30))
;                   exit)
;      (t/plus exit (t/hours 1))           ; lunch break is 1 hour
;      (t/plus exit (t/minutes 15)))))     ; turn break is 15 minutes


;(defn turn-guess-exit
;  "Tries to guess the enter time based on the enter time of the turn."
;  [turn working-time]
;  (let [exit-time (:exit turn)]
;    (if (t/before? enter-time (t/today-at 12 00))
;      (let [exit-time (t/today-at 12 00)] exit-time)
;      (let [remaining-time (- 510 working-time)]
;        (if (> remaining-time 360)
;          (let [full-turn (t/plus enter-time (t/hours 6))] full-turn)
;          (let [full-turn (t/plus enter-time (t/minutes remaining-time))]
;            full-turn))))))
