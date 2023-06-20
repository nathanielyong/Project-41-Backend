package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.requests.CreateUserRequest;
import com.snowtheghost.redistributor.api.models.requests.LoginUserRequest;
import com.snowtheghost.redistributor.api.models.responses.GetGameResponse;
import com.snowtheghost.redistributor.api.models.responses.GetUserResponse;
import com.snowtheghost.redistributor.api.models.responses.LoginResponse;
import com.snowtheghost.redistributor.database.models.Game;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.services.AuthenticationService;
import com.snowtheghost.redistributor.services.UserService;
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

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> createUser(@RequestBody CreateUserRequest request) {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, request.getUsername(), request.getEmail(), userService.encryptPassword(request.getPassword()));

        try {
            userService.createUser(user);
        } catch (DataIntegrityViolationException exception) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        String token = authenticationService.generateToken(user.getUserId());
        return ResponseEntity.created(URI.create(String.format("localhost:8080/users/%s", userId))).body(new LoginResponse(token));
    }

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserRequest request) {
        User user = userService.getUserByEmail(request.getEmail());

        if (user != null && userService.isValidCredentials(user, request.getEmail(), request.getPassword())) {
            String token = authenticationService.generateToken(user.getUserId());
            return ResponseEntity.ok(new LoginResponse(token));
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable String userId) {
        User user;
        try {
            user = userService.getUser(userId);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        return getUserResponse(userId, user);
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> getActiveUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        String userId;
        User user;

        try {
            userId = authenticationService.getUserId(bearerToken);
            user = userService.getUser(userId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        return getUserResponse(userId, user);
    }

    private ResponseEntity<GetUserResponse> getUserResponse(String userId, User user) {
        GetUserResponse response = new GetUserResponse(userId, user.getEmail(), user.getGames().stream().map(gamePlayer -> {
            Game game = gamePlayer.getGame();
            return new GetGameResponse(game.getGameId(), game.getCapacity(), game.getCost(), game.getType(), game.getState(), game.getPlayerUsernames());
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
