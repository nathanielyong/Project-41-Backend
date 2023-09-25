package com.snowtheghost.project41.api.models.responses.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class CreateUserResponse {

    @JsonProperty
    private String token;

    @JsonProperty
    private String connectedAccountLinkUrl;
}
