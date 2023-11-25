package com.snowtheghost.project41.api.models.responses.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowtheghost.project41.api.models.responses.games.GetGameResponse;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserResponse {

    public GetUserResponse(String userId, String username, String email, float balance, String currentGameId) {}

    @JsonProperty
    private String userId;

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty
    private Float balance;

    @JsonProperty
    private String currentGameId;

    @JsonProperty
    private String type;
}
