package com.snowtheghost.project41.api.models.responses.games;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetGameAnalyticsResponse {

    @JsonProperty
    private int gamesPlayed;

    @JsonProperty
    private List<Game> games;

    public static class Game {

        @JsonProperty
        private int winner;  // Tie: -1

        @JsonProperty
        private List<String> configuration;
    }
}
