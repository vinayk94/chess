package org.example.chessgame.model;

public class MovementUtil {
    public static boolean isValidStraightMove(int startRow, int startCol, int endRow, int endCol, Color color, Square[][] board) {
        if (startRow != endRow && startCol != endCol) return false;

        int rowDiff = Integer.signum(endRow - startRow);
        int colDiff = Integer.signum(endCol - startCol);

        int currentRow = startRow + rowDiff;
        int currentCol = startCol + colDiff;

        while (currentRow != endRow || currentCol != endCol) {
            if (board[currentRow][currentCol].getPiece() != null) {
                return false;
            }
            currentRow += rowDiff;
            currentCol += colDiff;
        }

        Piece targetPiece = board[endRow][endCol].getPiece();
        return targetPiece == null || targetPiece.getColor() != color;
    }

    public static boolean isValidDiagonalMove(int startRow, int startCol, int endRow, int endCol, Color color, Square[][] board) {
        if (Math.abs(endRow - startRow) != Math.abs(endCol - startCol)) return false;

        int rowDiff = Integer.signum(endRow - startRow);
        int colDiff = Integer.signum(endCol - startCol);

        int currentRow = startRow + rowDiff;
        int currentCol = startCol + colDiff;

        while (currentRow != endRow || currentCol != endCol) {
            if (board[currentRow][currentCol].getPiece() != null) {
                return false;
            }
            currentRow += rowDiff;
            currentCol += colDiff;
        }

        Piece targetPiece = board[endRow][endCol].getPiece();
        return targetPiece == null || targetPiece.getColor() != color;
    }
}

