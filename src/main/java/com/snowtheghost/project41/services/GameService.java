package com.snowtheghost.project41.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowtheghost.project41.api.models.responses.games.GameResponse;
import com.snowtheghost.project41.database.models.Game;
import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.database.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class GameService {

    @Value("${python.gameservice.path}")
    private String gameServicePath;
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

    public GameResponse startGame(User user, String gameType, String player1_type, String player2_type) {
        GameResponse response;
        String gameId = UUID.randomUUID().toString();
        //System.out.println("python " + gameServicePath + " -game_id " + gameId + " -game_type " + gameType + " -player1_type " + player1_type + " -player2_type " + player2_type);
        ProcessBuilder pb = new ProcessBuilder("python", gameServicePath, "-game_id", gameId, "-game_type", gameType, "-player1_type", player1_type, "-player2_type", player2_type);

        try {
            pb.redirectErrorStream(true);
            Process process = pb.start();
            System.out.println("Starting game");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            String jsonString = output.toString().strip();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Game started successfully");
                System.out.println("JSON: " + jsonString);
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(jsonString, GameResponse.class);
            } else {
                System.out.println("Error starting game");
                System.out.println("JSON: " + jsonString);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        user.setCurrentGameId(gameId);
        return response;
    }

    public GameResponse makeMove(User user, String gameId, String move) {
        GameResponse response;
        ProcessBuilder pb = new ProcessBuilder("python", gameServicePath, "-game_id", gameId, "-make_move", move, "-player_move", "player1");

        try {
            pb.redirectErrorStream(true);
            Process process = pb.start();
            System.out.println("Making move");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            String jsonString = output.toString().strip();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Move made successfully");
                System.out.println("JSON: " + jsonString);
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(jsonString, GameResponse.class);
            } else {
                System.out.println("Error making move");
                System.out.println("JSON: " + jsonString);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        return response;
    }

    public GameResponse quitGame(User user, String gameId) {
        GameResponse response;
        ProcessBuilder pb = new ProcessBuilder("python", gameServicePath, "-game_id", gameId, "-delete_game");

        try {
            pb.redirectErrorStream(true);
            Process process = pb.start();
            System.out.println("Deleting game");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            String jsonString = output.toString().strip();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Game deleted successfully");
                System.out.println("JSON: " + jsonString);
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(jsonString, GameResponse.class);
            } else {
                System.out.println("Error deleting game");
                System.out.println("JSON: " + jsonString);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        user.setCurrentGameId(null);
        return response;
    }

    // public void createGame(Game game) {
    //     gameRepository.save(game);
    // }

    // public Game getGame(String gameId) {
    //     return gameRepository.getReferenceById(gameId);
    // }

    // public List<Game> getGames(Integer capacity, Integer cost, Game.Type type, Game.State state) {
    //     ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues();
    //     Example<Game> example = Example.of(new Game(null, capacity, cost, type, state), exampleMatcher);
    //     return gameRepository.findAll(example);
    // }

    // public void play(Game game) {
    //     game.setState(Game.State.PENDING_RESULTS);
    //     Map<String, Integer> results = generateResults(game);
    //     gameWinnerService.addWinners(game, results);

    //     for (String winnerId : results.keySet()) {
    //         int amount = results.get(winnerId);
    //         if (amount > 0) {
    //             userService.addFunds(winnerId, amount);
    //         }
    //     }

    //     game.setState(Game.State.COMPLETED);
    //     gameRepository.save(game);
    // }

    // Map<String, Integer> generateResults(Game game) {
    //     HashMap<String, Integer> results = new HashMap<>();
    //     if (game.getType() == Game.Type.ROYALE) {
    //         results.put(game.getPlayerIds().get(random.nextInt(game.getCapacity())), game.getCapacity() * game.getCost());
    //     } else {
    //         int remaining = game.getCapacity();
    //         while (remaining > 0) {
    //             String username = game.getPlayerIds().get(random.nextInt(game.getCapacity()));
    //             results.merge(username, game.getCost(), Integer::sum);
    //             remaining -= 1;
    //         }
    //     }

    //     return results;
    // }
}
