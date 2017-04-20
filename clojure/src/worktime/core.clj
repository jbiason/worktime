(ns worktime.core
  (:gen-class)
  (require [clj-time.core :as t]
           [clj-time.format :as f]))


(def output-formatter (f/formatter "HH:mm"))


(defn break-time-string
  "Break strings into their time components"
  [clock]
  (map #(Integer/parseInt %) (clojure.string/split clock #":")))
  ; (fn [x] (Integer/parseInt x))


(defn conv-time
  "Convert times into a full datetime"
  [times]
  (map #(apply t/today-at (break-time-string %)) times))


(defn turn-data
  "Return an assoc with the turn information"
  [enter exit]
  (let [result {:enter enter
                :exit exit
                :elapsed (t/in-minutes (t/interval enter exit))}]
    result))


(defn print-turn-data
  "Pretty print the turn data"
  [turn]
  (println (f/unparse output-formatter (:enter turn))
           (f/unparse output-formatter (:exit  turn))
           (:elapsed turn)))


(defn guess-enter
  "Tries to guess the exit time based on the enter time"
  [last-turn]
  (let [exit (:exit last-turn)]
    (if (t/within? exit (t/today-at 11 00) (t/today-at 12 30))
      (t/plus exit (t/hours 1))           ; lunch break is 1 hour
      (t/plus exit (t/minutes 15)))))     ; turn break is 15 minutes


(defn guess-exit
  "Tries to guess the enter time based on the time in the previews turn"
  [enter-time]
  (let [full-turn (t/plus enter-time (t/hours 6))]
    full-turn))


(defn single-turn
  "Create a single turn, based either on the enter/exit times,
   only the enter time or building an expected time based on
   the current turns (the last one)."
  [last-turn enter exit]
  (cond
    (not (nil? exit))  (turn-data enter exit)
    (not (nil? enter)) (let [guessed-exit (guess-exit enter)]
                         (turn-data enter guessed-exit))
    :else              (let [guessed-enter (guess-enter last-turn)
                             guessed-exit  (guess-exit guessed-enter)]
                         (turn-data guessed-enter guessed-exit))))


(defn build-turns
  "Build the list of work turns"
  [working-time previous-turn [enter exit & rest]]
  (if (not (and (nil? enter)              ; there is not an open turn
                (>= working-time 510)))    ; 510 mins = 8.5h
    (do (let [new-turn (single-turn previous-turn enter exit)]
          (print-turn-data new-turn)
          (build-turns (+ working-time (:elapsed new-turn))
                       new-turn
                       rest)))))


(defn -main
  "Do the whole thing, getting values from the command line and
   building the turns with either the values or guessing the next
   ones."
  [& args]
  (->> args
       (conv-time)
       (build-turns 0 nil)
       ))
