package org.example.chessgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.chessgame.repository")
//@EntityScan(basePackages = "org.example.chessgame.model")
@EntityScan("org.example.chessgame.*")

public class ChessGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChessGameApplication.class, args);
    }
}
