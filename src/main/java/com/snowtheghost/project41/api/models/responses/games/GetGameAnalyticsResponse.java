package com.snowtheghost.project41.api.models.responses.games;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetGameAnalyticsResponse {
    @JsonProperty
    private List<Game> games;

    public int getNumPlayed() {
        return games == null ? 0 : games.size();
    }

    public static class Game {
        @JsonProperty
        private String id;

        @JsonProperty 
        private GameResponse game_object;
    }
}
