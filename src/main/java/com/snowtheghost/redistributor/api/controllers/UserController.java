package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.requests.users.CreateUserRequest;
import com.snowtheghost.redistributor.api.models.requests.users.LoginUserRequest;
import com.snowtheghost.redistributor.api.models.responses.games.GetGameResponse;
import com.snowtheghost.redistributor.api.models.responses.users.CreateUserResponse;
import com.snowtheghost.redistributor.api.models.responses.users.GetUserResponse;
import com.snowtheghost.redistributor.api.models.responses.users.LoginResponse;
import com.snowtheghost.redistributor.database.models.Game;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.services.AuthenticationService;
import com.snowtheghost.redistributor.services.StripeService;
import com.snowtheghost.redistributor.services.UserService;
import com.stripe.exception.StripeException;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final StripeService stripeService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, StripeService stripeService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.stripeService = stripeService;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        String connectedAccountId;
        try {
            connectedAccountId = stripeService.createConnectedAccount(request.getEmail());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String userId = UUID.randomUUID().toString();
        try {
            userService.createUser(userId, request.getUsername(), request.getEmail(), request.getPassword(), connectedAccountId);
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String connectedAccountLinkUrl;
        try {
            connectedAccountLinkUrl = stripeService.createConnectedAccountLink(connectedAccountId);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        String token = authenticationService.generateToken(userId);
        return ResponseEntity.created(URI.create(String.format("localhost:8080/users/%s", userId))).body(new CreateUserResponse(token, connectedAccountLinkUrl));
    }

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserRequest request) {
        User user = userService.getUserByEmail(request.getEmail());

        if (user != null && userService.isValidCredentials(user, request.getEmail(), request.getPassword())) {
            String token = authenticationService.generateToken(user.getUserId());
            return ResponseEntity.ok(new LoginResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable String userId) {
        User user;
        try {
            user = userService.getUser(userId);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(getUserResponse(user));
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> getActiveUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        String userId;
        User user;
        Boolean chargesEnabled = null;
        try {
            userId = authenticationService.getUserId(bearerToken);
            user = userService.getUser(userId);

            if (!user.getConnectedAccountId().isEmpty()) {
                chargesEnabled = stripeService.isChargesEnabled(user.getConnectedAccountId());
            }
        } catch (EntityNotFoundException | StripeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(getUserResponse(user, chargesEnabled));
    }

    private GetUserResponse getUserResponse(User user) {
        return getUserResponse(user, null);
    }

    private GetUserResponse getUserResponse(User user, Boolean chargesEnabled) {
        return new GetUserResponse(user.getUserId(), user.getUsername(), user.getEmail(), userService.getBalance(user), user.getConnectedAccountId(), chargesEnabled,
                user.getGames().stream().map(gamePlayer -> {
                    Game game = gamePlayer.getGame();
                    return new GetGameResponse(
                            game.getGameId(),
                            game.getCapacity(),
                            game.getCost(),
                            game.getType(),
                            game.getState(),
                            game.getPlayerUsernames(),
                            game.getWinnerUsernamesToEarnings()
                    );
                }).collect(Collectors.toList()));
    }
}
