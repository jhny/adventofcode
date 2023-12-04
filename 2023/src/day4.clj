(ns day4
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def sample (slurp "resources/day4/sample_input.txt"))

(def input (slurp "resources/day4/input.txt"))

(defn parse-numbers [s]
  (let [xs (-> s
               str/trim
               (str/split #"\s"))]
    (vec (remove nil? (map parse-long xs)))))

(defn parse-input [input]
  (let [input (str/split-lines input)
        input (map #(str/split % #":") input)
        input (map (fn [[number game]]
                     (let [number (str/replace-first number "Card " "")
                           number (parse-long number)
                           [winning-number numbers] (str/split game #"\|")]
                       [(parse-numbers winning-number)
                        (parse-numbers numbers)]))
                   input)]

    input))

(defn calc-winning-numbers [[winning-numbers numbers]]
  (assert (= (count numbers) (count (into #{} numbers)))
          (str numbers))
  (set/intersection (into #{} winning-numbers) (into #{} numbers)))

(defn calc-score [xs]
  (let [score (count xs)]
    (cond
      (zero? score)
      0

      (= 1 score)
      1

      :else
      (reduce
       (fn [acc _]
         (+ acc acc))
       1
       (range 0 (dec score)))

      )))

(defn part1 [input]
  (->> input
       parse-input
       (map calc-winning-numbers)
       (map calc-score)
       (reduce +)))


(part1 input)
(part1 sample)


(defn calc-scratchcards
  ([acc list-of-sets] (calc-scratchcards acc list-of-sets list-of-sets))
  ([acc list-of-sets all]
   (reduce
    (fn [acc [i x]]
      (let [c   (count x)
            xs  (map #(nth all %) (range (inc i) (inc (+ i c))))
            acc (conj acc [i x])]
        (calc-scratchcards acc xs all)))
    acc
    list-of-sets)))

(defn part2 [input]
  (->> input
       parse-input
       (map-indexed (fn [i x]
                      (vec (concat (list i) x))))
       (mapv (fn [[i & xs]]
               [i (calc-winning-numbers xs)]))
       (calc-scratchcards [])
       count))

(part2 input)



