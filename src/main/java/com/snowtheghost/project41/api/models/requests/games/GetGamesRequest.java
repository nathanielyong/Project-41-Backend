package com.snowtheghost.project41.api.models.requests.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowtheghost.project41.database.models.Game;
import lombok.Getter;

@Getter
public class GetGamesRequest {

    @JsonProperty
    private Integer capacity;

    @JsonProperty
    private Integer cost;

    @JsonProperty
    private Game.Type type;

    @JsonProperty
    private Game.State state;
}
