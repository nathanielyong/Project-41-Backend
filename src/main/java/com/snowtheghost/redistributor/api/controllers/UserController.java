package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.requests.CreateUserRequest;
import com.snowtheghost.redistributor.api.models.requests.LoginUserRequest;
import com.snowtheghost.redistributor.api.models.responses.GetUserResponse;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.infrastructure.authentication.JwtTokenProvider;
import com.snowtheghost.redistributor.services.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        String userId = UUID.randomUUID().toString();
        String email = request.getEmail();
        String encryptedPassword;
        encryptedPassword = userService.encryptPassword(request.getPassword());

        User user = new User(userId, email, encryptedPassword);
        userService.createUser(user);

        String token = jwtTokenProvider.generateToken(user.getUserId());
        return ResponseEntity.created(URI.create(String.format("localhost:8080/users/%s", userId))).header("Authorization", "Bearer " + token).build();
    }

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<Void> loginUser(@RequestBody LoginUserRequest request) {
        User user = userService.getUserByEmail(request.getEmail());
        if (userService.isValidCredentials(user, request.getEmail(), request.getPassword())) {
            String token = jwtTokenProvider.generateToken(user.getUserId());
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getGame(@PathVariable String userId) {
        User user = userService.getUser(userId);

        GetUserResponse response = new GetUserResponse(userId, user.getEmail(), user.getGames().stream().map(gamePlayer -> gamePlayer.getGame().getGameId()).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
