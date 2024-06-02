package org.example.chessgame.model;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "player", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Player {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Color color;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
