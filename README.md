# Checkers Game Manager

This project implements a Checkers game using the **Model-View-Controller (MVC)** architecture. It includes full game logic, a graphical user interface (GUI), and unit tests for core functionalities.

## Features

- **Game Modes**:
  - Player vs. Player (PvP)
  - Player vs. Computer (PvC)
- **Core Mechanics**:
  - Move validation for standard and king pieces.
  - Captures and multi-step jumps.
  - Piece promotion to kings.
  - Win and draw detection.
- **Interactive GUI**:
  - Highlights movable pieces and valid moves.
  - Visual representation of the board with pieces.
  - Turn tracking and scoring.
- **Unit Testing**:
  - Comprehensive JUnit tests for key game logic.

## Directory Structure

```plaintext
.
├── src
│   ├── controller
│   │   ├── GameManager.java
│   │   ├── Player.java
│   │   ├── Computer.java
│   │   ├── GameOverListener.java
│   │   ├── GameStateEnum.java
│   │   └── ...
│   ├── model
│   │   ├── GameBoard.java
│   │   ├── Piece.java
│   │   ├── Move.java
│   │   ├── Color.java
│   │   └── ...
│   ├── view
│   │   ├── Game.java
│   │   ├── Menu.java
│   │   ├── Cell.java
│   │   ├── SoundPlayer.java
│   │   └── ...
│   ├── tests
│   │   ├── GameManagerTest.java
│   │   └── ...
├── assets
│   ├── bg2.jpg
│   ├── white_man.png
│   ├── black_man.png
│   ├── white_king.png
│   ├── black_king.png
│   └── ...
├── README.md
└── ...
```

## Setup Instructions

### Prerequisites
- Java 8 or later
- An IDE like IntelliJ IDEA, Eclipse, or VS Code with Java support
- Maven (optional, for dependency management)

### Running the Game
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/checkers-game.git
   cd checkers-game
   ```
2. Open the project in your IDE.
3. Compile and run the `view.Checkers` class to start the game.

### Running Tests
1. Ensure you have JUnit 5 configured in your IDE.
2. Run all test cases in the `tests` package.

## Design Overview

### Model-View-Controller (MVC) Pattern
- **Model**:
  - Contains the game state and logic (e.g., `GameBoard`, `Piece`, `Move`).
  - Encapsulates data and ensures clean interactions.
- **View**:
  - Manages the graphical user interface (e.g., `Game`, `Menu`, `Cell`).
  - Displays the game board and interacts with users.
- **Controller**:
  - Orchestrates game flow and logic (e.g., `GameManager`, `Player`, `Computer`).
  - Bridges the model and view.

### Encapsulation and Modularity
- The project enforces encapsulation to protect game state and maintain modularity.
- Classes expose only necessary methods, ensuring clear boundaries.

## Testing
- **`GameManagerTest.java`**:
  - Tests for piece selection, movement, turn switching, and game over conditions.
- Use `System.out.println` for debugging when necessary during development.

## Assets
- Background image and piece icons are stored in the `assets/` directory.
- Replace or add new assets if needed.

## Contributors
- **Amyra White, Suhani Surana, Abrorjon Asralov, Pulat Uralov** - Game logic, GUI design, unit testing
- **Contributors Welcome!**
