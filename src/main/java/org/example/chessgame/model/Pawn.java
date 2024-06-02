package org.example.chessgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pawn extends Piece {
    private boolean enPassantEligible = false;
    private final String symbol;

    @JsonCreator
    public Pawn(@JsonProperty("color") Color color, @JsonProperty("symbol") String symbol) {
        super(color);
        this.symbol = symbol;
    }

    public boolean isEnPassantEligible() {
        return enPassantEligible;
    }

    public void setEnPassantEligible(boolean enPassantEligible) {
        this.enPassantEligible = enPassantEligible;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Square[][] board) {
        int direction = (color == Color.WHITE) ? -1 : 1;

        // Regular move
        if (startCol == endCol) {
            if (endRow == startRow + direction && board[endRow][endCol].getPiece() == null) {
                return true;
            }
            if (endRow == startRow + 2 * direction && startRow == (color == Color.WHITE ? 6 : 1) && board[endRow][endCol].getPiece() == null) {
                return true;
            }
        } else if (Math.abs(startCol - endCol) == 1 && endRow == startRow + direction) {
            // Regular diagonal capture
            Piece targetPiece = board[endRow][endCol].getPiece();
            if (targetPiece != null && targetPiece.getColor() != this.color) {
                return true;
            }

            // En passant capture
            Piece adjacentPiece = board[startRow][endCol].getPiece();
            if (adjacentPiece instanceof Pawn && adjacentPiece.getColor() != this.color && ((Pawn) adjacentPiece).isEnPassantEligible()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return (color == Color.WHITE) ? "P" : "p";
    }
}
