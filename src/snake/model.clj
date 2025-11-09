(ns snake.model
  (:require [clojure.spec.alpha :as s]))

;; Define the data structures for the Snake game

;; Define spec for Position as a tuple of two integers [x y]
(s/def ::position (s/tuple int? int?))

;; Define spec for tick
(s/def ::time number?) ;; Time interval in seconds
(s/def ::val nat-int?) ;; Current tick value (non-negative integer)
(s/def ::tick (s/keys :req-un [::time ::val]))

;; Define specs for :game-state fields
(s/def ::screen-width pos-int?) ;; Screen width (int) of the game window
(s/def ::screen-height pos-int?) ;; Screen height (int) of the game window
(s/def ::screen-padding nat-int?) ;; Screen padding (int) around the game grid
(s/def ::cell-size pos-int?) ;; Cell size (int) of each cell in the grid
(s/def ::grid-width pos-int?) ;; Grid width (int) of the game grid
(s/def ::grid-height pos-int?) ;; Grid height (int) of the game grid
(s/def ::snake (s/coll-of ::position :kind list? :min-count 2)) ;; List of Positions (tuple) representing the snake's body
(s/def ::direction #{:up :down :left :right}) ;; Current direction of the snake :up, :down, :left, :right
(s/def ::food (s/nilable ::position)) ;; Position (tuple) of the food or nil if no food is present
(s/def ::food-expiration-ticks pos-int?)  ;; Ticks (int) until the food expires
(s/def ::food-expiration-val nat-int?) ;; Current tick (int) value for food expirations
(s/def ::lives nat-int?) ;; Current lives
(s/def ::status #{:init :playing :paused :game-over :win}) ;; Current game status :init, :playing, :paused, :game-over or :win
(s/def ::portals (s/map-of ::position (s/map-of ::direction ::position))) ;; Portal positions mapping
(s/def ::ticks (s/map-of keyword? ::tick)) ;; Tick timers for various game actions
(s/def ::game-state
  (s/keys :req-un [::screen-width
                   ::screen-height
                   ::screen-padding
                   ::cell-size
                   ::grid-width
                   ::grid-height
                   ::snake
                   ::direction
                   ::food
                   ::food-expiration-ticks
                   ::food-expiration-val
                   ::lives
                   ::status
                   ::portals
                   ::ticks]))


(defn create-game-state [game-state]
  (let [default-state {:screen-width 550
                       :screen-height 400
                       :screen-padding 50
                       :cell-size 30
                       :grid-width 15
                       :grid-height 10
                       :snake '([7 7] [7 8])
                       :direction :up
                       :food nil
                       :food-expiration-ticks 3
                       :food-expiration-val 0
                       :lives 3
                       :status :init
                       :portals {[0 0] {:left [14 0] :up [0 9]}
                                 [14 0] {:right [0 0] :up [14 9]}
                                 [0 9] {:left [14 9] :down [0 0]}
                                 [14 9] {:right [0 9] :down [14 0]}}
                       :ticks {:move {:time 0.30 :val 0}
                               :food-expiration {:time 1.0 :val 0}}}
        new-state (conj default-state game-state)]
    (s/assert ::game-state new-state)
    new-state))

(def init-game (create-game-state {}))
