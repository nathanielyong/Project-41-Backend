package com.snowtheghost.redistributor.api.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateUserRequest {

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty
    private String password;
}
