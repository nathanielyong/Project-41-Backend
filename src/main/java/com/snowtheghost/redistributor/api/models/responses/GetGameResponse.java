package com.snowtheghost.redistributor.api.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowtheghost.redistributor.database.models.Game;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetGameResponse {

    @JsonProperty
    private String gameId;

    @JsonProperty
    private int capacity;

    @JsonProperty
    private int cost;

    @JsonProperty
    private Game.Type type;

    @JsonProperty
    private Game.State state;

    @JsonProperty
    private List<String> players;

    @JsonProperty
    private Map<String, Integer> winners;
}
