(ns snake.core
  (:require [snake.app :refer [run]]
            [snake.model :refer [init-game]]))

(defn -main
  "Starts the application."
  []
  ;; Call the run function from snake.app namespace
  (run init-game))
