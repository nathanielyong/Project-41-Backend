package com.snowtheghost.redistributor.api.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@AllArgsConstructor
public class GetGamesResponse {

    @JsonProperty
    private List<GetGameResponse> games;
}