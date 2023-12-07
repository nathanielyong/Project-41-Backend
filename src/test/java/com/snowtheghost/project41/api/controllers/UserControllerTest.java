package com.snowtheghost.project41.api.controllers;

import com.snowtheghost.project41.api.models.requests.users.CreateUserRequest;
import com.snowtheghost.project41.api.models.requests.users.LoginUserRequest;
import com.snowtheghost.project41.api.models.responses.users.CreateUserResponse;
import com.snowtheghost.project41.api.models.responses.users.GetUserResponse;
import com.snowtheghost.project41.api.models.responses.users.LoginResponse;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.services.AuthenticationService;
import com.snowtheghost.project41.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("username");
        createUserRequest.setEmail("email@example.com");
        createUserRequest.setPassword("password");
        createUserRequest.setType("PLAYER");

        String token = "token";

        when(authenticationService.generateToken(anyString())).thenReturn(token);

        ResponseEntity<CreateUserResponse> responseEntity = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(userService).createPlayerUser(anyString(), eq(createUserRequest.getUsername()), eq(createUserRequest.getEmail()), eq(createUserRequest.getPassword()));

        verify(authenticationService).generateToken(anyString());
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testLoginUser_Success() {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("email@example.com");
        loginUserRequest.setPassword("password");

        User user = new User();
        user.setUserId("user-id");

        when(userService.getUserByEmail(loginUserRequest.getEmail())).thenReturn(user);
        when(userService.loginUser(loginUserRequest.getEmail(), loginUserRequest.getPassword())).thenReturn("token");

        ResponseEntity<LoginResponse> responseEntity = userController.loginUser(loginUserRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService).loginUser(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testLoginUser_Unauthorized() {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("email@example.com");
        loginUserRequest.setPassword("password");

        when(userService.getUserByEmail(loginUserRequest.getEmail())).thenReturn(null);

        ResponseEntity<LoginResponse> responseEntity = userController.loginUser(loginUserRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        verify(userService).loginUser(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("email@example.com");
        loginUserRequest.setPassword("password");

        User user = new User();
        user.setUserId("user-id");

        when(userService.getUserByEmail(loginUserRequest.getEmail())).thenReturn(user);
        when(userService.loginUser(loginUserRequest.getEmail(), loginUserRequest.getPassword())).thenReturn(null);

        ResponseEntity<LoginResponse> responseEntity = userController.loginUser(loginUserRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        verify(userService).loginUser(loginUserRequest.getEmail(), loginUserRequest.getPassword());
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testGetUser_Success() {
        String userId = "user-id";

        User user = new User();
        user.setUserId(userId);
        user.setType(User.Type.PLAYER);

        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<GetUserResponse> responseEntity = userController.getUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(userService).getUser(userId);
        verify(userService).getBalance(user);
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testGetUser_NotFound() {
        String userId = "user-id";

        when(userService.getUser(userId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<GetUserResponse> responseEntity = userController.getUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(userService).getUser(userId);
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testGetActiveUser_Success() {
        String bearerToken = "Bearer token";
        String userId = "user-id";

        User user = new User();
        user.setUserId(userId);
        user.setType(User.Type.PLAYER);

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<GetUserResponse> responseEntity = userController.getActiveUser(bearerToken);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);

        verify(userService).getBalance(user);
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testGetActiveUser_NotFound() {
        String bearerToken = "Bearer token";
        String userId = "user-id";

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);
        when(userService.getUser(userId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<GetUserResponse> responseEntity = userController.getActiveUser(bearerToken);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verify(userService).getUser(userId);
        verifyNoMoreInteractions(userService, authenticationService);
    }

    @Test
    void testDeleteUser_Success() {
        String bearerToken = "Bearer token";
        String userId = "user-id";

        when(authenticationService.getUserId(bearerToken)).thenReturn(userId);

        ResponseEntity<Object> responseEntity = userController.deleteUser(bearerToken);
        verify(userService).deleteUser(userId);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(bearerToken);
        verifyNoMoreInteractions(userService, authenticationService);
    }
}
