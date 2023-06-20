package com.snowtheghost.redistributor.api.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowtheghost.redistributor.database.models.Game;
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
