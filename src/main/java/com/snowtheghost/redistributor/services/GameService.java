package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.database.models.Game;
import com.snowtheghost.redistributor.database.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    public Game getGame(String gameId) {
        return gameRepository.getReferenceById(gameId);
    }

    public List<Game> getGames(Integer capacity, Integer cost, Game.Type type, Game.State state) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Game> example = Example.of(new Game(null, capacity, cost, type, state), exampleMatcher);
        return gameRepository.findAll(example);
    }
}
