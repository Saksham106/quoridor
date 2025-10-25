/**
 * Game implementation for sliding puzzle.
 * Manages single-player sliding puzzle gameplay.
 */
public class SlidingPuzzleGame extends BoardGame {
    private SlidingPuzzleBoard board;

    public SlidingPuzzleGame(Player player) {
        super(player);
    }

    @Override
    protected Board getBoard() {
        return board;
    }

    @Override
    protected String getWelcomeMessage() {
        return "Welcome to the Sliding Puzzle Game! Arrange the numbeed tiles such that tiles are ordered from least to greatest from top left to bottom right.";
    }

    @Override
    protected String getInstructions() {
        return "Enter the number of the tile you want to move. Type 'shuffle' to reshuffle the board. Enter 'quit' to exit.";
    }

    @Override
    protected void setup() {
        String nameIn = getCurrentPlayer().getInput("Enter your name: ");
        if (!nameIn.isEmpty()) {
            getCurrentPlayer().setName(nameIn);
        }

        int r = getValidSize(
                "Enter number of rows (" + SlidingPuzzleBoard.MIN_SIZE + "-" + SlidingPuzzleBoard.MAX_SIZE + "): ");
        int c = getValidSize(
                "Enter number of cols (" + SlidingPuzzleBoard.MIN_SIZE + "-" + SlidingPuzzleBoard.MAX_SIZE + "): ");

        this.board = new SlidingPuzzleBoard(r, c);
        System.out.println("Good luck, " + getCurrentPlayer().getName() + "!");
    }
    
    // Get a valid board size from the player
    private int getValidSize(String prompt) {
        while (true) {
            String input = getCurrentPlayer().getInput(prompt);
            if (input == null) {
                return 3;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= SlidingPuzzleBoard.MIN_SIZE && value <= SlidingPuzzleBoard.MAX_SIZE) {
                    return value;
                }
                System.out.println("Please enter an integer between " +
                        SlidingPuzzleBoard.MIN_SIZE + " and " + SlidingPuzzleBoard.MAX_SIZE + ".");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    @Override
    protected Integer parseMove(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected boolean applyMove(int tile) {
        return board.trySlideTile(tile);
    }

    @Override
    protected boolean handleSpecialCommand(String command) {
        if (command.equalsIgnoreCase("shuffle")) {
            board.shuffle();
            System.out.println("Board shuffled.");
            return true;
        }
        return false;
    }

}
