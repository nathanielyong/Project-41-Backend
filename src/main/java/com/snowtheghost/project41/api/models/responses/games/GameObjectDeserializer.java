package com.snowtheghost.project41.api.models.responses.games;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class GameObjectDeserializer extends JsonDeserializer<GameResponse> {
    private final ObjectMapper objectMapper;

    public GameObjectDeserializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public GameResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String jsonString = p.getText();
        return objectMapper.readValue(jsonString, GameResponse.class);
    }
}