package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.models.Game;
import com.snowtheghost.project41.database.models.GameWinner;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.database.repositories.GameWinnerRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameWinnerService {

    private final GameWinnerRepository gameWinnerRepository;
    private final UserService userService;

    public GameWinnerService(GameWinnerRepository gameWinnerRepository, UserService userService) {
        this.gameWinnerRepository = gameWinnerRepository;
        this.userService = userService;
    }

    public void addWinners(Game game, Map<String, Integer> results) {
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            String userId = entry.getKey();
            Integer earnings = entry.getValue();
            User user = userService.getUser(userId);
            GameWinner gameWinner = new GameWinner(game, user, earnings);
            gameWinnerRepository.save(gameWinner);
        }
    }
}
