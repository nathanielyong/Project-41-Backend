package com.snowtheghost.project41.api.models.responses.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class CreateGameResponse {

    @JsonProperty
    private String gameId;
}
