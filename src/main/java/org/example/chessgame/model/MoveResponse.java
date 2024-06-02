package org.example.chessgame.model;

public class MoveResponse {
    private String from;
    private String to;
    private boolean valid;
    private String fen;  // Add FEN string

    // Constructor
    public MoveResponse(String from, String to, boolean valid, String fen) {
        this.from = from;
        this.to = to;
        this.valid = valid;
        this.fen = fen;
    }

    // Getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }
}
