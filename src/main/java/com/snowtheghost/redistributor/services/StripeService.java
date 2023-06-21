package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.configuration.StripeConfiguration;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class StripeService {

    @Autowired
    public StripeService(StripeConfiguration stripeConfiguration) {
        Stripe.apiKey = stripeConfiguration.getApiKey();
    }

    public URI createCheckoutSession() throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000?success=true")
                .setCancelUrl("http://localhost:3000?success=false")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(Price.CAD_10.getValue())
                        .build())
                .build();
        Session session = Session.create(params);
        return URI.create(session.getUrl());
    }

    private enum Price {

        CAD_10("price_1NLTw6BudgFCw40UFhsqirjA");

        private final String value;

        Price(String value) {
            this.value = value;
        }

        private String getValue() {
            return value;
        }
    }
}
