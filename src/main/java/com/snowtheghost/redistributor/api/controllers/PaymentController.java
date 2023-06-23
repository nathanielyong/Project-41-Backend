package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.responses.payment.CreateCheckoutSessionResponse;
import com.snowtheghost.redistributor.services.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/payment")
public class PaymentController {

    private final StripeService stripeService;

    @Autowired
    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/stripe/create-checkout-session")
    public ResponseEntity<CreateCheckoutSessionResponse> createCheckoutSession() {
        String redirectUrl;
        try {
            redirectUrl = stripeService.createCheckoutSession();
        } catch (StripeException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(new CreateCheckoutSessionResponse(redirectUrl));
    }
}
