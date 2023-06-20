package com.snowtheghost.redistributor.api.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@Setter
@AllArgsConstructor
public class CreateGameResponse {

    @JsonProperty
    private String gameId;
}
