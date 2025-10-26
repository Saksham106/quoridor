# CS611-Assignment 3
## Quoridor
---------------------------------------------------------------------------
- Name: Edaad Azman
- Email: edaad@bu.edu
- Student ID: U38459100

- Name: Saksham Goel
- Email: sakshamg@bu.edu
- Student ID: U45400025

Github: https://github.com/Saksham106/quoridor

## Files
---------------------------------------------------------------------------

### Core Classes
`App.java` — Main application entry point that launches the game menu system.

`GameMenu.java` — Terminal-based menu system for game selection between Sliding Puzzle and Dots and Boxes.

`BoardGame.java` — Abstract base class implementing the template method pattern for all board games. Handles the main game flow including setup, game loop, input processing, and replay functionality.

`Board.java` — Generic interface defining common board operations like dimensions, piece management, and solved state checking.

`Piece.java` — Abstract base class for game pieces with value, ownership, and movement capabilities.

`Player.java` — Manages player information including name, scoring, and input handling. Serves as an input handler using Scanner for user interaction.

### Sliding Puzzle Implementation
`SlidingPuzzleGame.java` — Game implementation extending BoardGame for sliding puzzle gameplay. Manages single-player game flow, setup, and move processing.

`SlidingPuzzleBoard.java` — Board implementation for sliding puzzle using 2D Tile array. Supports sizes 2x2 through 10x10, legal-move shuffle from solved state, and efficient tile movement with adjacency checking.

`Tile.java` — Concrete implementation of Piece for sliding puzzle tiles. Supports numbered tiles and blank tiles with movement validation.

### Dots and Boxes Implementation  
`DotsAndBoxesGame.java` — Game implementation for two-player Dots and Boxes gameplay.

`DotsAndBoxesBoard.java` — Board implementation using efficient 2D arrays for edge states and box ownership.

`Box.java` — Represents a box formed by four edges. Tracks edge completion status and handles automatic claiming when all four edges are completed by players.

`Edge.java` — Represents edges between dots that can be claimed by players. Supports both horizontal and vertical orientations with position tracking and adjacency checking.

### Quoridor Implementation  
`QuoridorGame.java` — Game implementation for two-player Quoridor gameplay. Manages turn based pawn movement and wall placement with mode switching between move and wall actions.

`QuoridorBoard.java` — Board implementation for 9x9 Quoridor grid using Tile array for pawn positions and boolean arrays for wall tracking. Implements BFS pathfinding for validation that wall placements don't completely block players from reaching their goals. Also adds color display with ANSI escape codes for player differentiation.

`Pawn.java` — Represents player pawns that move across the board. Tracks current position, target row for winning, and supports standard moves and jump moves over opponent pawns.

`Wall.java` — Represents wall pieces placed by players to block opponent movement. Supports horizontal and vertical orientations, spans 2 board segments, and includes overlap detection to prevent invalid placements.

`MoveValidator.java` — Static utility class for validating pawn movements. Handles orthogonal moves, jump moves over adjacent pawns, diagonal moves when jumps are blocked, and wall collision detection.

`Colors.java` — Simple utility class providing ANSI color codes for terminal output. Colors player 1 pieces blue and player 2 pieces red for visual distinction. Will be turned into a core class moving forward. 


## Notes
---------------------------------------------------------------------------

### Design Choices:

- **Template Method Pattern**: The abstract BoardGame class provides a consistent framework for all three games (Sliding Puzzle, Dots and Boxes, and Quoridor), allowing easy extension for new game types while maintaining uniform game flow and user experience. Each game implements game-specific logic (parseMove, applyMove) while inheriting shared functionality like turn management, input handling, and replay mechanics.

- **Separation of Concerns**: Clear separation between CLI handling (App, GameMenu), core game framework (BoardGame, Board, Piece), and specific game logic (SlidingPuzzleGame, DotsAndBoxesGame, QuoridorGame). Each layer has well-defined responsibilities: the framework handles common game flow, while individual implementations focus on their unique rules and mechanics.

- **Polymorphic Design**: The Board interface and Piece base class enable polymorphism across all games. Different board implementations (SlidingPuzzleBoard, DotsAndBoxesBoard, QuoridorBoard) can be treated uniformly by BoardGame, while different piece types (Tile, Box, Edge, Pawn, Wall) share common behavior through the Piece hierarchy. This makes the system highly extensible for various grid-based games.

- **Encapsulation**: Each class has clear responsibilities with controlled access through public methods. Game boards encapsulate their state and validation logic, Player handles all input operations, Piece subclasses manage their own display and movement rules, and BoardGame orchestrates the overall flow without needing to know implementation details of specific games.

### Cool Features / Creative Choices:

- **Smart Shuffling**: SlidingPuzzleBoard uses legal-move shuffling from solved state to guarantee puzzle solvability, preventing impossible configurations.

- **Automatic Box Claiming (Dots and Boxes)**: DotsAndBoxesBoard automatically detects and claims completed boxes when the fourth edge is placed, with instant score updates and bonus turn mechanics for players who complete boxes.

- **BFS Pathfinding (Quoridor)**: QuoridorBoard implements Breadth-First Search algorithm to validate wall placements in real-time, ensuring players always maintain a valid path to their goal. This prevents frustrating blocking scenarios while maintaining strategic gameplay.

- **Color-Coded Display (Quoridor)**: Terminal output uses ANSI escape codes to distinguish player 1 (blue) and player 2 (red) pieces, making it easy to identify pawns and walls at a glance. Moving forward, this can apply to multi player based games.

- **Flexible Board Sizing**: Sliding Puzzle supports customizable board sizes from 2x2 to 10x10, while Dots and Boxes allows dynamic row/column configuration, all with proper validation and error handling.

- **Mode Switching (Quoridor)**: Players can toggle between pawn movement and wall placement modes using the 'switch' command, providing flexible turn-based strategy without rigid alternating requirements.

- **Extensible Architecture**: Game collection menu system allows easy addition of new games through the BoardGame framework, with consistent user experience across all three games.


## How to compile and run
---------------------------------------------------------------------------

### Compilation
1. Navigate to the project directory:
   ```bash
   $ cd quoridor
   ```

2. Compile all Java files into ./out:
   ```bash
   $ mkdir -p out
   $ javac -d out $(find src -name "*.java")
   # try if above does not work
   $ javac -d out src/*.java   
   ```


### Running the Application
1. Run the main application:
   ```bash
   $ java -cp out App   
   ```

2. Follow the on-screen prompts to:
   - Choose between available games
   - Set up player names
   - Play the game
   - Choose to play again or exit


## Input/Output Example
---------------------------------------------------------------------------
Note: Color printing doesn't show up on README.md
```text
Welcome to the Game Collection!

=== Game Selection Menu ===
1. Sliding Puzzle
2. Dots and Boxes
3. Quoridor
4. Exit
Enter your choice (1-4): 3

Starting Quoridor...
Welcome to the game!
Welcome to Quoridor!
Be the first to reach the opposite side!

Enter name for Player 1: a
Enter name for Player 2: b
Rules:
1. Each turn: choose to move your pawn
2. Pawn moves: 'up', 'down', 'left', 'right'
3. Wall placement: 'wall h row col' or 'wall v row col'
4. Type 'switch' to toggle between move/wall modes
5. You have 10 walls left


Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · B · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
                    
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · A · · · ·

Walls Remaining:
a: 10
b: 10


a's turn (move pawn) - Enter your move or 'quit': up

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · B · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
                    
 6 · · · · · · · · ·
                    
 7 · · · · A · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 10


b's turn (move pawn) - Enter your move or 'quit': down

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · B · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
                    
 6 · · · · · · · · ·
                    
 7 · · · · A · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 10


a's turn (move pawn) - Enter your move or 'quit': up

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · B · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
                    
 6 · · · · A · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 10


b's turn (move pawn) - Enter your move or 'quit': switch
Now in wall mode.

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · B · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
                    
 6 · · · · A · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 10


b's turn (place wall) - Enter your move or 'quit': wall h 5 3 

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · B · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
         ---        
 6 · · · · A · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 9


a's turn (move pawn) - Enter your move or 'quit': right

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · B · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
         ---        
 6 · · · · · A · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 9


b's turn (move pawn) - Enter your move or 'quit': down

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · B · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · · · · ·
         ---        
 6 · · · · · A · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 9


a's turn (move pawn) - Enter your move or 'quit': up

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · B · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · A · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 9


b's turn (move pawn) - Enter your move or 'quit': switch
Now in wall mode.

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · B · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
                    
 5 · · · · · A · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 9


b's turn (place wall) - Enter your move or 'quit': wall h 4 3 

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · B · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · · · · ·
         ---        
 5 · · · · · A · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 8


a's turn (move pawn) - Enter your move or 'quit': up 

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · B · · · ·
                    
 3 · · · · · · · · ·
                    
 4 · · · · · A · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 8


b's turn (move pawn) - Enter your move or 'quit': down

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · B · · · ·
                    
 4 · · · · · A · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 8


a's turn (move pawn) - Enter your move or 'quit': switch
Now in wall mode.

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · B · · · ·
                    
 4 · · · · · A · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 10
b: 8


a's turn (place wall) - Enter your move or 'quit': wall v 3 4 

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · B|· · · ·
                    
 4 · · · · ·|A · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 9
b: 8


b's turn (move pawn) - Enter your move or 'quit': down

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · ·|· · · ·
                    
 4 · · · · B|A · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 9
b: 8


a's turn (move pawn) - Enter your move or 'quit': switch
Now in wall mode.

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · · ·|· · · ·
                    
 4 · · · · B|A · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 9
b: 8


a's turn (place wall) - Enter your move or 'quit': wall v 3 3

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · ·|·|· · · ·
                    
 4 · · · ·|B|A · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


b's turn (move pawn) - Enter your move or 'quit': right

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · ·|·|· · · ·
                    
 4 · · · ·|·|A B · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


a's turn (move pawn) - Enter your move or 'quit': up

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · ·|·|A · · ·
                    
 4 · · · ·|·|· B · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


b's turn (move pawn) - Enter your move or 'quit': down

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · ·|·|A · · ·
                    
 4 · · · ·|·|· · · ·
         ---        
 5 · · · · · · B · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


a's turn (move pawn) - Enter your move or 'quit': up

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · A · · ·
                    
 3 · · · ·|·|· · · ·
                    
 4 · · · ·|·|· · · ·
         ---        
 5 · · · · · · B · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


b's turn (move pawn) - Enter your move or 'quit': down

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · A · · ·
                    
 3 · · · ·|·|· · · ·
                    
 4 · · · ·|·|· · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · B · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


a's turn (move pawn) - Enter your move or 'quit': up

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · A · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · ·|·|· · · ·
                    
 4 · · · ·|·|· · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · B · ·
                    
 7 · · · · · · · · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


b's turn (move pawn) - Enter your move or 'quit': down

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · · · · ·
                    
 1 · · · · · A · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · ·|·|· · · ·
                    
 4 · · · ·|·|· · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · B · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


a's turn (move pawn) - Enter your move or 'quit': up

Quoridor Board:
    0 1 2 3 4 5 6 7 8
 0 · · · · · A · · ·
                    
 1 · · · · · · · · ·
                    
 2 · · · · · · · · ·
                    
 3 · · · ·|·|· · · ·
                    
 4 · · · ·|·|· · · ·
         ---        
 5 · · · · · · · · ·
         ---        
 6 · · · · · · · · ·
                    
 7 · · · · · · B · ·
                    
 8 · · · · · · · · ·

Walls Remaining:
a: 8
b: 8


a wins! You reached the opposite side!
Would you like to play again? (yes/no): no
Thanks for playing! Goodbye!
```