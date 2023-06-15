package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.requests.JoinGameRequest;
import com.snowtheghost.redistributor.api.models.responses.GetGameResponse;
import com.snowtheghost.redistributor.database.models.Game;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.services.GamePlayerService;
import com.snowtheghost.redistributor.services.GameService;
import com.snowtheghost.redistributor.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final GamePlayerService gamePlayerService;
    private final UserService userService;

    @Autowired
    public GameController(GameService gameService, GamePlayerService gamePlayerService, UserService userService) {
        this.gameService = gameService;
        this.gamePlayerService = gamePlayerService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createGame() {
        String gameId = UUID.randomUUID().toString();
        int capacity = 10;
        Game game = new Game(gameId, capacity);
        gameService.createGame(game);

        return ResponseEntity.created(URI.create(String.format("localhost:8080/games/%s", gameId))).build();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GetGameResponse> getGame(@PathVariable String gameId) {
        Game game = gameService.getGame(gameId);

        GetGameResponse response = new GetGameResponse(game.getGameId(), game.getCapacity(), game.getPlayers().stream().map(gamePlayer -> gamePlayer.getUser().getEmail()).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{gameId}/join")
    public ResponseEntity<Void> joinGame(@PathVariable("gameId") String gameId, @RequestBody JoinGameRequest request) {
        Game game = gameService.getGame(gameId);
        User user = userService.getUser(request.getUserId());
        gamePlayerService.joinGame(game, user);
        return ResponseEntity.noContent().build();
    }
}
