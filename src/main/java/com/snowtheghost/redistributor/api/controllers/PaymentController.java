package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.services.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
    public ResponseEntity<Void> createCheckoutSession() {
        URI redirectLocation;
        try {
            redirectLocation = stripeService.createCheckoutSession();
        } catch (StripeException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(redirectLocation).header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000").build();
    }
}
