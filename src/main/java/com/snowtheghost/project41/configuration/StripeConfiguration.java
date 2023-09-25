package com.snowtheghost.project41.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class StripeConfiguration {

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Value("${stripe.checkoutSuccessEndpointSecret}")
    private String checkoutSuccessEndpointSecret;
}
