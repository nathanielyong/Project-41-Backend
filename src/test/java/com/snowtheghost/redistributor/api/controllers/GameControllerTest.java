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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private GamePlayerService gamePlayerService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGame_Success() {
        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setCapacity(10);
        createGameRequest.setCost(100);
        createGameRequest.setType(Game.Type.REDISTRIBUTE);

        ResponseEntity<CreateGameResponse> responseEntity = gameController.createGame(createGameRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        verify(gameService).createGame(any(Game.class));
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }

    @Test
    void testGetGame_Success() {
        String gameId = "game-id";
        int capacity = 10;
        int cost = 100;
        Game.Type type = Game.Type.REDISTRIBUTE;
        Game.State state = Game.State.PENDING_PLAYERS;
        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        Map<String, Integer> winners = new HashMap<>();
        winners.put("winner1", 100);
        winners.put("winner2", 200);

        Game game = new Game(gameId, capacity, cost, type);
        game.setState(state);

        when(gameService.getGame(gameId)).thenReturn(game);

        ResponseEntity<GetGameResponse> responseEntity = gameController.getGame(gameId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(gameService).getGame(gameId);
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }

    @Test
    void testGetGame_NotFound() {
        String gameId = "game-id";

        when(gameService.getGame(gameId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<GetGameResponse> responseEntity = gameController.getGame(gameId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(gameService).getGame(gameId);
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }

    @Test
    void testGetGames_Success() {
        int capacity = 10;
        int cost = 100;
        Game.Type type = Game.Type.REDISTRIBUTE;
        Game.State state = Game.State.PENDING_PLAYERS;

        Game game1 = new Game("game1", capacity, cost, type);
        Game game2 = new Game("game2", capacity, cost, type);
        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);

        when(gameService.getGames(capacity, cost, type, state)).thenReturn(games);

        ResponseEntity<GetGamesResponse> responseEntity = gameController.getGames(capacity, cost, type, state);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(gameService).getGames(capacity, cost, type, state);
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }

    @Test
    void testGetGames_NotFound() {
        int capacity = 10;
        int cost = 100;
        Game.Type type = Game.Type.REDISTRIBUTE;
        Game.State state = Game.State.PENDING_PLAYERS;

        when(gameService.getGames(capacity, cost, type, state)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<GetGamesResponse> responseEntity = gameController.getGames(capacity, cost, type, state);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(gameService).getGames(capacity, cost, type, state);
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }

    @Test
    void testJoinGame_Success() {
        String gameId = "game-id";
        String token = "Bearer token";
        String userId = "user-id";
        int gameCost = 100;

        User user = new User();
        user.setUserId(userId);

        Game game = new Game(gameId, 10, gameCost, Game.Type.REDISTRIBUTE);

        when(authenticationService.getUserId(token)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);
        when(gameService.getGame(gameId)).thenReturn(game);
        when(userService.hasSufficientFunds(user, gameCost)).thenReturn(true);

        ResponseEntity<Void> responseEntity = gameController.joinGame(gameId, token);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(token);
        verify(userService).getUser(userId);
        verify(gameService).getGame(gameId);
        verify(userService).hasSufficientFunds(user, gameCost);
        verify(userService).subtractFunds(user, gameCost);
        verify(gamePlayerService).joinGame(game, user);
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }

    @Test
    void testJoinGame_NotFound() {
        String gameId = "game-id";
        String token = "Bearer token";
        String userId = "user-id";

        when(authenticationService.getUserId(token)).thenReturn(userId);
        when(userService.getUser(userId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<Void> responseEntity = gameController.joinGame(gameId, token);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(token);
        verify(userService).getUser(userId);
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }

    @Test
    void testJoinGame_InsufficientFunds() {
        String gameId = "game-id";
        String token = "Bearer token";
        String userId = "user-id";
        int gameCost = 100;

        User user = new User();
        user.setUserId(userId);

        Game game = new Game(gameId, 10, gameCost, Game.Type.REDISTRIBUTE);

        when(authenticationService.getUserId(token)).thenReturn(userId);
        when(userService.getUser(userId)).thenReturn(user);
        when(gameService.getGame(gameId)).thenReturn(game);
        when(userService.hasSufficientFunds(user, gameCost)).thenReturn(false);

        ResponseEntity<Void> responseEntity = gameController.joinGame(gameId, token);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(authenticationService).getUserId(token);
        verify(userService).getUser(userId);
        verify(gameService).getGame(gameId);
        verify(userService).hasSufficientFunds(user, gameCost);
        verifyNoMoreInteractions(gameService, gamePlayerService, userService, authenticationService);
    }
}
