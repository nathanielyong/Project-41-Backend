package com.snowtheghost.project41.api.models.responses.games;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetGamePointsResponse {
    @JsonProperty 
    private List<GamePointObject> games;

    private static class GamePointObject {
        @JsonProperty
        private String id;

        @JsonProperty
        private int player1;

        @JsonProperty
        private int player2;
    }
}
