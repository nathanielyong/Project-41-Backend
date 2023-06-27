package com.snowtheghost.redistributor.api.models.responses.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowtheghost.redistributor.api.models.responses.games.GetGameResponse;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@AllArgsConstructor
public class GetUserResponse {

    @JsonProperty
    private String userId;

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty
    private float balance;

    @JsonProperty
    private List<GetGameResponse> games;
}
