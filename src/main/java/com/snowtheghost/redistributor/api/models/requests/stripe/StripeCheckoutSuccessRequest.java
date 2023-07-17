package com.snowtheghost.redistributor.api.models.requests.stripe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StripeCheckoutSuccessRequest {

    @JsonProperty
    private String id;

    @JsonProperty
    private Data data;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        @JsonProperty
        private Object object;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Object {

            @JsonProperty("client_reference_id")
            private String userId;

            @JsonProperty("amount_total")
            private Integer amount;

            @JsonProperty
            private String currency;
        }
    }

    public String getUserId() {
        return data.object.userId;
    }

    public int getAmount() {
        return data.object.amount;
    }

    public String getCurrency() {
        return data.object.currency;
    }
}
