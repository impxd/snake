# Snake Game in Clojure

Welcome to the Snake Game implemented in **Clojure**! This project is a simple yet fun implementation of the classic Snake game, using the **Raylib** library for rendering and user interaction.

## Features
- **Dynamic Gameplay**: Move the snake, collect food, and avoid collisions.
- **Portals**: Teleport the snake through predefined portal positions.
- **Game States**: Includes states like `:init`, `:playing`, `:paused`, `:game-over`, and `:win`.
- **Customizable Grid**: Adjust grid size, cell size, and screen dimensions.
- **Tick-based Updates**: Smooth gameplay with tick-based movement and food expiration.

## Getting Started

### Prerequisites
- **Clojure**: Ensure you have Clojure installed. You can follow the [official installation guide](https://clojure.org/guides/getting_started).
- **Leiningen**: This project uses Leiningen for dependency management and running the application. Install it from [here](https://leiningen.org/).

### Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd snake
   ```
2. Install dependencies:
   ```bash
   lein deps
   ```

### Running the Game
To start the game, run the following command:
```bash
lein run
```

## Project Structure
The project is organized as follows:

```
src/
  snake/
    app.clj      ; Main game loop and initialization
    core.clj     ; Entry point for the application
    model.clj    ; Game state and data structures
    update.clj   ; Game logic and state updates
    utils.clj    ; Utility functions for drawing and ticks
    view.clj     ; Rendering logic for the game
```

### Key Files
- **`app.clj`**: Contains the main game loop and window initialization.
- **`core.clj`**: Entry point for the application (`-main` function).
- **`model.clj`**: Defines the game state, including the grid, snake, food, and portals.
- **`update.clj`**: Handles game logic, such as snake movement, collisions, and food spawning.
- **`utils.clj`**: Utility functions for drawing and tick management.
- **`view.clj`**: Handles rendering the game state using Raylib.

## Controls

| Key          | Action                          |
|--------------|---------------------------------|
| **ESC** | Close the game   |
| **Arrow Keys** | Change the snake's direction   |
| **P**        | Pause/Resume the game          |
| **R**        | Restart the game after `Game Over` or `Win` |

## Customization
You can modify the game settings in `model.clj`:
- **Grid Dimensions**: `:grid-width`, `:grid-height`
- **Cell Size**: `:cell-size`
- **Screen Dimensions**: `:screen-width`, `:screen-height`
- **Snake Initial Position**: `:snake`
- **Portal Positions**: `:portals`
- **Snake Speed**: `:move {:time 0.30 :val 0}`

## Screenshots

<img width="550" height="427" alt="Screenshot 2025-11-04 at 2 32 06 p m" src="https://github.com/user-attachments/assets/5e85930f-23ba-4dc0-8ec8-203a431559b8" />

<img width="550" height="427" alt="Screenshot 2025-11-04 at 1 42 45 p m" src="https://github.com/user-attachments/assets/d6c145bd-ee70-43df-a9ed-e249adbae6ea" />

## Dependencies
- **Clojure**: Functional programming language.
- **Raylib**: Simple and easy-to-use library for game development.
- **clojure.core.match**: Pattern matching library for Clojure.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments
- [Raylib](https://www.raylib.com/) for the rendering library.
- [The Elm Architecture](https://guide.elm-lang.org/architecture/)

Enjoy the game!
