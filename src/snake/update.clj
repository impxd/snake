(ns snake.update
  (:require [clojure.core.match :refer [match]]
            [snake.model :refer [init-game]]))

;; Define functions to update the game state

(defn available-food-positions
  "Returns a set of available Positions for the food"
  [game-state]
  (let [grid-width (:grid-width game-state)
        grid-height (:grid-height game-state)
        snake (:snake game-state)
        food (:food game-state)
        all-positions (set (for [x (range grid-width)
                                 y (range grid-height)]
                             [x y]))
        used-positions (clojure.set/union (set snake) (set (list food)))]
    (clojure.set/difference all-positions used-positions)))

(defn new-food-position
  "Return a random Position available for the food
   if there is not available Position it returns nil
   that means the game has finished."
  [game-state]
  (let [available-positions (available-food-positions game-state)]
    (if (empty? available-positions)
      nil
      (rand-nth (apply list available-positions)))))

(defn portal?
  "Returns true if the given position is a portal"
  [portals position direction]
  (and
   (contains? (set (keys portals)) position)
   (some? (get-in portals [position direction]))))

(defn new-position
  "Returns a new position based on a given direction."
  [position direction]
  (let [[px py] position]
    (case direction
      :left  [(dec px) py]
      :right [(inc px) py]
      :up    [px (dec py)]
      :down  [px (inc py)])))

(defn move-to-position
  "Returns the new Position after moving from a pivot Position
   in the given direction taking in account existing portals."
  [portals position direction]
  (if (portal? portals position direction)
    (get-in portals [position direction])
    (new-position position direction)))

(defn new-head-position
  "Returns the new head Position"
  [portals snake direction]
  (move-to-position portals (first snake) direction))

(defn next-move-direction
  "It returns the next snake's head direction:
   :available, :through-portal, :wall-collision, :self-collision or :food"
  [game-state]
  (let [grid-width (:grid-width game-state)
        grid-height (:grid-height game-state)
        snake (:snake game-state)
        direction (:direction game-state)
        food (:food game-state)
        head (first snake)
        new-head (new-head-position (:portals game-state) snake direction)
        [head-x head-y] new-head]
    (cond
      (= new-head food) :food
      (contains? (set snake) new-head) :self-collision
      (or (>= head-x grid-width)
          (< head-x 0)
          (>= head-y grid-height)
          (< head-y 0)) :wall-collision
      (portal? (:portals game-state) head direction) :through-portal
      :else :available)))

(defn head-direction
  "Returns a direction based on a head position and a body position"
  [head body]
  (cond
    (= (new-position head :right) body) :left
    (= (new-position head :left) body) :right
    (= (new-position head :up) body) :down
    (= (new-position head :down) body) :up))

(defn snake-direction
  "Returns the direction of the snake"
  [snake]
  (head-direction (first snake) (second snake)))

(defn move-snake
  "Moves the snake in the current direction.
   It returns the new snake body positions."
  [portals snake direction]
  (let [new-head (new-head-position portals snake direction)]
    (cons new-head (butlast snake))))

(defn update-game
  "Updates the game state based on action and game logic.
  action can be :idle, :restart, :pause, :move, :new-food-position,
  :food-expiration-tick and [:change-direction :direction]"
  [game-state action]
  (case (:status game-state)
    :init (conj game-state {:food (new-food-position game-state)
                            :status :playing})
    :paused (match action
              :pause (conj game-state {:status :playing})
              :else game-state)
    :game-over (match action
                 :restart init-game
                 :else game-state)
    :win (match action
           :restart init-game
           :else game-state)
    :playing (match action
               :idle game-state
               :pause (conj game-state {:status :paused})
               :new-food-position (conj game-state {:food (new-food-position game-state)})
               :food-expiration-tick (let [new-val (inc (:food-expiration-val game-state))]
                                       (if (> new-val (:food-expiration-ticks game-state))
                                         (conj game-state {:food (new-food-position game-state)
                                                           :food-expiration-val 0
                                                           :lives (dec (:lives game-state))
                                                           :status (if (zero? (dec (:lives game-state)))
                                                                     :game-over
                                                                     :playing)})
                                         (conj game-state {:food-expiration-val new-val})))
               :move (case (next-move-direction game-state)
                       :wall-collision (conj game-state {:status :game-over})
                       :self-collision (conj game-state {:status :game-over})
                       :available (conj game-state {:snake (move-snake (:portals game-state) (:snake game-state) (:direction game-state))})
                       :through-portal (conj game-state {:snake (move-snake (:portals game-state) (:snake game-state) (:direction game-state))
                                                         :lives (dec (:lives game-state))
                                                         :status (if (zero? (dec (:lives game-state)))
                                                                   :game-over
                                                                   :playing)})
                       :food (conj game-state {:snake (cons (new-head-position (:portals game-state) (:snake game-state) (:direction game-state)) (:snake game-state))
                                               :food (new-food-position game-state)
                                               :food-expiration-val 0
                                               :lives (inc (:lives game-state))
                                               :status (if (empty? (available-food-positions game-state))
                                                         :win
                                                         :playing)}))
               [:change-direction direction] (conj game-state {:direction direction}))))
