package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.models.Game;
import com.snowtheghost.project41.database.models.GamePlayer;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.database.repositories.GamePlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamePlayerServiceTest {

    @Mock
    private GamePlayerRepository gamePlayerRepository;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GamePlayerService gamePlayerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testJoinGame_Success() {
        Game game = new Game("gameId", 10, 100, Game.Type.ROYALE);
        User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");

        doAnswer(invocation -> {
            GamePlayer gamePlayer = invocation.getArgument(0);
            game.getPlayers().add(gamePlayer);
            return gamePlayer;
        }).when(gamePlayerRepository).save(any(GamePlayer.class));

        gamePlayerService.joinGame(game, user);

        verify(gamePlayerRepository).save(any(GamePlayer.class));

        assertNotNull(game.getPlayers());
        assertEquals(1, game.getPlayers().size());
        GamePlayer addedPlayer = game.getPlayers().get(0);
        assertEquals(user, addedPlayer.getUser());
        assertEquals(game, addedPlayer.getGame());

        verifyNoInteractions(gameService);
    }

    @Test
    void testJoinGame_GameFull() {
        Game game = new Game("gameId", 10, 100, Game.Type.ROYALE);
        User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");

        game.setState(Game.State.COMPLETED);

        assertThrows(IllegalStateException.class, () -> gamePlayerService.joinGame(game, user));

        verify(gamePlayerRepository, never()).save(any(GamePlayer.class));
        verifyNoInteractions(gameService);
    }

    @Test
    void testJoinGame_PlayGame() {
        Game game = new Game("gameId", 2, 100, Game.Type.ROYALE);
        User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");

        doAnswer(invocation -> {
            GamePlayer gamePlayer = invocation.getArgument(0);
            game.getPlayers().add(gamePlayer);
            return gamePlayer;
        }).when(gamePlayerRepository).save(any(GamePlayer.class));

        gamePlayerService.joinGame(game, user);

        verify(gamePlayerRepository).save(any(GamePlayer.class));

        assertNotNull(game.getPlayers());
        assertEquals(1, game.getPlayers().size());
        GamePlayer addedPlayer = game.getPlayers().get(0);
        assertEquals(user, addedPlayer.getUser());
        assertEquals(game, addedPlayer.getGame());

        verifyNoInteractions(gameService);

        User anotherUser = new User("anotherUserId", "anotherUsername", "anotherEmail", "anotherEncryptedPassword", "anotherEncryptedBalance", "anotherConnectedAccountId");
        gamePlayerService.joinGame(game, anotherUser);

        verify(gamePlayerRepository, times(2)).save(any(GamePlayer.class));
        verify(gameService).play(game);
    }
}
