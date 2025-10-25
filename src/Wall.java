/**
 * Represents a wall in the Quoridor game.
 * Walls are 2-space long barriers that can be placed between grid points.
 */
public class Wall extends Piece {
    /**
     * Enumeration of wall orientations.
     */
    public enum Orientation {
        HORIZONTAL('H'),
        VERTICAL('V');

        private final char symbol;

        Orientation(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
        
        // Get orientation from character
        public static Orientation fromChar(char c) {
            char upperC = Character.toUpperCase(c);
            for (Orientation orientation : values()) {
                if (orientation.symbol == upperC) {
                    return orientation;
                }
            }
            return null;
        }
    }

    private final Orientation orientation;
    private final int row;
    private final int col;
    private final String playerName;

    /**
     * Constructor for a wall.
     * @param orientation the orientation of the wall (horizontal or vertical)
     * @param row the row position where the wall is placed
     * @param col the column position where the wall is placed
     * @param playerName the name of the player who placed this wall
     */
    public Wall(Orientation orientation, int row, int col, String playerName) {
        super(2, playerName); // Walls have value 2 and are owned by the player
        this.orientation = orientation;
        this.row = row;
        this.col = col;
        this.playerName = playerName;
    }

    /**
     * Get the orientation of this wall.
     * @return the wall orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Get the row position of this wall.
     * @return the row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column position of this wall.
     * @return the column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Get the player name who placed this wall.
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Check if this wall overlaps with another wall.
     * @param other the other wall to check against
     * @return true if the walls overlap, false otherwise
     */
    public boolean overlapsWith(Wall other) {
        if (this.orientation == other.orientation) {
            // Same orientation - check if they're in the same position
            return this.row == other.row && this.col == other.col;
        } else {
            // Different orientations - check for intersection
            if (this.orientation == Orientation.HORIZONTAL) {
                // This wall is horizontal, other is vertical
                return (this.row == other.row || this.row == other.row + 1) &&
                       (this.col == other.col || this.col + 1 == other.col);
            } else {
                // This wall is vertical, other is horizontal
                return (this.col == other.col || this.col == other.col + 1) &&
                       (this.row == other.row || this.row + 1 == other.row);
            }
        }
    }

    @Override
    public boolean canMoveTo(Piece target) {
        // Walls cannot move
        return false;
    }

    @Override
    public String getDisplayString() {
        // Return the appropriate wall symbol
        return orientation == Orientation.HORIZONTAL ? "-" : "|";
    }

    @Override
    public String toString() {
        return playerName + " " + orientation.name().toLowerCase() + " wall at (" + row + "," + col + ")";
    }
}
