package com.snowtheghost.redistributor.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowtheghost.redistributor.api.models.requests.stripe.StripeCheckoutSuccessRequest;
import com.snowtheghost.redistributor.api.models.responses.payment.CreateCheckoutSessionResponse;
import com.snowtheghost.redistributor.services.AuthenticationService;
import com.snowtheghost.redistributor.services.StripeService;
import com.snowtheghost.redistributor.services.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/payment")
public class PaymentController {

    private final StripeService stripeService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public PaymentController(UserService userService, StripeService stripeService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.stripeService = stripeService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/stripe/checkout/session/create")
    public ResponseEntity<CreateCheckoutSessionResponse> createCheckoutSession(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        String userId = authenticationService.getUserId(bearerToken);
        String redirectUrl;
        try {
            redirectUrl = stripeService.createCheckoutSession(userId);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(new CreateCheckoutSessionResponse(redirectUrl));
    }

    @PostMapping("/stripe/checkout/handle/success")
    public ResponseEntity<Void> handleCheckoutSuccess(@RequestHeader("stripe-signature") String signature, @RequestBody String payload) {
        try {
            stripeService.validateCheckoutSuccess(payload, signature);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        StripeCheckoutSuccessRequest request;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            request = objectMapper.readValue(payload, StripeCheckoutSuccessRequest.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        userService.addFunds(request.getUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
