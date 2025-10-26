/**
 * Represents a pawn in Quoridor.
 * Tracks position and checks if player has won by reaching opposite side.
 */
public class Pawn extends Piece {
    private int row;
    private int col;
    private final String playerName;
    private final int targetRow; // Row to reach to win

    public Pawn(String playerName, int startRow, int startCol, int targetRow) {
        super(1, playerName);
        this.playerName = playerName;
        this.row = startRow;
        this.col = startCol;
        this.targetRow = targetRow;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getTargetRow() {
        return targetRow;
    }

    // Check if pawn reached the target row (won the game)
    public boolean hasWon() {
        return row == targetRow;
    }

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
