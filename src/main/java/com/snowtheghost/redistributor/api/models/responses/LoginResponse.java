package com.snowtheghost.redistributor.api.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class LoginResponse {

    @JsonProperty
    private String token;
}
