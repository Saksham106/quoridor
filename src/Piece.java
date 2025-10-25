/**
 * Base class for game pieces.
 * Provides common functionality for value, ownership, and movement validation.
 */
public class Piece {
    protected final int value;
    protected String owner;
    protected boolean isEmpty;

    /**
     * Constructor for pieces without owner.
     */
    protected Piece(int value) {
        this.value = value;
        this.owner = null;
        this.isEmpty = (value == 0);
    }

    /**
     * Constructor for pieces with owner.
     */
    protected Piece(int value, String owner) {
        this.value = value;
        this.owner = owner;
        this.isEmpty = (value == 0);
    }

    public int getValue() {
        return value;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean canMoveTo(Piece target) {
        // Default implementation - can move to empty pieces
        return target != null && target.isEmpty();
    }

    public String getDisplayString() {
        // Default implementation - show value or space for empty
        if (isEmpty) {
            return " ";
        }
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Piece piece = (Piece) obj;
        return value == piece.value &&
                isEmpty == piece.isEmpty &&
                (owner != null ? owner.equals(piece.owner) : piece.owner == null);
    }

    @Override
    public int hashCode() {
        int result = value;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (isEmpty ? 1 : 0);
        return result;
    }
}