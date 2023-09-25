package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.models.Game;
import com.snowtheghost.project41.database.models.GameWinner;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.database.repositories.GameWinnerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameWinnerServiceTest {

    @Mock
    private GameWinnerRepository gameWinnerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private GameWinnerService gameWinnerService;

    public GameWinnerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddWinners() {
        Game game = new Game("gameId", 2, 100, Game.Type.ROYALE);
        Map<String, Integer> results = new HashMap<>();
        results.put("userId1", 50);
        results.put("userId2", 75);

        User user1 = new User("userId1", "username1", "email1", "password1", "balance1", "connectedAccountId1");
        User user2 = new User("userId2", "username2", "email2", "password2", "balance2", "connectedAccountId2");

        when(userService.getUser("userId1")).thenReturn(user1);
        when(userService.getUser("userId2")).thenReturn(user2);

        gameWinnerService.addWinners(game, results);

        verify(gameWinnerRepository, times(2)).save(any(GameWinner.class));
    }
}
