package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.models.Game;
import com.snowtheghost.project41.database.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameWinnerService gameWinnerService;
    private final UserService userService;
    private final Random random = new Random();

    @Autowired
    public GameService(GameRepository gameRepository, GameWinnerService gameWinnerService, UserService userService) {
        this.gameRepository = gameRepository;
        this.gameWinnerService = gameWinnerService;
        this.userService = userService;
    }

    public void createGame(Game game) {
        gameRepository.save(game);
    }

    public Game getGame(String gameId) {
        return gameRepository.getReferenceById(gameId);
    }

    public List<Game> getGames(Integer capacity, Integer cost, Game.Type type, Game.State state) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Game> example = Example.of(new Game(null, capacity, cost, type, state), exampleMatcher);
        return gameRepository.findAll(example);
    }

    public void play(Game game) {
        game.setState(Game.State.PENDING_RESULTS);
        Map<String, Integer> results = generateResults(game);
        gameWinnerService.addWinners(game, results);

        for (String winnerId : results.keySet()) {
            int amount = results.get(winnerId);
            if (amount > 0) {
                userService.addFunds(winnerId, amount);
            }
        }

        game.setState(Game.State.COMPLETED);
        gameRepository.save(game);
    }

    Map<String, Integer> generateResults(Game game) {
        HashMap<String, Integer> results = new HashMap<>();
        if (game.getType() == Game.Type.ROYALE) {
            results.put(game.getPlayerIds().get(random.nextInt(game.getCapacity())), game.getCapacity() * game.getCost());
        } else {
            int remaining = game.getCapacity();
            while (remaining > 0) {
                String username = game.getPlayerIds().get(random.nextInt(game.getCapacity()));
                results.merge(username, game.getCost(), Integer::sum);
                remaining -= 1;
            }
        }

        return results;
    }
}
