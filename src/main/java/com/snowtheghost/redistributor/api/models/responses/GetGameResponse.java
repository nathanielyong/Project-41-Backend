package com.snowtheghost.redistributor.api.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowtheghost.redistributor.database.models.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@AllArgsConstructor
public class GetGameResponse {

    @JsonProperty
    private String gameId;

    @JsonProperty
    private int capacity;

    @JsonProperty
    private List<String> players;
}
