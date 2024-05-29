package org.example.chessgame.model;

import org.example.chessgame.converter.StringArrayConverter;

import jakarta.persistence.*;
import java.util.Arrays;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player host;

    @ManyToOne
    private Player opponent;

    @Convert(converter = StringArrayConverter.class)
    @Column(length = 2048) // Increase the length to handle larger data
    private String[][] board;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", host=" + host +
                ", opponent=" + opponent +
                ", board=" + Arrays.deepToString(board) +
                '}';
    }
}
