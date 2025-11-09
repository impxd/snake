(ns snake.view
  (:import [com.raylib Raylib Colors])
  (:require [snake.utils :as utils]
            [snake.update :as update]))

;; Define functions to render the game state using Raylib

(defn game-input
  "Handles user input for the current game state."
  [game-state]
  (case (:status game-state)
    :playing (let [direction (update/snake-direction (:snake game-state))]
               (cond-> []
                 (Raylib/IsKeyPressed Raylib/KEY_P) (conj :pause)
                 (and (not= direction :left) (Raylib/IsKeyPressed Raylib/KEY_RIGHT)) (conj [:change-direction :right])
                 (and (not= direction :right) (Raylib/IsKeyPressed Raylib/KEY_LEFT)) (conj [:change-direction :left])
                 (and (not= direction :down) (Raylib/IsKeyPressed Raylib/KEY_UP)) (conj [:change-direction :up])
                 (and (not= direction :up) (Raylib/IsKeyPressed Raylib/KEY_DOWN)) (conj [:change-direction :down])
                 (Raylib/IsKeyPressed Raylib/KEY_F) (conj :new-food-position)
                 (or (Raylib/IsKeyPressed Raylib/KEY_E) (zero? (utils/get-tick-value game-state :food-expiration))) (conj :food-expiration-tick)
                 (zero? (utils/get-tick-value game-state :move)) (conj :move)))
    :paused (cond
              (Raylib/IsKeyPressed Raylib/KEY_P) [:pause]
              :else [:idle])
    :game-over (cond
                 (Raylib/IsKeyPressed Raylib/KEY_R) [:restart]
                 :else [:idle])
    :win (cond
           (Raylib/IsKeyPressed Raylib/KEY_R) [:restart]
           :else [:idle])
    :init [:idle]))

(defn view-grid
  "Renders the game grid."
  [game-state]
  (let [screen-padding (:screen-padding game-state)
        cell-size (:cell-size game-state)
        grid-width (:grid-width game-state)
        grid-height (:grid-height game-state)]
    ;; Render x axis grid lines
    (dotimes [i (inc grid-height)]
      (let [x1 screen-padding
            y1 (+ screen-padding (* i cell-size))
            x2 (+ screen-padding (* grid-width cell-size))
            y2 (+ screen-padding (* i cell-size))]
        (Raylib/DrawLine x1 y1 x2 y2 Colors/LIGHTGRAY)))
    ;; Render y axis grid lines
    (dotimes [i (inc grid-width)]
      (let [x1 (+ screen-padding (* i cell-size))
            y1 screen-padding
            x2 (+ screen-padding (* i cell-size))
            y2 (+ screen-padding (* grid-height cell-size))]
        (Raylib/DrawLine x1 y1 x2 y2 Colors/LIGHTGRAY)))))

(defn view-snake
  "Renders the snake on the grid."
  [game-state]
  (let [snake (:snake game-state)
        screen-padding (:screen-padding game-state)
        cell-size (:cell-size game-state)]
    (doseq [[x y] snake]
      (let [rect-x (+ screen-padding (* x cell-size))
            rect-y (+ screen-padding (* y cell-size))]
        (Raylib/DrawRectangleV
         (utils/vector2 rect-x rect-y)
         (utils/vector2 cell-size cell-size)
         Colors/GRAY)))))

(defn view-food
  "Renders the food on the grid."
  [game-state]
  (let [food (:food game-state)
        screen-padding (:screen-padding game-state)
        cell-size (:cell-size game-state)]
    (when food
      (let [rect-x (+ screen-padding (* (first food) cell-size))
            rect-y (+ screen-padding (* (second food) cell-size))]
        (Raylib/DrawRectangle rect-x rect-y cell-size cell-size Colors/BEIGE)
        (utils/draw-text-centered
         (str (- (:food-expiration-ticks game-state) (:food-expiration-val game-state)))
         (+ rect-x (quot cell-size 2))
         (+ rect-y (quot cell-size 2))
         20
         Colors/RAYWHITE)))))

(defn view-lives
  "Renders the current lives."
  [game-state]
  (let [lives (:lives game-state)
        screen-padding (:screen-padding game-state)]
    (Raylib/DrawText (str "Lives: " lives) (+ screen-padding) 20 20 Colors/DARKGRAY)))

(defn view-portals
  "Renders the portals on the grid."
  [game-state]
  (let [screen-padding (:screen-padding game-state)
        cell-size (:cell-size game-state)
        portal-positions (keys (:portals game-state))]
    (doseq [[x y] portal-positions]
      (let [rect-x (+ screen-padding (* x cell-size))
            rect-y (+ screen-padding (* y cell-size))]
        (Raylib/DrawRectangle rect-x rect-y cell-size cell-size Colors/LIGHTGRAY)))))


(defn view-status
  "Renders the current game status."
  [game-state]
  (case (:status game-state)
    :init nil
    :playing nil
    :paused (let [screen-width (:screen-width game-state)
                  screen-height (:screen-height game-state)]
              (utils/draw-text-centered
               "Pause"
               (quot screen-width 2)
               (quot screen-height 2)
               20
               Colors/DARKGRAY))
    :game-over (let [screen-width (:screen-width game-state)
                     screen-height (:screen-height game-state)]
                 (utils/draw-text-centered
                  "Press (R) to try again!"
                  (quot screen-width 2)
                  (quot screen-height 2)
                  20
                  Colors/DARKGRAY))
    :win (let [screen-width (:screen-width game-state)
               screen-height (:screen-height game-state)]
           (utils/draw-text-centered
            "You Won! Press (R) to play again!"
            (quot screen-width 2)
            (quot screen-height 2)
            20
            Colors/DARKGRAY))))

(defn view-game
  "Renders the current game state."
  [game-state]
  (Raylib/BeginDrawing)
  (Raylib/ClearBackground Colors/RAYWHITE)
  (view-lives game-state)
  (view-portals game-state)
  (view-snake game-state)
  (view-food game-state)
  (view-grid game-state)
  (view-status game-state)
  (Raylib/EndDrawing)
  game-state)
