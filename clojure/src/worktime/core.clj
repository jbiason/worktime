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


(defn build-turn
  "Build a turn of work"
  [turn-list worked-time time-list]
  (let [turn-times (take 2 time-list)]
    (build-turn turn-list worked-time (drop 2 time-list)))


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
