package org.example.chessgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Square implements Serializable {
    private static final long serialVersionUID = 1L;
    private Color color;
    private Piece piece;

    public Square() {
        // Default constructor required by Jackson for deserialization
    }

    @JsonCreator
    public Square(@JsonProperty("color") Color color) {
        this.color = color;
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public Color getColor() {
        return color;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public String toString() {
        return "Square{" +
                "color=" + color +
                ", piece=" + piece +
                '}';
    }
}