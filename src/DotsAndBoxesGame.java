import java.util.*;
import java.util.Arrays;

/**
 * Game implementation for dots and boxes.
 * Manages two-player gameplay with scoring and turn management.
 */
public class DotsAndBoxesGame extends BoardGame {
    private DotsAndBoxesBoard dotsBoard;
    private List<String> playerNames;
    private boolean currentPlayerScored;  // Whether current player scored (gets another turn)

    public DotsAndBoxesGame(Player player1, Player player2) {
        super(Arrays.asList(player1, player2));
        this.playerNames = new ArrayList<>();
        this.currentPlayerScored = false;
    }

    @Override
    protected Board getBoard() {
        return dotsBoard;
    }

    @Override
    protected void setup() {
        System.out.println("Welcome to Dots and Boxes!");

        // Get board rows
        int rows;
        do {
            String input = getCurrentPlayer().getInput("Enter number of rows (2-10): ");
            try {
                rows = Integer.parseInt(input);
                if (rows >= 2 && rows <= 10)
                    break;
                System.out.println("Rows must be between 2 and 10.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                rows = 0;
            }
        } while (true);

        // Get board columns
        int cols;
        do {
            String input = getCurrentPlayer().getInput("Enter number of columns (2-10): ");
            try {
                cols = Integer.parseInt(input);
                if (cols >= 2 && cols <= 10)
                    break;
                System.out.println("Columns must be between 2 and 10.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                cols = 0;
            }
        } while (true);

        // Get player names
        String player1Name = getCurrentPlayer().getInput("Enter name for Player 1: ");
        if (player1Name.trim().isEmpty()) {
            player1Name = "Player 1";
        }

        String player2Name = getCurrentPlayer().getInput("Enter name for Player 2: ");
        if (player2Name.trim().isEmpty()) {
            player2Name = "Player 2";
        }

        playerNames.add(player1Name);
        playerNames.add(player2Name);

        // Set player names
        getPlayers().get(0).setName(player1Name);
        getPlayers().get(1).setName(player2Name);

        // Create board
        dotsBoard = new DotsAndBoxesBoard(rows, cols, playerNames);

        dotsBoard.displayBoard();
    }

    // Store the last parsed move for use in applyMove
    private Edge.EdgeMove lastParsedMove;

    @Override
    protected Integer parseMove(String input) {
        Edge.EdgeMove move = Edge.parseMove(input);
        if (move != null && move.isValid()) {
            // Store the move for use in applyMove
            lastParsedMove = move;
            // Return a simple hash code as the move identifier
            return move.hashCode();
        }
        lastParsedMove = null;
        return null;
    }

    @Override
    protected boolean applyMove(int moveHash) {
        // Use the stored EdgeMove instead of decoding
        if (lastParsedMove == null || !lastParsedMove.isValid()) {
            return false;
        }

        // Get scores before the move to check if player scored
        String currentPlayerName = getCurrentPlayer().getName();
        Map<String, Integer> scoresBefore = dotsBoard.getScores();
        int playerScoreBefore = scoresBefore.get(currentPlayerName);

        if (dotsBoard.claimEdge(lastParsedMove, currentPlayerName)) {
            // Check if player scored (completed boxes)
            Map<String, Integer> scoresAfter = dotsBoard.getScores();
            int playerScoreAfter = scoresAfter.get(currentPlayerName);

            if (playerScoreAfter > playerScoreBefore) {
                int boxesScored = playerScoreAfter - playerScoreBefore;
                System.out.println(currentPlayerName + " completed " + boxesScored + " box(es)! Go again!");
                // Player scored - they get another turn
                currentPlayerScored = true;
            } else {
                // Player didn't score - normal turn switch will happen
                currentPlayerScored = false;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean handleSpecialCommand(String command) {
        // No special commands for Dots and Boxes
        return false;
    }

    @Override
    protected String getInstructions() {
        return "Enter moves as: [H/V] [row] [col]\n" +
                "H = Horizontal edge, V = Vertical edge\n" +
                "Example: 'H 0 1' claims horizontal edge at row 0, col 1\n" +
                "Complete boxes to score points! Player with most boxes wins!";
    }

    @Override
    protected String getInputPrompt() {
        return "\n" + getCurrentPlayer().getName() + "'s turn - Enter your move (H/V row col) or 'quit': ";
    }

    @Override
    protected String getVictoryMessage() {
        String winner = dotsBoard.getWinner();
        StringBuilder message = new StringBuilder("\nGame Over!\n");

        if (winner != null) {
            message.append("Winner: ").append(winner).append("!\n");
        } else {
            message.append("It's a tie!\n");
        }

        message.append("\nFinal Scores:\n");
        Map<String, Integer> finalScores = dotsBoard.getScores();
        for (String playerName : playerNames) {
            message.append(playerName).append(": ").append(finalScores.get(playerName)).append("\n");
        }

        return message.toString();
    }

    @Override
    protected void handlePlayerSwitch() {
        // Only switch players if the current player didn't score
        // Players who complete boxes get another turn
        if (!currentPlayerScored) {
            switchToNextPlayer();
        }
        // Reset the flag for the next move
        currentPlayerScored = false;
    }

}