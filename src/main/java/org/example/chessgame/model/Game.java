package org.example.chessgame.model;

import org.example.chessgame.converter.ChessBoardConverter;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@NamedQueries({
        @NamedQuery(name = "Game.findByHost_Name", query = "SELECT g FROM Game g WHERE g.host.name = :hostName")
})
public class Game {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private Player host;

    @Convert(converter = ChessBoardConverter.class)
    @Column(name = "board", columnDefinition = "LONGBLOB")
    private ChessBoard board;

    @ManyToOne
    private Player opponent;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }
}
