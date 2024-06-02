package org.example.chessgame.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Queen extends Piece {
    private final String symbol;

    @JsonCreator
    public Queen(@JsonProperty("color") Color color, @JsonProperty("symbol") String symbol) {
        super(color);
        this.symbol = symbol;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Square[][] board) {
        return MovementUtil.isValidStraightMove(startRow, startCol, endRow, endCol, color, board) ||
                MovementUtil.isValidDiagonalMove(startRow, startCol, endRow, endCol, color, board);
    }


    @Override
    public String getSymbol() {
        return (color == Color.WHITE) ? "Q" : "q";
    }
}
