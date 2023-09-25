package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.models.Game;
import com.snowtheghost.project41.database.models.GamePlayer;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.database.repositories.GamePlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamePlayerService {

    private final GamePlayerRepository gamePlayerRepository;
    private final GameService gameService;

    @Autowired
    public GamePlayerService(GamePlayerRepository gamePlayerRepository, GameService gameService) {
        this.gamePlayerRepository = gamePlayerRepository;
        this.gameService = gameService;
    }

    public void joinGame(Game game, User user) {
        if (game.getState() != Game.State.PENDING_PLAYERS) {
            throw new IllegalStateException("Game is full");
        }

        GamePlayer gamePlayer = new GamePlayer(game, user);
        gamePlayerRepository.save(gamePlayer);

        if (game.getCapacity() == game.getPlayers().size()) {
            gameService.play(game);
        }
    }
}
