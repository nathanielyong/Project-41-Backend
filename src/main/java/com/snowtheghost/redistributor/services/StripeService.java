package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.configuration.StripeConfiguration;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Transfer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    private final double SERVICE_FEE = 0.96;
    private final String checkoutSuccessEndpointSecret;

    @Autowired
    public StripeService(StripeConfiguration stripeConfiguration) {
        Stripe.apiKey = stripeConfiguration.getApiKey();
        this.checkoutSuccessEndpointSecret = stripeConfiguration.getCheckoutSuccessEndpointSecret();
    }

    public void withdraw(String connectedAccountId, int amount) throws StripeException {
        amount = (int) (amount * SERVICE_FEE);
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "cad");
        params.put("destination", connectedAccountId);
        Transfer.create(params);
    }

    public boolean isChargesEnabled(String connectedAccountId) throws StripeException {
        Account account = Account.retrieve(connectedAccountId);
        return account.getChargesEnabled();
    }

    public String createConnectedAccount(String email) throws StripeException {
        AccountCreateParams params = AccountCreateParams.builder()
                .setCountry("CA")
                .setType(AccountCreateParams.Type.EXPRESS)
                .setCapabilities(AccountCreateParams.Capabilities.builder()
                        .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build())
                        .setTransfers(AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build())
                        .build())
                .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                .setEmail(email)
                .setSettings(AccountCreateParams.Settings.builder()
                        .setPayouts(AccountCreateParams.Settings.Payouts.builder()
                                .setSchedule(AccountCreateParams.Settings.Payouts.Schedule.builder()
                                        .setInterval(AccountCreateParams.Settings.Payouts.Schedule.Interval.MANUAL).build())
                                .build())
                        .build())
                .build();
        Account account = Account.create(params);
        return account.getId();
    }

    public String createConnectedAccountLink(String connectedAccountId) throws StripeException {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(connectedAccountId)
                .setRefreshUrl("http://localhost:3000/funds?account=false")
                .setReturnUrl("http://localhost:3000/funds")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();
        AccountLink accountLink = AccountLink.create(params);
        return accountLink.getUrl();
    }

    public void validateCheckoutSuccess(String payload, String signature) throws SignatureVerificationException {
        Webhook.constructEvent(payload, signature, checkoutSuccessEndpointSecret);
    }

    public String createCheckoutSession(String userId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/funds?deposit=true")
                .setCancelUrl("http://localhost:3000/funds?deposit=false")
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
