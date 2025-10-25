/**
 * Abstract base class for all board games.
 * Implements the template method pattern for consistent game flow.
 */
public abstract class BoardGame {
    protected final java.util.List<Player> players;
    protected int currentPlayerIndex;
    protected int moveCount;

    // Constructor for single player games
    protected BoardGame(Player player) {
        this.players = new java.util.ArrayList<>();
        this.players.add(player);
        this.currentPlayerIndex = 0;
        this.moveCount = 0;
    }

    // Constructor for multiplayer games
    protected BoardGame(java.util.List<Player> players) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("At least one player required");
        }
        this.players = new java.util.ArrayList<>(players);
        this.currentPlayerIndex = 0;
        this.moveCount = 0;
    }

    // Abstract methods that must be implemented by concrete game classes
    protected abstract Board getBoard();
    protected abstract void setup();
    protected abstract Integer parseMove(String token);
    protected abstract boolean applyMove(int tile);
    protected abstract boolean handleSpecialCommand(String command);

    // Player management methods
    protected Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    protected void switchToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    protected boolean isMultiplayer() {
        return players.size() > 1;
    }

    protected java.util.List<Player> getPlayers() {
        return new java.util.ArrayList<>(players);
    }

    protected int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // Override this method in subclasses for custom player switching logic
    protected void handlePlayerSwitch() {
        switchToNextPlayer();
    }

    protected String getWelcomeMessage() {
        return "Welcome to the game!";
    }

    protected abstract String getInstructions();

    protected String getInputPrompt() {
        return "Enter your move, 'shuffle', or 'quit': ";
    }

    protected String getInvalidInputMessage() {
        return "Invalid input! Please try again.";
    }

    protected String getInvalidMoveMessage() {
        return "Invalid move! Please try again.";
    }

    protected String getVictoryMessage() {
        return "Congratulations! You won in " + moveCount + " moves!";
    }

    protected String getGoodbyeMessage() {
        return "Thanks for playing! Goodbye!";
    }

    protected String getQuitCommand() {
        return "quit";
    }

    protected String getPlayAgainPrompt() {
        return "Would you like to play again? (yes/no): ";
    }

    /**
     * Main game loop implementing the template method pattern.
     * Handles setup, gameplay, and replay functionality.
     */
    public final void run() {
        boolean playAgain = true;

        while (playAgain) {
            System.out.println(getWelcomeMessage());
            setup();
            this.moveCount = 0;
            System.out.println(getInstructions() + "\n");
            System.out.println(getBoard().toString());

            // Main game loop
            while (!getBoard().isSolved()) {
                String line = getCurrentPlayer().getInput(getInputPrompt());
                if (line == null) {
                    return;
                }
                if (line.equalsIgnoreCase(getQuitCommand())) {
                    System.out.println(getGoodbyeMessage());
                    return;
                }

                // Handle special commands (like shuffle)
                if (handleSpecialCommand(line)) {
                    System.out.println(getBoard().toString());
                    continue;
                }

                // Parse and apply the move
                Integer tile = parseMove(line);
                if (tile == null) {
                    System.out.println(getInvalidInputMessage());
                    continue;
                }
                boolean moved = applyMove(tile);
                if (moved) {
                    moveCount++;
                    // For single player games, don't switch players
                    if (isMultiplayer()) {
                        handlePlayerSwitch();
                    }
                } else {
                    System.out.println(getInvalidMoveMessage());
                }
                System.out.println(getBoard().toString());
            }

            // Handle game completion
            if (getBoard().isSolved()) {
                System.out.println(getVictoryMessage());
                String response = getCurrentPlayer().getInput(getPlayAgainPrompt());
                playAgain = "yes".equals(response);
            } else {
                playAgain = false;
            }
        }

        System.out.println(getGoodbyeMessage());
    }
}
