package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.database.models.Game;
import com.snowtheghost.redistributor.database.models.GamePlayer;
import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.database.repositories.GamePlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamePlayerService {
    private final GamePlayerRepository gamePlayerRepository;

    @Autowired
    public GamePlayerService(GamePlayerRepository gamePlayerRepository) {
        this.gamePlayerRepository = gamePlayerRepository;
    }

    public GamePlayer joinGame(Game game, User user) {
        GamePlayer gamePlayer = new GamePlayer(game, user);
        return gamePlayerRepository.save(gamePlayer);
    }
}
