(ns snake.app
  (:import
   [com.raylib Raylib])
  (:require
   [snake.update :as update]
   [snake.utils :as utils]
   [snake.view :as view]))

(defn run-loop
  "Runs one iteration of the game loop: rendering the game state."
  [game-state]
  (if (Raylib/WindowShouldClose)
    (Raylib/CloseWindow)
    (recur (->> game-state
                (view/game-input)
                (reduce update/update-game game-state)
                (view/view-game)
                (utils/update-tick-values (Raylib/GetFrameTime))))))

(defn run
  "Starts the Raylib window and game loop."
  [game-state]
  (let [screen-width (:screen-width game-state)
        screen-height (:screen-height game-state)]
    ;; Initialization
    (Raylib/InitWindow screen-width screen-height "Hello Snake from Clojure")
    (Raylib/SetTargetFPS 60)
    ;; Main game loop
    (run-loop game-state)))
