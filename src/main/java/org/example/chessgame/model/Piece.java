package org.example.chessgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pawn.class, name = "PAWN"),
        @JsonSubTypes.Type(value = Rook.class, name = "ROOK"),
        @JsonSubTypes.Type(value = Knight.class, name = "KNIGHT"),
        @JsonSubTypes.Type(value = Bishop.class, name = "BISHOP"),
        @JsonSubTypes.Type(value = King.class, name = "KING"),
        @JsonSubTypes.Type(value = Queen.class, name = "QUEEN"),
})
public abstract class Piece implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Color color;

    @JsonCreator
    public Piece(@JsonProperty("color") Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Square[][] board);

    public abstract String getSymbol();
}
