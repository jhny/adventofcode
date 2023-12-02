(ns day2
  (:require [clojure.string :as str]))

(def sample (slurp "resources/day2/sample_input.txt"))

(def input (slurp "resources/day2/input.txt"))

(defn parse-subgame [subgame]
  (let [[number color] (str/split subgame #" ")
        number (parse-long number)
        color  (keyword color)]
    [color number]))

(defn split-subgame [subgame]
  (let [subgames (str/split subgame #",")
        subgames (mapv str/trim subgames)]
    (into {} (map parse-subgame subgames))))

(defn parse-input [input]
  (mapv (fn [x]
          (let [[_ & [rest]] (str/split x #":")
                rest (str/split rest #";")]
            (mapv split-subgame rest)))
        (str/split-lines input)))

(defn fit-subgame [subgame bag]
  (reduce-kv
   (fn [_ color number]
     (if (< number (get subgame color 0))
       (reduced false)
       true))
   true
   bag))

(defn fit [game bag]
  (every? #(fit-subgame % bag) game))

;only 12 red cubes, 13 green cubes, and 14 blue cubes
(def first-bag {:red 12 :green 13 :blue 14})

(defn part1 [input]
  (let [game (parse-input input)]
    (->> game
         (map-indexed (fn [index subgame]
                        (if (fit subgame first-bag)
                          (inc index)
                          0)))
         (apply +))))

(comment

 (part1 input)

 nil)

(defn bigger [a b]
  (if (> a b)
    a
    b))

(defn fewest-number [game]
  (let [[blue red green] (reduce
                          (fn [[blue' red' green'] {:keys [blue red green]}]
                            [(bigger blue' (or blue 1))
                             (bigger red' (or red 1))
                             (bigger green' (or green 1))])
                          [1 1 1]
                          game)]
    (* blue red green)))

(defn part2 [input]
  (let [game (parse-input input)]
    (apply + (map fewest-number game))))


(comment

 (part2 input)

 nil)