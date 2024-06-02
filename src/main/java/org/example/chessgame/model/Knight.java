package org.example.chessgame.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Knight extends Piece {
    private final String symbol;

    @JsonCreator
    public Knight(@JsonProperty("color") Color color, @JsonProperty("symbol") String symbol) {
        super(color);
        this.symbol = symbol;
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Square[][] board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }


    @Override
    public String getSymbol() {
        return (color == Color.WHITE) ? "N" : "n";
    }
}
