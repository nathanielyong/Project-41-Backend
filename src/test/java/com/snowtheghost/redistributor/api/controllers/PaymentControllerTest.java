package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.requests.WithdrawalRequest;
import com.snowtheghost.redistributor.api.models.responses.payment.CreateCheckoutSessionResponse;
import com.snowtheghost.redistributor.api.models.responses.payment.RegisterConnectedAccountResponse;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.services.AuthenticationService;
import com.snowtheghost.redistributor.services.StripeService;
import com.snowtheghost.redistributor.services.UserService;
import com.stripe.exception.ApiException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    @Mock
    private StripeService stripeService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCheckoutSession_Success() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";
        String redirectUrl = "https://example.com/checkout/success";

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(stripeService.createCheckoutSession(userId)).thenReturn(redirectUrl);

        ResponseEntity<CreateCheckoutSessionResponse> responseEntity = paymentController.createCheckoutSession(bearerToken);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(stripeService).createCheckoutSession(userId);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testCreateCheckoutSession_StripeException() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(stripeService.createCheckoutSession(userId)).thenThrow(new ApiException("", "", "", 1, new Exception()));

        ResponseEntity<CreateCheckoutSessionResponse> responseEntity = paymentController.createCheckoutSession(bearerToken);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(stripeService).createCheckoutSession(userId);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testHandleCheckoutSuccess_ValidPayload() throws Exception {
        String signature = "stripe-signature";
        String payload = "{\"data\": {\"object\": {\"amount_total\": 100, \"client_reference_id\": \"user-id\"}}}";

        ResponseEntity<Void> responseEntity = paymentController.handleCheckoutSuccess(signature, payload);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(stripeService).validateCheckoutSuccess(payload, signature);
        verify(userService).addFunds("user-id", 100);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testHandleCheckoutSuccess_InvalidSignature() throws Exception {
        String signature = "invalid-signature";
        String payload = "{\"userId\":\"user-id\",\"amount\":100}";

        doThrow(SignatureVerificationException.class).when(stripeService).validateCheckoutSuccess(payload, signature);

        ResponseEntity<Void> responseEntity = paymentController.handleCheckoutSuccess(signature, payload);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        verify(stripeService).validateCheckoutSuccess(payload, signature);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testHandleCheckoutSuccess_InvalidPayload() throws Exception {
        String signature = "stripe-signature";
        String payload = "invalid-payload";


        ResponseEntity<Void> responseEntity = paymentController.handleCheckoutSuccess(signature, payload);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(stripeService).validateCheckoutSuccess(payload, signature);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testRegisterConnectedAccount_Success() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";
        String connectedAccountId = "connected-account-id";
        String connectedAccountLinkUrl = "https://example.com/connected-account/link";

        User user = new User();
        user.setConnectedAccountId(connectedAccountId);

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);
        when(stripeService.createConnectedAccountLink(connectedAccountId)).thenReturn(connectedAccountLinkUrl);

        ResponseEntity<RegisterConnectedAccountResponse> responseEntity = paymentController.registerConnectedAccount(bearerToken);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verify(stripeService).createConnectedAccountLink(connectedAccountId);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testRegisterConnectedAccount_EntityNotFoundException() {
        String bearerToken = "Bearer token";
        String userId = "user-id";

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<RegisterConnectedAccountResponse> responseEntity = paymentController.registerConnectedAccount(bearerToken);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testRegisterConnectedAccount_StripeException() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";
        String connectedAccountId = "connected-account-id";

        User user = new User();
        user.setConnectedAccountId(connectedAccountId);

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);
        when(stripeService.createConnectedAccountLink(connectedAccountId)).thenThrow(new ApiException("", "", "", 1, new Exception()));

        ResponseEntity<RegisterConnectedAccountResponse> responseEntity = paymentController.registerConnectedAccount(bearerToken);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verify(stripeService).createConnectedAccountLink(connectedAccountId);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testWithdraw_Success() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";
        int withdrawalAmount = 100;

        User user = new User();
        user.setConnectedAccountId("connected-account-id");

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);
        when(userService.hasSufficientFunds(user, withdrawalAmount)).thenReturn(true);

        ResponseEntity<Void> responseEntity = paymentController.withdraw(bearerToken, new WithdrawalRequest(withdrawalAmount));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verify(userService).hasSufficientFunds(user, withdrawalAmount);
        verify(userService).subtractFunds(user, withdrawalAmount);
        verify(stripeService).withdraw(user.getConnectedAccountId(), withdrawalAmount);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        String bearerToken = "Bearer token";
        String userId = "user-id";
        int withdrawalAmount = 100;

        User user = new User();
        user.setConnectedAccountId("connected-account-id");

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);
        when(userService.hasSufficientFunds(user, withdrawalAmount)).thenReturn(false);

        ResponseEntity<Void> responseEntity = paymentController.withdraw(bearerToken, new WithdrawalRequest(withdrawalAmount));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verify(userService).hasSufficientFunds(user, withdrawalAmount);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }

    @Test
    void testWithdraw_StripeException() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";
        int withdrawalAmount = 100;

        User user = new User();
        user.setConnectedAccountId("connected-account-id");

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);
        when(userService.hasSufficientFunds(user, withdrawalAmount)).thenReturn(true);
        doThrow(new ApiException("", "", "", 1, new Exception())).when(stripeService).withdraw(user.getConnectedAccountId(), withdrawalAmount);

        ResponseEntity<Void> responseEntity = paymentController.withdraw(bearerToken, new WithdrawalRequest(withdrawalAmount));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verify(userService).hasSufficientFunds(user, withdrawalAmount);
        verify(userService).subtractFunds(user, withdrawalAmount);
        verify(stripeService).withdraw(user.getConnectedAccountId(), withdrawalAmount);
        verify(userService).addFunds(userId, withdrawalAmount);
        verifyNoMoreInteractions(stripeService, userService, authenticationService);
    }
}
