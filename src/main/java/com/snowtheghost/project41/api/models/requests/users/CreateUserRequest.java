package com.snowtheghost.project41.api.models.requests.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotNull
    @JsonProperty
    private String username;

    @NotNull
    @JsonProperty
    private String email;

    @NotNull
    @JsonProperty
    private String password;

    @NotNull
    @JsonProperty
    private String type;
}
