package com.snowtheghost.redistributor.api.controllers;

import com.snowtheghost.redistributor.api.models.requests.games.CreateGameRequest;
import com.snowtheghost.redistributor.api.models.responses.games.CreateGameResponse;
import com.snowtheghost.redistributor.api.models.responses.games.GetGameResponse;
import com.snowtheghost.redistributor.api.models.responses.games.GetGamesResponse;
import com.snowtheghost.redistributor.database.models.Game;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.services.AuthenticationService;
import com.snowtheghost.redistributor.services.GamePlayerService;
import com.snowtheghost.redistributor.services.GameService;
import com.snowtheghost.redistributor.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final GamePlayerService gamePlayerService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public GameController(GameService gameService,
                          GamePlayerService gamePlayerService,
                          UserService userService,
                          AuthenticationService authenticationService) {
        this.gameService = gameService;
        this.gamePlayerService = gamePlayerService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest request) {
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId, request.getCapacity(), request.getCost(), request.getType());
        gameService.createGame(game);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateGameResponse(gameId));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GetGameResponse> getGame(@PathVariable String gameId) {
        Game game;
        try {
            game = gameService.getGame(gameId);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        GetGameResponse response = new GetGameResponse(
                game.getGameId(),
                game.getCapacity(),
                game.getCost(),
                game.getType(),
                game.getState(),
                game.getPlayerUsernames(),
                game.getWinnerUsernamesToEarnings()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<GetGamesResponse> getGames(
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Integer cost,
            @RequestParam(required = false) Game.Type type,
            @RequestParam(required = false) Game.State state
    ) {
        List<Game> games;
        try {
            games = gameService.getGames(capacity, cost, type, state);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        List<GetGameResponse> responseGames = games.stream().map(game -> new GetGameResponse(
                game.getGameId(),
                game.getCapacity(),
                game.getCost(),
                game.getType(),
                game.getState(),
                game.getPlayerUsernames(),
                game.getWinnerUsernamesToEarnings()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(new GetGamesResponse(responseGames));
    }

    @PutMapping("/{gameId}/join")
    public ResponseEntity<Void> joinGame(@PathVariable("gameId") String gameId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Game game;
        User user;
        try {
            String userId = authenticationService.getUserId(token);
            user = userService.getUser(userId);
            game = gameService.getGame(gameId);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        try {
            gamePlayerService.joinGame(game, user);
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.noContent().build();
    }
}
