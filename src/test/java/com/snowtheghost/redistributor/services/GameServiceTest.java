package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.database.models.Game;
import com.snowtheghost.redistributor.database.models.GamePlayer;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.database.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameWinnerService gameWinnerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGame() {
        Game game = new Game("gameId", 10, 100, Game.Type.ROYALE);

        gameService.createGame(game);

        verify(gameRepository).save(game);
    }

    @Test
    void testGetGame() {
        String gameId = "gameId";
        Game game = new Game(gameId, 10, 100, Game.Type.ROYALE);

        when(gameRepository.getReferenceById(gameId)).thenReturn(game);

        Game result = gameService.getGame(gameId);

        assertEquals(game, result);

        verify(gameRepository).getReferenceById(gameId);
    }

    @Test
    void testGetGames() {
        Integer capacity = 10;
        Integer cost = 100;
        Game.Type type = Game.Type.ROYALE;
        Game.State state = Game.State.PENDING_PLAYERS;

        Game exampleGame = new Game(null, capacity, cost, type, state);
        List<Game> expectedGames = Collections.singletonList(exampleGame);

        when(gameRepository.findAll((Example<Game>) any())).thenReturn(expectedGames);

        List<Game> result = gameService.getGames(capacity, cost, type, state);

        assertEquals(expectedGames, result);

        verify(gameRepository).findAll((Example<Game>) any());
    }

    @Test
    void testPlayRoyale() {
        String gameId = "gameId";
        Game game = new Game(gameId, 10, 100, Game.Type.ROYALE);
        game.setPlayers(List.of(new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User())));

        when(gameRepository.save(game)).thenReturn(game);

        gameService.play(game);

        assertEquals(Game.State.COMPLETED, game.getState());
        verify(gameRepository).save(game);
        verify(gameWinnerService).addWinners(eq(game), any());
        verify(userService, times(1)).addFunds(any(), anyInt());
    }

    @Test
    void testPlayRedistribute() {
        String gameId = "gameId";
        Game game = new Game(gameId, 10, 100, Game.Type.REDISTRIBUTE);
        game.setPlayers(List.of(new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User()), new GamePlayer(game, new User())));

        when(gameRepository.save(game)).thenReturn(game);

        gameService.play(game);

        assertEquals(Game.State.COMPLETED, game.getState());
        verify(gameRepository).save(game);
        verify(gameWinnerService).addWinners(eq(game), any());
        verify(userService, atLeast(1)).addFunds(any(), anyInt());
    }
}
