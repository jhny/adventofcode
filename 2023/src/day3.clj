(ns day3
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def sample (slurp "resources/day3/sample_input.txt"))

(def input (slurp "resources/day3/input.txt"))



(defn parse-input [input]
  (into [] (map vec (str/split-lines input))))

(def digit? #{\1 \2 \3 \4 \5 \6 \7 \8 \9 \0})

(defn touching? [schematic xs]
  (some
   (fn [xs]
     (let [x (get-in schematic xs)]
       (and
        (not (nil? x))
        (not (= \. x))
        (not (digit? x)))))
   xs))

(defn get-possible-coords [idx-y idx-x]
  (let [xs [[idx-y (inc idx-x)]
            [idx-y (dec idx-x)]
            [(inc idx-y) idx-x]
            [(dec idx-y) idx-x]
            [(inc idx-y) (inc idx-x)]
            [(dec idx-y) (inc idx-x)]
            [(inc idx-y) (dec idx-x)]
            [(dec idx-y) (dec idx-x)]]]
    xs))

(defn coords-touching? [xs yys]
  (let [xs' (get-possible-coords (first xs) (second xs))]
    (some (fn [xs]
            (some (fn [ys] (= xs ys)) yys))
          xs')))

(defn get-number-coords [idx-y row]
  (map-indexed
   (fn [idx-x x]
     (if (digit? x)
       [idx-y idx-x]
       nil))
   row))

(defn get-gear-coords [idx-y row]
  (map-indexed
   (fn [idx-x x]
     (if (= x \*)
       [idx-y idx-x]
       nil))
   row))

(defn get-number-by-coords [schematic xxs]
  (parse-long (reduce
               (fn [acc xs]
                 (str acc (get-in schematic xs)))
               ""
               xxs)))

(defn get-all-possible-coords [xxs]
  (vec (set/difference
        (into #{} (mapcat
                   (fn [xs]
                     (get-possible-coords (first xs) (second xs)))
                   xxs))
        (into #{} xxs))))

(defn get-number-if-touching [schematic xxs]
  (if (touching? schematic (get-all-possible-coords xxs))
    (get-number-by-coords schematic xxs)
    nil))

; part1
(let [schematic (parse-input input)]
  (->> schematic
       (map-indexed
        (fn [index row]
          (get-number-coords index row)))
       (mapcat #(partition-by nil? %))
       (remove #(nil? (first %)))
       (map #(get-number-if-touching schematic %))
       (remove nil?)
       (apply +)))


;Part2
(let [schematic     (parse-input input)
      gear-coords   (->> schematic
                         (map-indexed
                          (fn [index row]
                            (get-gear-coords index row)))
                         (map #(remove nil? %))
                         (mapcat identity))
      number-coords (->> schematic
                         (map-indexed
                          (fn [index row]
                            (get-number-coords index row)))
                         (mapcat #(partition-by nil? %))
                         (remove #(nil? (first %))))]
  (->> gear-coords
       (map (fn [coord]
              (filter #(coords-touching? coord %) number-coords)))
       (filter #(= 2 (count %)))
       (map (fn [xxs]
              (map #(get-number-by-coords schematic %) xxs)))
       (map #(apply * %))
       (apply +)))