package com.snowtheghost.project41.api.models.responses.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class CreateCheckoutSessionResponse {

    @JsonProperty
    private String redirectUrl;
}
