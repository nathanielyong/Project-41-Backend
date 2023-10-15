package com.snowtheghost.project41.api.controllers;

import com.snowtheghost.project41.api.models.requests.users.CreateUserRequest;
import com.snowtheghost.project41.api.models.requests.users.LoginUserRequest;
import com.snowtheghost.project41.api.models.responses.users.CreateUserResponse;
import com.snowtheghost.project41.api.models.responses.users.GetUserResponse;
import com.snowtheghost.project41.api.models.responses.users.LoginResponse;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.services.AuthenticationService;
import com.snowtheghost.project41.services.StripeService;
import com.snowtheghost.project41.services.UserService;
import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() throws StripeException {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("username");
        createUserRequest.setEmail("email@example.com");
        createUserRequest.setPassword("password");

//      TODO: Uncomment to use Stripe
//        String connectedAccountId = "connected-account-id";
//        String connectedAccountLinkUrl = "connected-account-link-url";
        String token = "token";

//      TODO: Uncomment to use Stripe
//        when(stripeService.createConnectedAccount(createUserRequest.getEmail())).thenReturn(connectedAccountId);
//        when(stripeService.createConnectedAccountLink(connectedAccountId)).thenReturn(connectedAccountLinkUrl);
        when(authenticationService.generateToken(anyString())).thenReturn(token);

        ResponseEntity<CreateUserResponse> responseEntity = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

//      TODO: Uncomment to use Stripe
//        verify(stripeService).createConnectedAccount(createUserRequest.getEmail());
//        verify(userService).createUser(anyString(), eq(createUserRequest.getUsername()), eq(createUserRequest.getEmail()), eq(createUserRequest.getPassword()), eq(connectedAccountId));

        verify(userService).createUser(anyString(), eq(createUserRequest.getUsername()), eq(createUserRequest.getEmail()), eq(createUserRequest.getPassword()), eq("Placeholder"));

//      TODO: Uncomment to use Stripe
//        verify(stripeService).createConnectedAccountLink(connectedAccountId);
        verify(authenticationService).generateToken(anyString());
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

//  TODO: Uncomment to use Stripe
//    @Test
//    void testCreateUser_StripeException() throws StripeException {
//        CreateUserRequest createUserRequest = new CreateUserRequest();
//        createUserRequest.setUsername("username");
//        createUserRequest.setEmail("email@example.com");
//        createUserRequest.setPassword("password");
//
//        when(stripeService.createConnectedAccount(createUserRequest.getEmail())).thenThrow(new ApiException("", "", "", 1, new Exception()));
//
//        ResponseEntity<CreateUserResponse> responseEntity = userController.createUser(createUserRequest);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//
//        verify(stripeService).createConnectedAccount(createUserRequest.getEmail());
//        verifyNoMoreInteractions(userService, authenticationService, stripeService);
//    }

//  TODO: Uncomment to use Stripe
//    @Test
//    void testCreateUser_StripeConflict() throws StripeException {
//        CreateUserRequest createUserRequest = new CreateUserRequest();
//        createUserRequest.setUsername("username");
//        createUserRequest.setEmail("email@example.com");
//        createUserRequest.setPassword("password");
//
//        String connectedAccountId = "connected-account-id";
//
//        when(stripeService.createConnectedAccount(createUserRequest.getEmail())).thenReturn(connectedAccountId);
//        doThrow(new DataIntegrityViolationException("")).when(userService).createUser(anyString(), eq(createUserRequest.getUsername()), eq(createUserRequest.getEmail()), eq(createUserRequest.getPassword()), eq(connectedAccountId));
//
//        ResponseEntity<CreateUserResponse> responseEntity = userController.createUser(createUserRequest);
//
//        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//
//        verify(stripeService).createConnectedAccount(createUserRequest.getEmail());
//        verify(userService).createUser(anyString(), eq(createUserRequest.getUsername()), eq(createUserRequest.getEmail()), eq(createUserRequest.getPassword()), eq(connectedAccountId));
//        verifyNoMoreInteractions(userService, authenticationService, stripeService);
//    }

//  TODO: Uncomment to use Stripe
//    @Test
//    void testCreateUser_ServiceUnavailable() throws StripeException {
//        CreateUserRequest createUserRequest = new CreateUserRequest();
//        createUserRequest.setUsername("username");
//        createUserRequest.setEmail("email@example.com");
//        createUserRequest.setPassword("password");
//
//        String connectedAccountId = "connected-account-id";
//        String userId = "user-id";
//
//        when(stripeService.createConnectedAccount(createUserRequest.getEmail())).thenReturn(connectedAccountId);
//        when(stripeService.createConnectedAccountLink(connectedAccountId)).thenThrow(new ApiException("", "", "", 1, new Exception()));
//
//        ResponseEntity<CreateUserResponse> responseEntity = userController.createUser(createUserRequest);
//
//        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
//
//        verify(stripeService).createConnectedAccount(createUserRequest.getEmail());
//        verify(userService).createUser(anyString(), eq(createUserRequest.getUsername()), eq(createUserRequest.getEmail()), eq(createUserRequest.getPassword()), eq(connectedAccountId));
//        verify(stripeService).createConnectedAccountLink(connectedAccountId);
//        verifyNoMoreInteractions(userService, authenticationService, stripeService);
//    }

    @Test
    void testLoginUser_Success() {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("email@example.com");
        loginUserRequest.setPassword("password");

        User user = new User();
        user.setUserId("user-id");

        when(userService.getUserByEmail(loginUserRequest.getEmail())).thenReturn(user);
        when(userService.isValidCredentials(user, loginUserRequest.getEmail(), loginUserRequest.getPassword())).thenReturn(true);
        when(authenticationService.generateToken(user.getUserId())).thenReturn("token");

        ResponseEntity<LoginResponse> responseEntity = userController.loginUser(loginUserRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService).getUserByEmail(loginUserRequest.getEmail());
        verify(userService).isValidCredentials(user, loginUserRequest.getEmail(), loginUserRequest.getPassword());
        verify(authenticationService).generateToken(user.getUserId());
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

    @Test
    void testLoginUser_Unauthorized() {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("email@example.com");
        loginUserRequest.setPassword("password");

        when(userService.getUserByEmail(loginUserRequest.getEmail())).thenReturn(null);

        ResponseEntity<LoginResponse> responseEntity = userController.loginUser(loginUserRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        verify(userService).getUserByEmail(loginUserRequest.getEmail());
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("email@example.com");
        loginUserRequest.setPassword("password");

        User user = new User();
        user.setUserId("user-id");

        when(userService.getUserByEmail(loginUserRequest.getEmail())).thenReturn(user);
        when(userService.isValidCredentials(user, loginUserRequest.getEmail(), loginUserRequest.getPassword())).thenReturn(false);

        ResponseEntity<LoginResponse> responseEntity = userController.loginUser(loginUserRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        verify(userService).getUserByEmail(loginUserRequest.getEmail());
        verify(userService).isValidCredentials(user, loginUserRequest.getEmail(), loginUserRequest.getPassword());
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

    @Test
    void testGetUser_Success() {
        String userId = "user-id";

        User user = new User();
        user.setUserId(userId);

        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<GetUserResponse> responseEntity = userController.getUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(userService).getUser(userId);
        verify(userService).getBalance(user);
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

    @Test
    void testGetUser_NotFound() {
        String userId = "user-id";

        when(userService.getUser(userId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<GetUserResponse> responseEntity = userController.getUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(userService).getUser(userId);
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

    @Test
    void testGetActiveUser_Success() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";
        String connectedAccountId = "connected-account-id";

        User user = new User();
        user.setUserId(userId);
        user.setConnectedAccountId(connectedAccountId);

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);

//      TODO: Uncomment to use Stripe
//        when(stripeService.isChargesEnabled(connectedAccountId)).thenReturn(true);

        ResponseEntity<GetUserResponse> responseEntity = userController.getActiveUser(bearerToken);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);

//      TODO: Uncomment to use Stripe
//        verify(stripeService).isChargesEnabled(connectedAccountId);

        verify(userService).getBalance(user);
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

    @Test
    void testGetActiveUser_NotFound() throws StripeException {
        String bearerToken = "Bearer token";
        String userId = "user-id";

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<GetUserResponse> responseEntity = userController.getActiveUser(bearerToken);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verifyNoMoreInteractions(userService, authenticationService, stripeService);
    }

//  TODO: Uncomment to use Stripe
//    @Test
//    void testGetActiveUser_StripeException() throws StripeException {
//        String bearerToken = "Bearer token";
//        String userId = "user-id";
//        String connectedAccountId = "connected-account-id";
//
//        User user = new User();
//        user.setUserId(userId);
//        user.setConnectedAccountId(connectedAccountId);
//
//        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
//        when(userService.getUser(userId)).thenReturn(user);
//        when(stripeService.isChargesEnabled(connectedAccountId)).thenThrow(new ApiException("", "", "", 1, new Exception()));
//
//        ResponseEntity<GetUserResponse> responseEntity = userController.getActiveUser(bearerToken);
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//
//        verify(authenticationService).getUserId(bearerToken);
//        verify(userService).getUser(userId);
//        verify(stripeService).isChargesEnabled(connectedAccountId);
//        verifyNoMoreInteractions(userService, authenticationService, stripeService);
//    }
}
