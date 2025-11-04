(ns snake.model)

;; Define the data structures for the Snake game

(defn create-game-state [game-state]
  (let [default-state {:screen-width 550 ;; Screen width (int) of the game window
                       :screen-height 400 ;; Screen height (int) of the game window
                       :screen-padding 50 ;; Screen padding (int) around the game grid
                       :cell-size 30 ;; Cell size (int) of each cell in the grid
                       :grid-width 15 ;; Grid width (int) of the game grid
                       :grid-height 10 ;; Grid height (int) of the game grid
                       :snake '([7 7] [7 8]) ;; List of Positions (tuple) representing the snake's body
                       :direction :up ;; Current direction of the snake :up, :down, :left, :right
                       :food nil ;; Position (tuple) of the food or nil
                       :food-expiration-ticks 3 ;; Ticks (int) until the food expires
                       :food-expiration-val 0 ;; Current tick (int) value for food expirations
                       :lives 3 ;; Current lives
                       :status :init ;; Current game status :init, :playing, :paused, :game-over or :win
                       :portals {[0 0] {:left [14 0] :up [0 9]}
                                 [14 0] {:right [0 0] :up [14 9]}
                                 [0 9] {:left [14 9] :down [0 0]}
                                 [14 9] {:right [0 9] :down [14 0]}} ;; Portal positions mapping
                       ;; Tick timers for various game actions
                       :ticks {:move {:time 0.30 :val 0} ;; Time interval (float) between snake moves in seconds
                               :food-expiration {:time 1.0 :val 0}}
                       }]
    (conj default-state game-state)))

(def init-game (create-game-state {}))
