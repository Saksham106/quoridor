/**
 * Generic interface for game boards.
 * Defines common operations for board-based games including dimensions,
 * piece management, and adjacency checking.
 */
public interface Board {
    int rows();
    int cols();
    boolean isSolved();

    Piece getPieceAt(int row, int col);

    void setPieceAt(int row, int col, Piece piece);

    default boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows() && col >= 0 && col < cols();
    }

    default boolean areAdjacent(int row1, int col1, int row2, int col2) {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2) == 1;
    }
    /**
     * Get all valid adjacent positions to the given position.
     * takes in row and col and returns all valid adjacent positions
     */
    default int[][] getAdjacentPositions(int row, int col) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        java.util.List<int[]> adjacent = new java.util.ArrayList<>();

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (isValidPosition(newRow, newCol)) {
                adjacent.add(new int[] { newRow, newCol });
            }
        }

        return adjacent.toArray(new int[adjacent.size()][]);
    }
}
