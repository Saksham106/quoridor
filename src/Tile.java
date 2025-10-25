/**
 * Concrete implementation of Piece for sliding puzzle tiles.
 * Supports both numbered tiles and blank tiles with movement validation.
 */
public class Tile extends Piece {
    private final boolean isBlank;  // Whether this tile is the blank space

    public Tile(int value) {
        super(value);
        this.isBlank = (value == 0);
    }

    public boolean isBlank() {
        return isBlank;
    }

    @Override
    public boolean canMoveTo(Piece target) {
        // A tile can move to a position if the target is a blank tile
        return target instanceof Tile && ((Tile) target).isBlank();
    }

    @Override
    public String getDisplayString() {
        if (isBlank) {
            return " ";
        }
        return String.valueOf(value);
    }

    public static Tile createNumberedTile(int number) {
        return new Tile(number);
    }

    public static Tile createBlankTile() {
        return new Tile(0);
    }

    @Override
    public String toString() {
        if (isBlank) {
            return "[ ]";
        }
        return "[" + value + "]";
    }
}