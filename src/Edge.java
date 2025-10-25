/**
 * Represents an edge in the dots and boxes game.
 * Edges can be horizontal or vertical and can be claimed by players.
 */
public class Edge extends Piece {
    /**
     * Enumeration of edge types.
     */
    public enum EdgeType {
        HORIZONTAL('H'),
        VERTICAL('V');

        private final char symbol;

        EdgeType(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
        
        // Get edge type from character
        public static EdgeType fromChar(char c) {
            char upperC = Character.toUpperCase(c);
            for (EdgeType type : values()) {
                if (type.symbol == upperC) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * Represents a move to claim an edge.
     */
    public static class EdgeMove {
        private final EdgeType type;
        private final int row;
        private final int col;

        public EdgeMove(EdgeType type, int row, int col) {
            this.type = type;
            this.row = row;
            this.col = col;
        }

        public EdgeMove(char typeChar, int row, int col) {
            this.type = EdgeType.fromChar(typeChar);
            this.row = row;
            this.col = col;
        }

        public EdgeType getType() {
            return type;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public boolean isValid() {
            return type != null && row >= 0 && col >= 0;
        }

        @Override
        public String toString() {
            return type.getSymbol() + " " + row + " " + col;
        }
    }

    private boolean claimed;

    public Edge() {
        super(0, null); // Edge has no value, no initial owner
        this.claimed = false;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void claim(String playerName) {
        this.claimed = true;
        setOwner(playerName);
    }

    @Override
    public boolean isEmpty() {
        return !claimed;
    }

    @Override
    public boolean canMoveTo(Piece target) {
        return !claimed; // Can only claim unclaimed edges
    }

    @Override
    public String getDisplayString() {
        return claimed ? "-" : " ";
    }

    // Static method to parse move input
    public static EdgeMove parseMove(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] parts = input.trim().split("\\s+");
        if (parts.length != 3) {
            return null;
        }

        try {
            char type = parts[0].toUpperCase().charAt(0);
            EdgeType edgeType = EdgeType.fromChar(type);
            if (edgeType == null) {
                return null;
            }

            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);

            return new EdgeMove(edgeType, row, col);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}