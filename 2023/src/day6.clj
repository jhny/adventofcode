(ns day6
  (:require [clojure.string :as str]))

(def sample (slurp "resources/day6/sample_input.txt"))

(def input (slurp "resources/day6/input.txt"))

(defn parse-input [input]
  (let [[times distances] (str/split-lines input)
        [_ & times] (str/split times #"\s+")
        times     (mapv parse-long times)
        [_ & distances] (str/split distances #"\s+")
        distances (mapv parse-long distances)]
    (mapv (fn [x y]
            {:time     x
             :distance y})
          times distances)))


(defn beat [time race-time distance]
  (let [left         (- race-time time)
        new-distance (* left time)]
    (>= new-distance distance)))

; Part 1
(reduce
 (fn [acc {:keys [time distance]}]
   (let [xs (range 0 (inc time))
         ys (map #(beat % time distance) xs)
         m  (zipmap xs ys)
         zs (reduce-kv
             (fn [acc time beat?]
               (if beat?
                 (conj acc time)
                 acc))
             []
             m)]
     (* acc (count zs))))
 1
 (parse-input input))



;Part 2
(defn parse-input2 [input]
  (let [[times distances] (str/split-lines input)
        [_ & times] (str/split times #"\s+")
        time     (parse-long (str/join times))
        [_ & distances] (str/split distances #"\s+")
        distance (parse-long (str/join distances))]
    [time distance]))

(let [[time distance] (parse-input2 input)
      xs (range 0 (inc time))
      ys (map #(beat % time distance) xs)
      m  (zipmap xs ys)
      zs (reduce-kv
          (fn [acc time beat?]
            (if beat?
              (conj acc time)
              acc))
          []
          m)]
  (count zs))


; Time:        59     70     78     78
; Distance:   430   1218   1213   1276

