/**
 * Represents a pawn in the Quoridor game.
 * Each player has one pawn that they must move to the opposite side.
 */
public class Pawn extends Piece {
    private int row;
    private int col;
    private final String playerName;
    private final int targetRow; // The row the pawn needs to reach to win

    /**
     * Constructor for a pawn.
     * @param playerName the name of the player who owns this pawn
     * @param startRow the starting row position
     * @param startCol the starting column position
     * @param targetRow the target row this pawn needs to reach to win
     */
    public Pawn(String playerName, int startRow, int startCol, int targetRow) {
        super(1, playerName); // Pawns have value 1 and are owned by the player
        this.playerName = playerName;
        this.row = startRow;
        this.col = startCol;
        this.targetRow = targetRow;
    }

    /**
     * Get the current row position of this pawn.
     * @return the current row
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the current column position of this pawn.
     * @return the current column
     */
    public int getCol() {
        return col;
    }

    /**
     * Set the position of this pawn.
     * @param row the new row position
     * @param col the new column position
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Get the target row this pawn needs to reach to win.
     * @return the target row
     */
    public int getTargetRow() {
        return targetRow;
    }

    /**
     * Check if this pawn has reached its target row (won the game).
     * @return true if the pawn has won, false otherwise
     */
    public boolean hasWon() {
        return row == targetRow;
    }

    /**
     * Get the player name who owns this pawn.
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean canMoveTo(Piece target) {
        // Pawns can move to empty tiles
        return target == null || target.isEmpty();
    }

    @Override
    public String getDisplayString() {
        // Show first letter of player name
        return playerName.substring(0, 1).toUpperCase();
    }

    @Override
    public String toString() {
        return playerName + " pawn at (" + row + "," + col + ")";
    }
}
