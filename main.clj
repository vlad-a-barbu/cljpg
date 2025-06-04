(ns main
  (:require [clojure.core.async :refer :all]))

(defn- from
  [n]
  (let [ch (chan)]
    (go-loop [i n]
      (>! ch i)
      (recur (inc i)))
    ch))

(defn- add-filter
  [ch-in]
  (let [n (<!! ch-in)
        ch-out (chan)]
    (go-loop [in (<! ch-in)]
      (if-not (zero? (mod in n))
        (>! ch-out in))
      (recur (<! ch-in)))
    ch-out))

(defn nth-prime
  [n]
  (if-not (pos? n) 
    nil
    (loop [ch (from 2)
           i (dec n)]
      (if (zero? i) (<!! ch)
          (recur
           (add-filter ch)
           (dec i))))))
