package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.requests.CreateUserRequest;
import com.snowtheghost.redistributor.api.models.responses.GetUserResponse;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        String userId = UUID.randomUUID().toString();
        String email = request.getEmail();
        String encryptedPassword;
        try {
            encryptedPassword = userService.encryptPassword(request.getPassword());
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User(userId, email, encryptedPassword);
        userService.createUser(user);

        return ResponseEntity.created(URI.create(String.format("localhost:8080/users/%s", userId))).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getGame(@PathVariable String userId) {
        User user = userService.getUser(userId);

        GetUserResponse response = new GetUserResponse(userId, user.getEmail(), user.getGames().stream().map(gamePlayer -> gamePlayer.getGame().getGameId()).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
