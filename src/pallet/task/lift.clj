(ns pallet.task.lift
  "Apply configuration."
  (:require
   [pallet.core :as core]
   [clojure.contrib.logging :as logging]))

(defn- build-args [args]
  (loop [args args
         prefix nil
         m nil
         phases []]
    (if-let [a (first args)]
      (cond
       (and (nil? m) (symbol? a) (nil? (namespace a))) (recur
                                                        (next args)
                                                        (name a)
                                                        m
                                                        phases)
       (not (keyword? a)) (recur
                           (next args)
                           prefix
                           (conj (or m []) a)
                           phases)
       :else (recur (next args) prefix m (conj phases a)))
      (concat (if prefix [prefix] []) m phases))))

(defn lift
  "Apply configuration.
     eg. pallet lift mynodes/my-node
   The node-types should be namespace qualified."
  [& args]
  (let [args (build-args args)]
    (doseq [arg args]
      (println arg (type arg)))
    (apply core/lift args)))
