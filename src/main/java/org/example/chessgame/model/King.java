package org.example.chessgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class King extends Piece {
    private final String symbol;

    @JsonCreator
    public King(@JsonProperty("color") Color color, @JsonProperty("symbol") String symbol) {
        super(color);
        this.symbol = symbol;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Square[][] board) {
        if (Math.abs(endCol - startCol) == 2 && startRow == endRow) {
            // Castling logic
            int row = startRow;
            int colDirection = (endCol - startCol) / 2; // Direction of castling
            int rookCol = (colDirection > 0) ? 7 : 0; // Rook position depending on castling direction

            // Check if squares between king and rook are empty
            for (int col = startCol + colDirection; col != endCol; col += colDirection) {
                if (board[row][col].getPiece() != null) {
                    return false;
                }
            }

            // Check rook position and piece type
            Piece rook = board[row][rookCol].getPiece();
            if (rook instanceof Rook && rook.getColor() == this.color) {
                // Additional rules for castling (e.g., neither the king nor the rook has moved, not in check, etc.)
                return true;
            }
        }

        // Regular king move (one square in any direction)
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        return (rowDiff <= 1 && colDiff <= 1);
    }

    @Override
    public String getSymbol() {
        return (color == Color.WHITE) ? "K" : "k";
    }
}
