/**
 * Represents a box in the dots and boxes game.
 * A box is completed when all four surrounding edges are claimed.
 */
public class Box extends Piece {
    private boolean completed;  // Whether all four edges are claimed

    public Box() {
        super(1, null); // Box has value 1 when completed
        this.completed = false;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete(String playerName) {
        this.completed = true;
        setOwner(playerName);
    }

    @Override
    public boolean isEmpty() {
        return !completed;
    }

    @Override
    public boolean canMoveTo(Piece target) {
        return false; // Boxes cannot be moved
    }

    @Override
    public String getDisplayString() {
        if (!completed) {
            return " ";
        }
        // Show first letter of player name
        return getOwner() != null ? getOwner().substring(0, 1) : "?";
    }
}