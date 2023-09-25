package com.snowtheghost.project41.api.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WithdrawalRequest {

    @JsonProperty
    private int amount;
}
