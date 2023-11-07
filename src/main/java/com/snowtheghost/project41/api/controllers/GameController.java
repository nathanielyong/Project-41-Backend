package com.snowtheghost.project41.api.controllers;

import com.snowtheghost.project41.api.models.responses.games.GameResponse;
import com.snowtheghost.project41.api.models.responses.games.GetGameResponse;
import com.snowtheghost.project41.api.models.responses.games.GetGamesResponse;
import com.snowtheghost.project41.database.models.Game;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.services.AuthenticationService;
import com.snowtheghost.project41.services.GameService;
import com.snowtheghost.project41.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public GameController(GameService gameService,
                          UserService userService,
                          AuthenticationService authenticationService) {
        this.gameService = gameService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameResponse> startGame(
        @RequestParam String gameType, 
        @RequestParam String player1_type,
        @RequestParam String player2_type,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        User user;
        try {
            String userId = authenticationService.getUserId(token);
            user = userService.getUser(userId);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        GameResponse response = gameService.startGame(user, gameType, player1_type, player2_type);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/move")
    public ResponseEntity<GameResponse> makeMove( 
        @RequestParam String move,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        User user;
        try {
            String userId = authenticationService.getUserId(token);
            user = userService.getUser(userId);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        GameResponse response = gameService.makeMove(user, user.getCurrentGameId(), move);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/quit")
    public ResponseEntity<GameResponse> makeMove(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        User user;
        try {
            String userId = authenticationService.getUserId(token);
            user = userService.getUser(userId);
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

        GameResponse response = gameService.quitGame(user, user.getCurrentGameId());
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(response);
    }

    // TODO: We should avoid using the Game model directly in this layer
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
                game.getType(),
                game.getState(),
                game.getMetrics()
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
                game.getType(),
                game.getState(),
                game.getMetrics()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(new GetGamesResponse(responseGames));
    }

    // @PostMapping("/create")
    // public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest request) {
    //     String gameId = UUID.randomUUID().toString();
    //     Game game = new Game(gameId, request.getCapacity(), request.getCost(), request.getType());
    //     gameService.createGame(game);

    //     return ResponseEntity.status(HttpStatus.CREATED).body(new CreateGameResponse(gameId));
    // }

    // @PostMapping("/move/{gameName}")
    // public ResponseEntity<MakeMoveResponse> makeMove(@PathVariable String gameName) {

    //     return ResponseEntity.ok(response);
    // }

    // @PostMapping("/quit/{gameName}")
    // public ResponseEntity<QuitGameResponse> quitGame(@PathVariable String gameName) {

    //     return ResponseEntity.ok(response);
    // }

    // @PutMapping("/{gameId}/join")
    // public ResponseEntity<Void> joinGame(@PathVariable("gameId") String gameId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    //     Game game;
    //     User user;
    //     try {
    //         String userId = authenticationService.getUserId(token);
    //         user = userService.getUser(userId);
    //         game = gameService.getGame(gameId);
    //     } catch (EntityNotFoundException exception) {
    //         return ResponseEntity.notFound().build();
    //     }

    //     if (!userService.hasSufficientFunds(user, game.getCost())) {
    //         return ResponseEntity.badRequest().build();
    //     }

    //     userService.subtractFunds(user, game.getCost());

    //     try {
    //         gamePlayerService.joinGame(game, user);
    //     } catch (DataIntegrityViolationException exception) {
    //         return ResponseEntity.status(HttpStatus.CONFLICT).build();
    //     } catch (IllegalStateException exception) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    //     }

    //     return ResponseEntity.noContent().build();
    // }
}
