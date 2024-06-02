package org.example.chessgame.controller;

import org.example.chessgame.model.Move;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/move")
    @SendTo("/topic/moves")
    public Move sendMove(Move move) {
        return move; // Broadcast the move to all connected clients
    }
}
