package com.snowtheghost.project41.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Game {

    public Game(@NotNull String gameId, @NotNull Integer capacity, @NotNull Integer cost, @NotNull Type type) {
        this.gameId = gameId;
        this.capacity = capacity;
        this.cost = cost;
        this.type = type;
        this.state = State.PENDING_PLAYERS;
    }

    @Id
    @NotNull(message = "Game ID is required.")
    private String gameId;

    @NotNull(message = "Capacity is required.")
    private Integer capacity;

    @NotNull(message = "Cost is required.")
    private Integer cost;

    @NotNull(message = "Type is required.")
    private Type type;

    @NotNull(message = "State is required.")
    private State state;

    @OneToMany(mappedBy = "game")
    private List<GamePlayer> players = new ArrayList<>();

    @OneToMany(mappedBy = "game")
    private List<GameWinner> winners = new ArrayList<>();

    public enum Type {

        ROYALE,
        REDISTRIBUTE,
    }

    public enum State {

        PENDING_PLAYERS,
        PENDING_RESULTS,
        COMPLETED,
    }

    public List<String> getPlayerUsernames() {
        return players.stream().map(gamePlayer -> gamePlayer.getUser().getUsername()).collect(Collectors.toList());
    }

    public List<String> getPlayerIds() {
        return players.stream().map(gamePlayer -> gamePlayer.getUser().getUserId()).collect(Collectors.toList());
    }

    public Map<String, Integer> getWinnerUsernamesToEarnings() {
        if (state == State.COMPLETED) {
            HashMap<String, Integer> results = new HashMap<>();
            for (GameWinner gameWinner : winners) {
                results.put(gameWinner.getUser().getUsername(), gameWinner.getEarnings());
            }
            return results;
        }
        return null;
    }
}
