package org.example.chessgame.model;

import java.util.UUID;

public class MoveRequest {
    private UUID gameId;
    private String from;
    private String to;

    // Getters and setters
    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

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

    public Move getMove() {
        Move move = new Move();
        move.setFrom(this.from);
        move.setTo(this.to);
        return move;
    }
}
