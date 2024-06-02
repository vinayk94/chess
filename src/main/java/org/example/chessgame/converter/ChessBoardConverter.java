package org.example.chessgame.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.chessgame.model.ChessBoard;

import java.io.IOException;

@Converter
public class ChessBoardConverter implements AttributeConverter<ChessBoard, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ChessBoard chessBoard) {
        try {
            return objectMapper.writeValueAsString(chessBoard);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting ChessBoard to JSON", e);
        }
    }

    @Override
    public ChessBoard convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, ChessBoard.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to ChessBoard", e);
        }
    }
}
