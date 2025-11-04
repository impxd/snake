(ns snake.utils
    (:import
    [com.raylib Raylib Raylib$Vector2]))

;; Drawing

(defn draw-text-centered
  "Draw centered text at given x and y position,
   based on the text width."
  [text x y font-size color]
  (let [text-width (Raylib/MeasureText text font-size)]
    (Raylib/DrawText text (- x (quot text-width 2)) (- y (quot font-size 2)) font-size color)))

(defn vector2 [x y]
  (doto (Raylib$Vector2.) (.x x) (.y y)))

;; Tick utilities

(defn get-tick-value
  [game-state tick-key]
  (get-in game-state [:ticks tick-key :val]))

(defn get-tick-time
  [game-state tick-key]
  (get-in game-state [:ticks tick-key :time]))

(defn get-tick-percent
  [game-state tick-key]
  (let [tick (get-in game-state [:ticks tick-key])
        val (:val tick)
        time (:time tick)]
    (/ val time)))

(defn update-tick-values
  [delta-time game-state]
  (reduce (fn [gs tick-key]
            (let [tick (get-in gs [:ticks tick-key])
                  new-val (+ (:val tick) delta-time)
                  capped-val (if (>= new-val (:time tick)) 0 new-val)]
              (assoc-in gs [:ticks tick-key :val] capped-val)))
          game-state
          (keys (:ticks game-state))))
