package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.configuration.StripeConfiguration;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private final String checkoutSuccessEndpointSecret;

    @Autowired
    public StripeService(StripeConfiguration stripeConfiguration) {
        Stripe.apiKey = stripeConfiguration.getApiKey();
        this.checkoutSuccessEndpointSecret = stripeConfiguration.getCheckoutSuccessEndpointSecret();
    }

    public void validateCheckoutSuccess(String payload, String signature) throws SignatureVerificationException {
        Webhook.constructEvent(payload, signature, checkoutSuccessEndpointSecret);
    }

    public String createCheckoutSession(String userId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/funds?success=true")
                .setCancelUrl("http://localhost:3000/funds?success=false")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(Price.CAD_10.getValue())
                        .build())
                .setClientReferenceId(userId)
                .build();
        Session session = Session.create(params);
        return session.getUrl();
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
