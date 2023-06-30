package com.snowtheghost.redistributor.api.models.responses.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowtheghost.redistributor.api.models.responses.games.GetGameResponse;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserResponse {

    public GetUserResponse(String userId, String username, String email, float balance, String connectedAccountId, String games) {}

    @JsonProperty
    private String userId;

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty
    private Float balance;

    @JsonProperty
    private String connectedAccountId;

    @JsonProperty
    private Boolean chargesEnabled;

    @JsonProperty
    private List<GetGameResponse> games;
}
