package com.snowtheghost.project41.api.models.responses.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {
    @JsonProperty
    private String status;
    
    @JsonProperty
    private String message;

    @JsonProperty
    private String game_type;

    @JsonProperty 
    private int player1_points;

    @JsonProperty 
    private int player2_points;

    @JsonProperty
    private Map<String, Object> gameState;
}
