import java.util.*;

public class QuoridorBoard implements Board {
    public static final int BOARD_SIZE = 9;
    public static final int MAX_WALLS_PER_PLAYER = 10;

    private final Tile[][] grid;
    private final Pawn[][] pawnPositions;
    private final List<Wall> placedWalls;
    private final Map<String, Integer> wallCounts;
    private final List<String> playerNames;

    private final boolean[][] horizontalWalls;
    private final boolean[][] verticalWalls;

    public QuoridorBoard(List<String> playerNames) {
        if (playerNames == null || playerNames.size() != 2) {
            throw new IllegalArgumentException("Quoridor requires exactly 2 players");
        }

        this.playerNames = new ArrayList<>(playerNames);
        this.grid = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.pawnPositions = new Pawn[BOARD_SIZE][BOARD_SIZE];
        this.placedWalls = new ArrayList<>();
        this.wallCounts = new HashMap<>();

        this.horizontalWalls = new boolean[BOARD_SIZE - 1][BOARD_SIZE];
        this.verticalWalls = new boolean[BOARD_SIZE][BOARD_SIZE - 1];

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                grid[r][c] = new Tile();
            }
        }

        for (String playerName : playerNames) {
            wallCounts.put(playerName, MAX_WALLS_PER_PLAYER);
        }

        initializePawns();
    }

    private void initializePawns() {
        Pawn pawn1 = new Pawn(playerNames.get(0), 8, 4, 0);
        grid[8][4].setPiece(pawn1);
        pawnPositions[8][4] = pawn1;

        Pawn pawn2 = new Pawn(playerNames.get(1), 0, 4, 8);
        grid[0][4].setPiece(pawn2);
        pawnPositions[0][4] = pawn2;
    }

    @Override
    public int rows() {
        return BOARD_SIZE;
    }

    @Override
    public int cols() {
        return BOARD_SIZE;
    }

    @Override
    public Piece getPieceAt(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        return grid[row][col].getPiece();
    }

    @Override
    public void setPieceAt(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            grid[row][col].setPiece(piece);
        }
    }

    @Override
    public boolean isSolved() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.hasWon()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Pawn getPawnForPlayer(String playerName) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.getPlayerName().equals(playerName)) {
                        return pawn;
                    }
                }
            }
        }
        return null;
    }

    public String getWinner() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.hasWon()) {
                        return pawn.getPlayerName();
                    }
                }
            }
        }
        return null;
    }

    public int getWallCount(String playerName) {
        return wallCounts.getOrDefault(playerName, 0);
    }

    public Pawn[][] getPawnPositions() {
        return pawnPositions;
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public List<Wall> getPlacedWalls() {
        return new ArrayList<>(placedWalls);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nQuoridor Board:\n");

        sb.append("   ");
        for (int c = 0; c < BOARD_SIZE; c++) {
            sb.append(" ").append(c);
        }
        sb.append("\n");

        for (int r = 0; r < BOARD_SIZE; r++) {
            sb.append(String.format("%2d ", r));

            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    sb.append(piece.getDisplayString());
                } else {
                    sb.append("·");
                }

                if (c < BOARD_SIZE - 1) {
                    if (verticalWalls[r][c]) {
                        sb.append("|");
                    } else {
                        sb.append(" ");
                    }
                }
            }
            sb.append("\n");

            if (r < BOARD_SIZE - 1) {
                sb.append("   ");
                for (int c = 0; c < BOARD_SIZE; c++) {
                    if (horizontalWalls[r][c]) {
                        sb.append("-");
                    } else {
                        sb.append(" ");
                    }
                    if (c < BOARD_SIZE - 1) {
                        sb.append(" ");
                    }
                }
                sb.append("\n");
            }
        }

        sb.append("\nWalls Remaining:\n");
        for (String playerName : playerNames) {
            sb.append(playerName).append(": ").append(wallCounts.get(playerName)).append("\n");
        }

        return sb.toString();
    }
}