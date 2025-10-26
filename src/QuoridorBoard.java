import java.util.*;

public class QuoridorBoard implements Board {
    public static final int BOARD_SIZE = 9;
    public static final int MAX_WALLS_PER_PLAYER = 10;

    private final Tile[][] grid;
    private final Pawn[][] pawnPositions;
    private final List<Wall> placedWalls;
    private final Map<String, Integer> wallCounts;
    private final List<String> playerNames;

    private final boolean[][] horizontalWalls;
    private final boolean[][] verticalWalls;

    public QuoridorBoard(List<String> playerNames) {
        if (playerNames == null || playerNames.size() != 2) {
            throw new IllegalArgumentException("Quoridor requires exactly 2 players");
        }

        this.playerNames = new ArrayList<>(playerNames);
        this.grid = new Tile[BOARD_SIZE][BOARD_SIZE];
        this.pawnPositions = new Pawn[BOARD_SIZE][BOARD_SIZE];
        this.placedWalls = new ArrayList<>();
        this.wallCounts = new HashMap<>();

        this.horizontalWalls = new boolean[BOARD_SIZE - 1][BOARD_SIZE];
        this.verticalWalls = new boolean[BOARD_SIZE][BOARD_SIZE - 1];

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                grid[r][c] = new Tile();
            }
        }

        for (String playerName : playerNames) {
            wallCounts.put(playerName, MAX_WALLS_PER_PLAYER);
        }

        initializePawns();
    }

    private void initializePawns() {
        Pawn pawn1 = new Pawn(playerNames.get(0), 8, 4, 0);
        grid[8][4].setPiece(pawn1);
        pawnPositions[8][4] = pawn1;

        Pawn pawn2 = new Pawn(playerNames.get(1), 0, 4, 8);
        grid[0][4].setPiece(pawn2);
        pawnPositions[0][4] = pawn2;
    }

    @Override
    public int rows() {
        return BOARD_SIZE;
    }

    @Override
    public int cols() {
        return BOARD_SIZE;
    }

    @Override
    public Piece getPieceAt(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        return grid[row][col].getPiece();
    }

    @Override
    public void setPieceAt(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            grid[row][col].setPiece(piece);
        }
    }

    @Override
    public boolean isSolved() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.hasWon()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Pawn getPawnForPlayer(String playerName) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.getPlayerName().equals(playerName)) {
                        return pawn;
                    }
                }
            }
        }
        return null;
    }

    public String getWinner() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.hasWon()) {
                        return pawn.getPlayerName();
                    }
                }
            }
        }
        return null;
    }

    public int getWallCount(String playerName) {
        return wallCounts.getOrDefault(playerName, 0);
    }

    public Pawn[][] getPawnPositions() {
        return pawnPositions;
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public List<Wall> getPlacedWalls() {
        return new ArrayList<>(placedWalls);
    }

    public boolean movePawn(String playerName, String direction) {
        Pawn pawn = getPawnForPlayer(playerName);
        if (pawn == null) {
            return false;
        }

        int currentRow = pawn.getRow();
        int currentCol = pawn.getCol();
        int newRow = currentRow;
        int newCol = currentCol;

        switch (direction.toLowerCase()) {
            case "up":
                newRow--;
                break;
            case "down":
                newRow++;
                break;
            case "left":
                newCol--;
                break;
            case "right":
                newCol++;
                break;
            default:
                return false;
        }

        if (!MoveValidator.canMovePawn(currentRow, currentCol, newRow, newCol,
                pawnPositions, horizontalWalls, verticalWalls)) {
            return false;
        }

        // Update pawn position
        pawnPositions[currentRow][currentCol] = null;
        pawnPositions[newRow][newCol] = pawn;
        pawn.setPosition(newRow, newCol);

        // Update grid
        grid[currentRow][currentCol].setPiece(null);
        grid[newRow][newCol].setPiece(pawn);

        return true;
    }

    public boolean movePawnTwoSteps(String playerName, String direction) {
        Pawn pawn = getPawnForPlayer(playerName);
        if (pawn == null) {
            return false;
        }

        int currentRow = pawn.getRow();
        int currentCol = pawn.getCol();
        int newRow = currentRow;
        int newCol = currentCol;

        switch (direction.toLowerCase()) {
            case "up":
                newRow -= 2;
                break;
            case "down":
                newRow += 2;
                break;
            case "left":
                newCol -= 2;
                break;
            case "right":
                newCol += 2;
                break;
            default:
                return false;
        }

        // Update pawn position
        pawnPositions[currentRow][currentCol] = null;
        pawnPositions[newRow][newCol] = pawn;
        pawn.setPosition(newRow, newCol);

        // Update grid
        grid[currentRow][currentCol].setPiece(null);
        grid[newRow][newCol].setPiece(pawn);

        return true;
    }

    public boolean placeWall(Wall wall) {
        if (wall == null) {
            return false;
        }

        String playerName = wall.getPlayerName();

        if (getWallCount(playerName) <= 0) {
            return false;
        }

        if (!isValidWallPlacement(wall)) {
            return false;
        }

        temporarilyPlaceWall(wall);

        boolean bothPlayersCanWin = canBothPlayersReachGoal();

        temporarilyRemoveWall(wall);

        if (bothPlayersCanWin) {
            permanentlyPlaceWall(wall);
            placedWalls.add(wall);
            wallCounts.put(playerName, wallCounts.get(playerName) - 1);
            return true;
        }

        return false;
    }

    private boolean isValidWallPlacement(Wall wall) {
        int row = wall.getRow();
        int col = wall.getCol();
        Wall.Orientation orientation = wall.getOrientation();

        if (orientation == Wall.Orientation.HORIZONTAL) {
            if (row < 0 || row >= BOARD_SIZE - 1 || col < 0 || col >= BOARD_SIZE - 1) {
                return false;
            }
            // Check both segments of the horizontal wall
            if (horizontalWalls[row][col] || (col + 1 < BOARD_SIZE && horizontalWalls[row][col + 1])) {
                return false;
            }
        } else {
            if (row < 0 || row >= BOARD_SIZE - 1 || col < 0 || col >= BOARD_SIZE - 1) {
                return false;
            }
            // Check both segments of the vertical wall
            if (verticalWalls[row][col] || (row + 1 < BOARD_SIZE && verticalWalls[row + 1][col])) {
                return false;
            }
        }

        for (Wall existingWall : placedWalls) {
            if (wall.overlapsWith(existingWall)) {
                return false;
            }
        }

        return true;
    }

    private void temporarilyPlaceWall(Wall wall) {
        if (wall.getOrientation() == Wall.Orientation.HORIZONTAL) {
            // Horizontal wall blocks TWO vertical edges: at [row][col] and [row][col+1]
            horizontalWalls[wall.getRow()][wall.getCol()] = true;
            if (wall.getCol() + 1 < BOARD_SIZE) {
                horizontalWalls[wall.getRow()][wall.getCol() + 1] = true;
            }
        } else {
            // Vertical wall blocks TWO horizontal edges: at [row][col] and [row+1][col]
            verticalWalls[wall.getRow()][wall.getCol()] = true;
            if (wall.getRow() + 1 < BOARD_SIZE) {
                verticalWalls[wall.getRow() + 1][wall.getCol()] = true;
            }
        }
    }

    private void temporarilyRemoveWall(Wall wall) {
        if (wall.getOrientation() == Wall.Orientation.HORIZONTAL) {
            horizontalWalls[wall.getRow()][wall.getCol()] = false;
            if (wall.getCol() + 1 < BOARD_SIZE) {
                horizontalWalls[wall.getRow()][wall.getCol() + 1] = false;
            }
        } else {
            verticalWalls[wall.getRow()][wall.getCol()] = false;
            if (wall.getRow() + 1 < BOARD_SIZE) {
                verticalWalls[wall.getRow() + 1][wall.getCol()] = false;
            }
        }
    }

    private void permanentlyPlaceWall(Wall wall) {
        if (wall.getOrientation() == Wall.Orientation.HORIZONTAL) {
            horizontalWalls[wall.getRow()][wall.getCol()] = true;
            if (wall.getCol() + 1 < BOARD_SIZE) {
                horizontalWalls[wall.getRow()][wall.getCol() + 1] = true;
            }
        } else {
            verticalWalls[wall.getRow()][wall.getCol()] = true;
            if (wall.getRow() + 1 < BOARD_SIZE) {
                verticalWalls[wall.getRow() + 1][wall.getCol()] = true;
            }
        }
    }

    private boolean canBothPlayersReachGoal() {
        for (String playerName : playerNames) {
            Pawn pawn = getPawnForPlayer(playerName);
            if (pawn == null || !hasPathToGoal(pawn)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPathToGoal(Pawn pawn) {
        if (pawn == null) {
            return false;
        }

        int startRow = pawn.getRow();
        int startCol = pawn.getCol();
        int targetRow = pawn.getTargetRow();

        if (startRow == targetRow) {
            return true;
        }

        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];

        queue.offer(new int[] { startRow, startCol });
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            if (row == targetRow) {
                return true;
            }

            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (!isValidPosition(newRow, newCol) || visited[newRow][newCol]) {
                    continue;
                }

                if (isBlockedByWall(row, col, newRow, newCol)) {
                    continue;
                }

                visited[newRow][newCol] = true;
                queue.offer(new int[] { newRow, newCol });
            }
        }

        return false;
    }

    private boolean isBlockedByWall(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow == toRow) {
            int minCol = Math.min(fromCol, toCol);
            if (minCol >= 0 && minCol < BOARD_SIZE - 1) {
                return verticalWalls[fromRow][minCol];
            }
        } else if (fromCol == toCol) {
            int minRow = Math.min(fromRow, toRow);
            if (minRow >= 0 && minRow < BOARD_SIZE - 1) {
                return horizontalWalls[minRow][fromCol];
            }
        }

        return false;
    }

    /**
     * Color a wall segment based on its owner.
     */
    private String colorWall(String symbol, int row, int col, boolean isVertical) {
        // Find which wall owns this segment
        for (Wall wall : placedWalls) {
            if (isVertical && wall.getOrientation() == Wall.Orientation.VERTICAL) {
                int wallRow = wall.getRow();
                int wallCol = wall.getCol();
                // Vertical wall at (wallRow, wallCol) covers rows wallRow and wallRow+1
                if (wallCol == col && (row == wallRow || row == wallRow + 1)) {
                    if (wall.getPlayerName().equals(playerNames.get(0))) {
                        return Colors.player1(symbol);
                    } else {
                        return Colors.player2(symbol);
                    }
                }
            } else if (!isVertical && wall.getOrientation() == Wall.Orientation.HORIZONTAL) {
                int wallRow = wall.getRow();
                int wallCol = wall.getCol();
                // Horizontal wall at (wallRow, wallCol) covers columns wallCol and wallCol+1
                if (wallRow == row && (col == wallCol || col == wallCol + 1)) {
                    if (wall.getPlayerName().equals(playerNames.get(0))) {
                        return Colors.player1(symbol);
                    } else {
                        return Colors.player2(symbol);
                    }
                }
            }
        }
        return symbol; // Shouldn't happen
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nQuoridor Board:\n");

        sb.append("   ");
        for (int c = 0; c < BOARD_SIZE; c++) {
            sb.append(" ").append(c);
        }
        sb.append("\n");

        for (int r = 0; r < BOARD_SIZE; r++) {
            sb.append(String.format("%2d ", r));

            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece piece = grid[r][c].getPiece();
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    String display = piece.getDisplayString();
                    // Color player 1 blue, player 2 red
                    if (pawn.getPlayerName().equals(playerNames.get(0))) {
                        sb.append(Colors.player1(display));
                    } else {
                        sb.append(Colors.player2(display));
                    }
                } else {
                    sb.append("·");
                }

                if (c < BOARD_SIZE - 1) {
                    if (verticalWalls[r][c]) {
                        // Color walls based on owner
                        String wallDisplay = colorWall("|", r, c, true);
                        sb.append(wallDisplay);
                    } else {
                        sb.append(" ");
                    }
                }
            }
            sb.append("\n");

            if (r < BOARD_SIZE - 1) {
                sb.append("   ");
                for (int c = 0; c < BOARD_SIZE; c++) {
                    if (horizontalWalls[r][c]) {
                        // Color walls based on owner
                        String wallDisplay = colorWall("-", r, c, false);
                        sb.append(wallDisplay);
                    } else {
                        sb.append(" ");
                    }
                    if (c < BOARD_SIZE - 1) {

                        boolean currentHasWall = horizontalWalls[r][c];
                        boolean nextHasWall = horizontalWalls[r][c + 1];

                        boolean sameWall = false;
                        if (currentHasWall && nextHasWall) {
                            for (Wall wall : placedWalls) {
                                if (wall.getOrientation() == Wall.Orientation.HORIZONTAL &&
                                        wall.getRow() == r) {
                                    int wallCol = wall.getCol();
                                    if (wallCol == c || (wallCol == c - 1) || (wallCol == c + 1)) {
                                        if ((c >= wallCol && c <= wallCol + 1) &&
                                                (c + 1 >= wallCol && c + 1 <= wallCol + 1)) {
                                            sameWall = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (sameWall) {
                            // Use colored dash for connection
                            String wallDisplay = colorWall("-", r, c, false);
                            sb.append(wallDisplay);
                        } else {
                            sb.append(" ");
                        }
                    }
                }
                sb.append("\n");
            }
        }

        sb.append("\nWalls Remaining:\n");
        for (String playerName : playerNames) {
            sb.append(playerName).append(": ").append(wallCounts.get(playerName)).append("\n");
        }

        return sb.toString();
    }
}