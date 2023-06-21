package com.snowtheghost.redistributor.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfiguration {

    @Value("${stripe.apiKey}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
