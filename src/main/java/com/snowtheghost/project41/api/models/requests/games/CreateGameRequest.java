package com.snowtheghost.project41.api.models.requests.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.snowtheghost.project41.database.models.Game;
import lombok.Setter;

@Getter
@Setter
public class CreateGameRequest {

    @JsonProperty
    private int capacity;

    @JsonProperty
    private int cost;

    @JsonProperty
    private Game.Type type;
}
