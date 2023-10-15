package com.snowtheghost.project41.api.models.responses.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@AllArgsConstructor
public class GameResponse {
    @JsonProperty
    private String status;
    
    @JsonProperty
    private String message;

    @JsonProperty
    private Map<String, Object> gameState;
}
