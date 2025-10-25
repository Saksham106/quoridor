/**
 * Represents a tile on the game board.
 * A tile can contain a piece or be empty.
 * This is the board space, not the game piece itself.
 */
public class Tile {
    private Piece piece;  // The piece currently on this tile (null if empty)

    public Tile() {
        this.piece = null;
    }

    public Tile(Piece piece) {
        this.piece = piece;
    }

    /**
     * Get the piece currently on this tile.
     * @return the piece on this tile, or null if empty
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Set the piece on this tile.
     * @param piece the piece to place on this tile (null to make empty)
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Check if this tile is empty (no piece).
     * @return true if empty, false if occupied
     */
    public boolean isEmpty() {
        return piece == null;
    }

    /**
     * Check if this tile contains a blank piece.
     * @return true if contains a blank piece (value 0), false otherwise
     */
    public boolean isBlank() {
        return piece != null && piece.getValue() == 0;
    }

    /**
     * Get the display string for this tile.
     * @return the display string of the piece, or " " if empty
     */
    public String getDisplayString() {
        if (piece == null) {
            return " ";
        }
        return piece.getDisplayString();
    }

    /**
     * Get the value of the piece on this tile.
     * @return the value of the piece, or 0 if empty
     */
    public int getValue() {
        if (piece == null) {
            return 0;
        }
        return piece.getValue();
    }

    @Override
    public String toString() {
        if (piece == null) {
            return "[ ]";
        }
        return piece.toString();
    }
}