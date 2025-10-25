import java.util.*;

/**
 * Board implementation for sliding puzzle game.
 * Uses 2D array of tiles with smart shuffling and efficient movement.
 */
public class SlidingPuzzleBoard implements Board {
    public static final int MIN_SIZE = 2;
    public static final int MAX_SIZE = 10;

    private final int rows;
    private final int cols;
    private final Tile[][] grid;

    public SlidingPuzzleBoard(int rows, int cols) {
        this(rows, cols, true);
    }

    private SlidingPuzzleBoard(int rows, int cols, boolean shouldShuffle) {
        if (rows < MIN_SIZE || cols < MIN_SIZE)
            throw new IllegalArgumentException("Minimum board size is " + MIN_SIZE + "x" + MIN_SIZE + ".");
        if (rows > MAX_SIZE || cols > MAX_SIZE)
            throw new IllegalArgumentException("Maximum board size is " + MAX_SIZE + "x" + MAX_SIZE + ".");
        this.rows = rows;
        this.cols = cols;
        this.grid = new Tile[rows][cols];
        initSolved();
        if (shouldShuffle) {
            shuffle();
        }
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public Piece getPieceAt(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        return grid[row][col];
    }

    @Override
    public void setPieceAt(int row, int col, Piece piece) {
        if (isValidPosition(row, col) && piece instanceof Tile) {
            grid[row][col] = (Tile) piece;
        }
    }

    private void initSolved() {
        int v = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = Tile.createNumberedTile(v++);
            }
        }
        grid[rows - 1][cols - 1] = Tile.createBlankTile();
    }

    /**
     * Shuffle the board using legal moves to guarantee solvability.
     * Performs 100 random legal moves from the solved state.
     */
    public void shuffle() {
        Random rng = new Random();
        for (int i = 0; i < 100; i++) {
            int[] blankPos = findBlankTile();
            if (blankPos != null) {
                List<int[]> adjacentTiles = getAdjacentTiles(blankPos[0], blankPos[1]);
                if (!adjacentTiles.isEmpty()) {
                    int[] randomTile = adjacentTiles.get(rng.nextInt(adjacentTiles.size()));
                    swapTiles(blankPos[0], blankPos[1], randomTile[0], randomTile[1]);
                }
            }
        }
    }

    private List<int[]> getAdjacentTiles(int blankRow, int blankCol) {
        int[][] adjacentPositions = getAdjacentPositions(blankRow, blankCol);
        List<int[]> adjacent = new ArrayList<>();
        for (int[] pos : adjacentPositions) {
            adjacent.add(pos);
        }
        return adjacent;
    }

    public boolean trySlideTile(int value) {
        if (value <= 0 || value >= rows * cols)
            return false;
        int[] tilePos = findTile(value);
        int[] blankPos = findBlankTile();
        if (tilePos == null || blankPos == null)
            return false;
        if (isAdjacent(tilePos[0], tilePos[1], blankPos[0], blankPos[1])) {
            swapTiles(tilePos[0], tilePos[1], blankPos[0], blankPos[1]);
            return true;
        }
        return false;
    }

    private int[] findTile(int value) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].getValue() == value)
                    return new int[] { r, c };
            }
        }
        return null;
    }

    private int[] findBlankTile() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (((Tile) grid[r][c]).isBlank())
                    return new int[] { r, c };
            }
        }
        return null;
    }

    private boolean isAdjacent(int r1, int c1, int r2, int c2) {
        return areAdjacent(r1, c1, r2, c2);
    }

    private void swapTiles(int r1, int c1, int r2, int c2) {
        Tile tmp = grid[r1][c1];
        grid[r1][c1] = grid[r2][c2];
        grid[r2][c2] = tmp;
    }

    @Override
    public boolean isSolved() {
        int expectedValue = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = grid[r][c];
                if (r == rows - 1 && c == cols - 1) {
                    return tile.isBlank();
                }
                if (tile.getValue() != expectedValue++) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        int maxVal = rows * cols - 1;
        int cellWidth = Integer.toString(maxVal).length() + 2;
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append("|");
                Tile tile = grid[r][c];
                if (tile.isBlank()) {
                    sb.append(String.format("%" + cellWidth + "s", " "));
                } else {
                    sb.append(String.format("%" + cellWidth + "d", tile.getValue()));
                }
            }
            sb.append("|\n");

            for (int c = 0; c < cols; c++) {
                sb.append("+");
                for (int i = 0; i < cellWidth; i++) {
                    sb.append("-");
                }
            }
            sb.append("+\n");
        }
        return sb.toString();
    }

}
