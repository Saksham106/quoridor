import java.util.*;

/**
 * Board implementation for dots and boxes game.
 * Manages edges, boxes, and scoring for two-player gameplay.
 */
public class DotsAndBoxesBoard implements Board {
    private final int rows;
    private final int cols;
    private final Edge[][] horizontalEdges;
    private final Edge[][] verticalEdges;
    private final Box[][] boxes;
    private final List<String> players;
    private final Map<String, Integer> scores;

    public DotsAndBoxesBoard(int rows, int cols, List<String> players) {
        this.rows = rows;
        this.cols = cols;
        this.players = new ArrayList<>(players);
        this.scores = new HashMap<>();

        // Initialize scores
        for (String player : players) {
            scores.put(player, 0);
        }

        // Create edges - horizontal edges are (rows+1) x cols
        horizontalEdges = new Edge[rows + 1][cols];
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j < cols; j++) {
                horizontalEdges[i][j] = new Edge();
            }
        }

        // Vertical edges are rows x (cols+1)
        verticalEdges = new Edge[rows][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols; j++) {
                verticalEdges[i][j] = new Edge();
            }
        }

        // Create boxes - rows x cols
        boxes = new Box[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boxes[i][j] = new Box();
            }
        }
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public boolean isSolved() {
        // Game is solved when all edges are claimed
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!horizontalEdges[i][j].isClaimed()) {
                    return false;
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols; j++) {
                if (!verticalEdges[i][j].isClaimed()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Piece getPieceAt(int row, int col) {
        // This method is for compatibility with Board interface
        // In Dots and Boxes, we work with edges and boxes separately
        if (row < rows && col < cols) {
            return boxes[row][col];
        }
        return null;
    }

    @Override
    public void setPieceAt(int row, int col, Piece piece) {
        // This method is for compatibility with Board interface
        // In Dots and Boxes, we work with edges and boxes separately
        if (row < rows && col < cols && piece instanceof Box) {
            boxes[row][col] = (Box) piece;
        }
    }

    public boolean claimEdge(char type, int row, int col, String playerName) {
        Edge edge = null;

        if (type == 'H' || type == 'h') {
            // Horizontal edge
            if (row >= 0 && row <= rows && col >= 0 && col < cols) {
                edge = horizontalEdges[row][col];
            }
        } else if (type == 'V' || type == 'v') {
            // Vertical edge
            if (row >= 0 && row < rows && col >= 0 && col <= cols) {
                edge = verticalEdges[row][col];
            }
        }

        if (edge != null && !edge.isClaimed()) {
            edge.claim(playerName);

            // Check for completed boxes and update score
            int completedBoxes = checkAndCompleteBoxes(type, row, col, playerName);
            if (completedBoxes > 0) {
                scores.put(playerName, scores.get(playerName) + completedBoxes);
            }

            return true;
        }

        return false;
    }

    // Overloaded method that accepts EdgeMove
    public boolean claimEdge(Edge.EdgeMove move, String playerName) {
        if (move == null || !move.isValid()) {
            return false;
        }
        return claimEdge(move.getType().getSymbol(), move.getRow(), move.getCol(), playerName);
    }

    private int checkAndCompleteBoxes(char type, int row, int col, String playerName) {
        int completedBoxes = 0;

        if (type == 'H' || type == 'h') {
            // Check box above (if exists)
            if (row > 0) {
                completedBoxes += checkBox(row - 1, col, playerName);
            }
            // Check box below (if exists)
            if (row < rows) {
                completedBoxes += checkBox(row, col, playerName);
            }
        } else if (type == 'V' || type == 'v') {
            // Check box to the left (if exists)
            if (col > 0) {
                completedBoxes += checkBox(row, col - 1, playerName);
            }
            // Check box to the right (if exists)
            if (col < cols) {
                completedBoxes += checkBox(row, col, playerName);
            }
        }

        return completedBoxes;
    }

    private int checkBox(int boxRow, int boxCol, String playerName) {
        if (boxRow < 0 || boxRow >= rows || boxCol < 0 || boxCol >= cols) {
            return 0;
        }

        Box box = boxes[boxRow][boxCol];
        if (box.isCompleted()) {
            return 0; // Already completed
        }

        // Check all 4 edges of the box
        boolean topEdge = horizontalEdges[boxRow][boxCol].isClaimed();
        boolean bottomEdge = horizontalEdges[boxRow + 1][boxCol].isClaimed();
        boolean leftEdge = verticalEdges[boxRow][boxCol].isClaimed();
        boolean rightEdge = verticalEdges[boxRow][boxCol + 1].isClaimed();

        if (topEdge && bottomEdge && leftEdge && rightEdge) {
            box.complete(playerName);
            return 1;
        }

        return 0;
    }

    public Map<String, Integer> getScores() {
        return new HashMap<>(scores);
    }

    public String getWinner() {
        String winner = null;
        int maxScore = -1;

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                winner = entry.getKey();
            } else if (entry.getValue() == maxScore) {
                winner = null; // Tie
            }
        }

        return winner;
    }

    public void displayBoard() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nCurrent Board:\n");

        // Print column numbers
        sb.append("  ");
        for (int j = 0; j <= cols; j++) {
            sb.append(j).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i <= rows; i++) {
            // Print row number
            sb.append(i).append(" ");

            // Print dots and horizontal edges
            for (int j = 0; j < cols; j++) {
                sb.append("·"); // Dot
                if (horizontalEdges[i][j].isClaimed()) {
                    sb.append("-");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("·\n"); // Last dot

            // Print vertical edges and boxes (except for last row)
            if (i < rows) {
                sb.append("  ");
                for (int j = 0; j <= cols; j++) {
                    if (j < cols) {
                        if (verticalEdges[i][j].isClaimed()) {
                            sb.append("|");
                        } else {
                            sb.append(" ");
                        }
                        sb.append(boxes[i][j].getDisplayString());
                    } else {
                        if (verticalEdges[i][j].isClaimed()) {
                            sb.append("|");
                        } else {
                            sb.append(" ");
                        }
                    }
                }
                sb.append("\n");
            }
        }

        // Print scores
        sb.append("\nScores:\n");
        for (String player : players) {
            sb.append(player).append(": ").append(scores.get(player)).append("\n");
        }

        return sb.toString();
    }
}