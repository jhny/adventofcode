(ns day1
  (:require [clojure.string :as str]))

(def sample (slurp "resources/day1/sample_input.txt"))
(def sample2 "two1nine\neightwothree\nabcone2threexyz\nxtwone3four\n4nineeightseven2\nzoneight234\n7pqrstsixteen")

(def input (slurp "resources/day1/input.txt"))

(def integers #{\1 \2 \3 \4 \5 \6 \7 \8 \9})

(def int-strings #{"one" "two" "three" "four" "five" "six" "seven" "eight" "nine"})

(def convert {"one"   1
              "two"   2
              "three" 3
              "four"  4
              "five"  5
              "six"   6
              "seven" 7
              "eight" 8
              "nine"  9})

(def regex #"one|two|three|four|five|six|seven|eight|nine")
(def regex' #"^one|^two|^three|^four|^five|^six|^seven|^eight|^nine|^1|^2|^3|^4|^5|^6|^7|^8|^9")
(def get-first-last (juxt first last))

(defn parse-input [input]
  (map (fn [xs]
         (parse-long (str/join (get-first-last (filter integers xs)))))
       (str/split-lines input)))


(defn part1 [input]
  (apply + (parse-input input)))

(comment

 (part1 input)

 nil)

(defn get-possible-integers [s]
  (let [result (reduce
                (fn [[s acc] x]
                  (let [s     (str x s)
                        match (first (re-seq regex' s))]
                    (if match
                      [s (conj acc (get convert match match))]
                      [s acc])))
                ["" (list)]
                (reverse s))]
    (second result)))


(defn parse-input2 [input]
  (map (fn [xs]
         (parse-long (str/join (get-first-last
                                (get-possible-integers xs)))))
       (str/split-lines input)))

(defn part2 [input]
  (apply + (parse-input2 input)))

(comment

 (part2 input)

 nil)
