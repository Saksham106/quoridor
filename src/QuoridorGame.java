import java.util.*;

public class QuoridorGame extends BoardGame {
    private QuoridorBoard board;
    private List<String> playerNames;
    private String nextMoveType; // "move" or "wall"

    public QuoridorGame(Player player1, Player player2) {
        super(Arrays.asList(player1, player2));
        this.playerNames = new ArrayList<>();
        this.nextMoveType = "move";
    }

    @Override
    protected Board getBoard() {
        return board;
    }

    @Override
    protected void setup() {
        System.out.println("Welcome to Quoridor!");
        System.out.println("Be the first to reach the opposite side!\n");

        // Clear player names for replay
        playerNames.clear();

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

        getPlayers().get(0).setName(player1Name);
        getPlayers().get(1).setName(player2Name);

        board = new QuoridorBoard(playerNames);
    }

    @Override
    protected Integer parseMove(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] parts = input.trim().split("\\s+");

        if (parts.length == 1) {
            String direction = parts[0].toLowerCase();
            if (isValidDirection(direction)) {
                return getDirectionCode(direction);
            }
        } else if (parts.length == 4) {
            if (parts[0].equalsIgnoreCase("wall")) {
                char orientation = parts[1].toUpperCase().charAt(0);
                try {
                    int row = Integer.parseInt(parts[2]);
                    int col = Integer.parseInt(parts[3]);
                    if (orientation == 'H' || orientation == 'V') {
                        return getWallCode(orientation, row, col);
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        return null;
    }

    @Override
    protected boolean applyMove(int moveCode) {
        if (nextMoveType.equals("move")) {
            return applyPawnMove(moveCode);
        } else {
            return applyWallMove(moveCode);
        }
    }

    private boolean applyPawnMove(int directionCode) {
        String direction = getDirectionFromCode(directionCode);

        // Try one-step move first
        if (board.movePawn(getCurrentPlayer().getName(), direction)) {
            return true;
        }

        // If one-step fails, try two-step jump in same direction
        Pawn pawn = board.getPawnForPlayer(getCurrentPlayer().getName());
        if (pawn == null) {
            return false;
        }

        int currentRow = pawn.getRow();
        int currentCol = pawn.getCol();
        int jumpRow = currentRow;
        int jumpCol = currentCol;

        switch (direction.toLowerCase()) {
            case "up":
                jumpRow -= 2;
                break;
            case "down":
                jumpRow += 2;
                break;
            case "left":
                jumpCol -= 2;
                break;
            case "right":
                jumpCol += 2;
                break;
            default:
                return false;
        }

        // Try two-step jump
        if (MoveValidator.canMovePawn(currentRow, currentCol, jumpRow, jumpCol,
                board.getPawnPositions(), board.getHorizontalWalls(),
                board.getVerticalWalls())) {
            board.movePawnTwoSteps(getCurrentPlayer().getName(), direction);
            return true;
        }

        return false;
    }

    private boolean applyWallMove(int wallCode) {
        Wall.Orientation orientation = getWallOrientationFromCode(wallCode);
        int row = getWallRowFromCode(wallCode);
        int col = getWallColFromCode(wallCode);
        Wall wall = new Wall(orientation, row, col, getCurrentPlayer().getName());
        return board.placeWall(wall);
    }

    @Override
    protected boolean handleSpecialCommand(String command) {
        if (command.equalsIgnoreCase("switch")) {
            nextMoveType = nextMoveType.equals("move") ? "wall" : "move";
            System.out.println("Now in " + nextMoveType + " mode.");
            return true;
        }
        return false;
    }

    @Override
    protected String getInstructions() {
        String mode = nextMoveType.equals("move") ? "move your pawn" : "place a wall";
        String wallsRemaining = board.getWallCount(getCurrentPlayer().getName()) + " walls left";

        return "Rules:\n" +
                "1. Each turn: choose to " + mode + "\n" +
                "2. Pawn moves: 'up', 'down', 'left', 'right'\n" +
                "3. Wall placement: 'wall h row col' or 'wall v row col'\n" +
                "4. Type 'switch' to toggle between move/wall modes\n" +
                "5. You have " + wallsRemaining;
    }

    @Override
    protected String getInputPrompt() {
        String mode = nextMoveType.equals("move") ? "move pawn" : "place wall";
        return "\n" + getCurrentPlayer().getName() + "'s turn (" + mode + ") - Enter your move or 'quit': ";
    }

    @Override
    protected String getVictoryMessage() {
        String winner = board.getWinner();
        if (winner != null) {
            return "\n" + winner + " wins! You reached the opposite side!";
        }
        return "\nGame Over!";
    }

    @Override
    protected void handlePlayerSwitch() {
        switchToNextPlayer();
        nextMoveType = "move"; // Start next turn with move
    }

    private boolean isValidDirection(String direction) {
        return direction.equals("up") || direction.equals("down") ||
                direction.equals("left") || direction.equals("right");
    }

    private int getDirectionCode(String direction) {
        switch (direction) {
            case "up":
                return 1;
            case "down":
                return 2;
            case "left":
                return 3;
            case "right":
                return 4;
            default:
                return 0;
        }
    }

    private String getDirectionFromCode(int code) {
        switch (code) {
            case 1:
                return "up";
            case 2:
                return "down";
            case 3:
                return "left";
            case 4:
                return "right";
            default:
                return "";
        }
    }

    private int getWallCode(char orientation, int row, int col) {
        int orientCode = (orientation == 'H') ? 1 : 2;
        return orientCode * 10000 + row * 100 + col;
    }

    private Wall.Orientation getWallOrientationFromCode(int code) {
        return (code / 10000 == 1) ? Wall.Orientation.HORIZONTAL : Wall.Orientation.VERTICAL;
    }

    private int getWallRowFromCode(int code) {
        return (code % 10000) / 100;
    }

    private int getWallColFromCode(int code) {
        return code % 100;
    }
}
