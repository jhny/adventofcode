(ns day5
  (:require [clojure.string :as str]))

(def sample (slurp "resources/day5/sample_input.txt"))

(def input (slurp "resources/day5/input.txt"))


(do

  (defn build-map [k v r]
    (let [rng-k (range k (+ k r))
          rng-v (range v (+ v r))]
      (zipmap rng-v rng-k)))

  (defn parse-map [s]
    (let [[m & xs] (str/split s #"\n")
          [m] (str/split m #" ")
          [from to] (str/split m #"-to-")
          from (keyword from)
          to   (keyword to)
          xs   (mapv (fn [x] (mapv parse-long (str/split x #" "))) xs)
          map  (reduce
                (fn [acc [k v r]]
                  (conj acc
                        {:from  v
                         :to    k
                         :delta r})
                  )
                []
                xs)
          ]
      {from {:xs (sort-by :from map)
             :to to}}))

  (defn match [xs number]
    (let [result (reduce
                  (fn [_ {:keys [from to delta]}]
                    (cond

                      (and (>= number from) (<= number (+ from (dec delta))))
                      (reduced (+ to (- number from)))

                      :else nil))
                  nil
                  xs)]
      (or result number)))

  (def memoized-match (memoize match))

  (defn in-range [[m1 m2] x]
    (and (>= x m1)
         (<= x m2)))
  (defn walk-seed [m x]
    (loop [x x
           m m
           k :seed]

      (let [xs (get-in m [k :xs])
            to (get-in m [k :to])]
        (if (= to :location)
          (match xs x)
          (recur (match xs x) m to)))))

  ;(let [[seeds & xs] (str/split input #"\n\n")
  ;      m  (apply merge (map parse-map xs))
  ;      xs (map parse-long (rest (str/split seeds #" ")))
  ;      ;xs (take 1 (drop 1 xs))
  ;      ;xs (take 2 xs)
  ;      ]
  ;  m
  ;  (apply min (map #(walk-seed m %) xs)) )
  (defn define-ranges [m]
    (reduce-kv
     (fn [{:keys [ranges] :as acc} k {:keys [xs]}]
       (reduce
        (fn [{:keys [ranges] :as acc} {:keys [from to delta]}]
          (let [[m1 m2] ranges
                m1 (or m1 from)
                m2 (or m2 (+ to delta))]
            (assoc acc :ranges [(if (< from m1)
                                  from
                                  m1)
                                (if (> to m2)
                                  to
                                  m2)]
                       )))
        acc
        xs))
     m
     m))

  (let [[seeds & xs] (str/split input #"\n\n")
        m      (apply merge (map parse-map xs))
        xs     (map parse-long (rest (str/split seeds #" ")))
        xs     (partition 2 xs)
        result (reduce
                (fn [acc [f l]]
                  (reduce
                   (fn [acc x]
                     (let [result (walk-seed m x)]
                       (if (< result acc)
                         (do
                           (clojure.pprint/pprint result)
                           result)
                         acc)))
                   acc
                   (range f (+ f l))))
                999999999999999
                xs)
        ]
    (clojure.pprint/pprint result)
    (spit "result.edn" result)
    result

    )

  )




