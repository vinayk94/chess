package org.example.chessgame;

import org.example.chessgame.model.Game;
import org.example.chessgame.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChessGameApplicationTests {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void contextLoads() {
        Game game = new Game();
        game = gameRepository.save(game);
        assertThat(game.getId()).isNotNull();
    }
}
