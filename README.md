# CS611-Assignment 2
## Dots and Boxes
---------------------------------------------------------------------------
- Name: Edaad Azman
- Email: edaad@bu.edu
- Student ID: U38459100

- Name: Saksham Goel
- Email: sakshamg@bu.edu
- Student ID: U45400025

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

## Notes
---------------------------------------------------------------------------

### Design Choices:

- **Template Method Pattern**: The abstract BoardGame class provides a consistent framework for all board games, allowing easy extension for new game types while maintaining uniform game flow and user experience.

- **Separation of Concerns**: Clear separation between CLI handling (App, GameMenu), core game framework (BoardGame, Board, Piece), and specific game logic (SlidingPuzzleGame, DotsAndBoxesGame).

- **Polymorphic Design**: The Board interface allows different board implementations, making the system extensible for various grid-based games beyond sliding puzzle and dots and boxes.

- **Encapsulation**: Each class has clear responsibilities with controlled access through public methods. SlidingPuzzleBoard encapsulates game state, Player handles input, and BoardGame manages flow.

### Cool Features / Creative Choices:

- **Smart Shuffling**: SlidingPuzzleBoard uses legal-move shuffling from solved state to guarantee puzzle solvability, preventing impossible configurations.

- **Efficient Movement**: Board caches blank tile position and uses adjacency checking for fast, valid move validation.

- **Flexible Sizing**: Supports board sizes from 2x2 up to 10x10 with proper validation and error handling.

- **Clean UI**: Terminal-based interface with clear visual representation of the puzzle state and intuitive tile-value input system.

- **Extensible Architecture**: Game collection menu system allows easy addition of new games through the BoardGame framework.



## How to compile and run
---------------------------------------------------------------------------

### Compilation
1. Navigate to the project directory:
   ```bash
   $ cd dots-and-boxes
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

```text
Output: 
Welcome to the Game Collection!

=== Game Selection Menu ===
1. Sliding Puzzle
2. Dots and Boxes
3. Exit
Enter your choice (1-3): 

Input:
2

Output: 
Starting Dots and Boxes...
Welcome to the game!
Welcome to Dots and Boxes!
Enter number of rows (2-10): 

Input:
2

Output: 
Enter number of columns (2-10): 

Input:
2

Output: 
Enter name for Player 1: 

Input:
Alice

Output: 
Enter name for Player 2: 

Input:
Bob

Output: 
Enter moves as: [H/V] [row] [col]
H = Horizontal edge, V = Vertical edge
Example: 'H 0 1' claims horizontal edge at row 0, col 1
Complete boxes to score points! Player with most boxes wins!


Current Board:
  0 1 2 
0 · · ·
       
1 · · ·
       
2 · · ·

Scores:
Alice: 0
Bob: 0


Alice's turn - Enter your move (H/V row col) or 'quit': 

Input:
H 0 0

Output: 
Current Board:
  0 1 2 
0 ·-· ·
       
1 · · ·
       
2 · · ·

Scores:
Alice: 0
Bob: 0


Bob's turn - Enter your move (H/V row col) or 'quit': 

Input:
V 0 0

Output: 
Current Board:
  0 1 2 
0 ·-· ·
  |    
1 · · ·
       
2 · · ·

Scores:
Alice: 0
Bob: 0


Alice's turn - Enter your move (H/V row col) or 'quit': 

Input:
H 0 1

Output: 
Current Board:
  0 1 2 
0 ·-·-·
  |    
1 · · ·
       
2 · · ·

Scores:
Alice: 0
Bob: 0


[...]
Some turns later
[...]

Output: 
Alice's turn - Enter your move (H/V row col) or 'quit': 

Input:
H 1 1

Output: 
Alice completed 1 box(es)! Go again!

Current Board:
  0 1 2 
0 ·-·-·
  | |A|
1 · ·-·
    |  
2 · · ·

Scores:
Alice: 1
Bob: 0

[...]
Some turns later
[...]

Output:
Current Board:
  0 1 2 
0 ·-·-·
  |A|A|
1 ·-·-·
  |B|A|
2 ·-·-·

Scores:
Alice: 3
Bob: 1


Game Over!
Winner: Alice!

Final Scores:
Alice: 3
Bob: 1

Would you like to play again? (yes/no): 

Input:
no

Output: 
Thanks for playing! Goodbye!

=== Game Selection Menu ===
1. Sliding Puzzle
2. Dots and Boxes
3. Exit
Enter your choice (1-3): 

Input:
3

Output: 
Thanks for playing! Goodbye!
```