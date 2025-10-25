import java.util.*;

/**
 * Move validation for Quoridor.
 */
public class MoveValidator {
    public static final int BOARD_SIZE = 9;
    
    public static boolean canMovePawn(int fromRow, int fromCol, int toRow, int toCol,
                                    Pawn[][] pawns, boolean[][] horizontalWalls, 
                                    boolean[][] verticalWalls) {
        // Check if positions are valid
        if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol)) {
            return false;
        }
        
        // Check if starting position has a pawn
        if (pawns[fromRow][fromCol] == null) {
            return false;
        }
        
        // Check if target position is empty
        if (pawns[toRow][toCol] != null) {
            return false;
        }
        
        // Check if it's an orthogonal move (one step in any direction)
        if (isOrthogonalMove(fromRow, fromCol, toRow, toCol)) {
            return !isBlockedByWall(fromRow, fromCol, toRow, toCol, horizontalWalls, verticalWalls);
        }
        
        // Check if it's a jump move (over an adjacent pawn)
        if (isJumpMove(fromRow, fromCol, toRow, toCol, pawns)) {
            return !isBlockedByWall(fromRow, fromCol, toRow, toCol, horizontalWalls, verticalWalls);
        }
        
        // Check if it's a diagonal move (when jump is blocked by wall)
        if (isDiagonalMove(fromRow, fromCol, toRow, toCol, pawns, horizontalWalls, verticalWalls)) {
            return true;
        }
        
        return false;
    }
    
    private static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    private static boolean isOrthogonalMove(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }
    private static boolean isJumpMove(int fromRow, int fromCol, int toRow, int toCol, Pawn[][] pawns) {
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;
        
        // Check if it's a 2-step move in the same direction
        if ((Math.abs(rowDiff) == 2 && colDiff == 0) || (rowDiff == 0 && Math.abs(colDiff) == 2)) {
            // Check if there's a pawn in the middle position
            int middleRow = fromRow + rowDiff / 2;
            int middleCol = fromCol + colDiff / 2;
            
            if (isValidPosition(middleRow, middleCol) && pawns[middleRow][middleCol] != null) {
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean isDiagonalMove(int fromRow, int fromCol, int toRow, int toCol,
                                        Pawn[][] pawns, boolean[][] horizontalWalls,
                                        boolean[][] verticalWalls) {
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;
        
        // Check if it's a diagonal move (1 step in each direction)
        if (Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 1) {
            // Check if there's a pawn adjacent in one direction
            boolean hasPawnUp = isValidPosition(fromRow - 1, fromCol) && pawns[fromRow - 1][fromCol] != null;
            boolean hasPawnDown = isValidPosition(fromRow + 1, fromCol) && pawns[fromRow + 1][fromCol] != null;
            boolean hasPawnLeft = isValidPosition(fromRow, fromCol - 1) && pawns[fromRow][fromCol - 1] != null;
            boolean hasPawnRight = isValidPosition(fromRow, fromCol + 1) && pawns[fromRow][fromCol + 1] != null;
            
            // Check if the jump in that direction is blocked by a wall
            boolean jumpBlockedUp = hasPawnUp && isBlockedByWall(fromRow, fromCol, fromRow - 2, fromCol, horizontalWalls, verticalWalls);
            boolean jumpBlockedDown = hasPawnDown && isBlockedByWall(fromRow, fromCol, fromRow + 2, fromCol, horizontalWalls, verticalWalls);
            boolean jumpBlockedLeft = hasPawnLeft && isBlockedByWall(fromRow, fromCol, fromRow, fromCol - 2, horizontalWalls, verticalWalls);
            boolean jumpBlockedRight = hasPawnRight && isBlockedByWall(fromRow, fromCol, fromRow, fromCol + 2, horizontalWalls, verticalWalls);
            
            // Check if the diagonal move is valid based on the blocked jump
            if (rowDiff == -1 && colDiff == -1) { // Up-left diagonal
                return (hasPawnUp && jumpBlockedUp) || (hasPawnLeft && jumpBlockedLeft);
            } else if (rowDiff == -1 && colDiff == 1) { // Up-right diagonal
                return (hasPawnUp && jumpBlockedUp) || (hasPawnRight && jumpBlockedRight);
            } else if (rowDiff == 1 && colDiff == -1) { // Down-left diagonal
                return (hasPawnDown && jumpBlockedDown) || (hasPawnLeft && jumpBlockedLeft);
            } else if (rowDiff == 1 && colDiff == 1) { // Down-right diagonal
                return (hasPawnDown && jumpBlockedDown) || (hasPawnRight && jumpBlockedRight);
            }
        }
        
        return false;
    }
    
    private static boolean isBlockedByWall(int fromRow, int fromCol, int toRow, int toCol,
                                         boolean[][] horizontalWalls, boolean[][] verticalWalls) {
        if (fromRow == toRow) {
            // Horizontal movement - check for vertical walls
            int col = Math.min(fromCol, toCol);
            return verticalWalls[fromRow][col + 1];
        } else if (fromCol == toCol) {
            // Vertical movement - check for horizontal walls
            int row = Math.min(fromRow, toRow);
            return horizontalWalls[row + 1][fromCol];
        }
        
        return false;
    }
    
    public static List<int[]> getValidMoves(int row, int col, Pawn[][] pawns,
                                          boolean[][] horizontalWalls, boolean[][] verticalWalls) {
        List<int[]> validMoves = new ArrayList<>();
        
        // Check all 8 directions (4 orthogonal + 4 diagonal)
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // Orthogonal
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // Diagonal
        };
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            if (canMovePawn(row, col, newRow, newCol, pawns, horizontalWalls, verticalWalls)) {
                validMoves.add(new int[]{newRow, newCol});
            }
        }
        
        return validMoves;
    }
    
    public static boolean hasValidMoves(int row, int col, Pawn[][] pawns,
                                      boolean[][] horizontalWalls, boolean[][] verticalWalls) {
        return !getValidMoves(row, col, pawns, horizontalWalls, verticalWalls).isEmpty();
    }
}
