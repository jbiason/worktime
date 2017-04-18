(ns worktime.core
  (:gen-class)
  (require [clj-time.core :as t])
  (require [clj-time.format :as f]))


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
  ({:enter enter
    :exit exit
    :elapsed (t/in-minutes (t/interval enter exit))}))


(defn guess-enter
  "Tries to guess the exit time based on the enter time"
  [last-turn]
  ())


(defn guess-exit
  "Tries to guess the enter time based on the time in the previews turn"
  [enter-time]
  ())


(defn working-time
  "Return the amount of time working in the turn list."
  [turn-list]
  (reduce (fn [result record] (+ result (:elapsed record))) 0 turn-list))


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
  [working-time turn-list [enter exit & rest]]
  (if (and (nil? enter)              ; there is not an open turn
           (>= working-time 510))    ; 510 mins = 8.5h
    turn-list
    (do (let [new-turn (single-turn (last turn-list) enter exit)]
          (into turn-list new-turn)
          (build-turns (+ working-time (:elapsed new-turn))
                       turn-list
                       rest)))))


(defn -main
  "Do the whole thing, getting values from the command line and
   building the turns with either the values or guessing the next
   ones."
  [& args]
  (->> args
       (conv-time)
       (map #(f/unparse output-formatter %))
       (clojure.string/join " ")
       (println)))
