/**
 * Pawn class for Quoridor game.
 */
public class Pawn extends Piece {
    private int row;
    private int col;
    private final String playerName;
    private final int targetRow; // The row the pawn needs to reach to win

    public Pawn(String playerName, int startRow, int startCol, int targetRow) {
        super(1, playerName); // Pawns have value 1 and are owned by the player
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
