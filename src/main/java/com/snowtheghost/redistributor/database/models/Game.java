package com.snowtheghost.redistributor.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Game {

    public Game(String gameId, Integer capacity, Integer cost, Type type) {
        this.gameId = gameId;
        this.capacity = capacity;
        this.cost = cost;
        this.type = type;
        this.state = State.PENDING_PLAYERS;
    }

    @Id
    private final String gameId;

    private final Integer capacity;

    private final Integer cost;

    private final Type type;

    private final State state;

    @OneToMany(mappedBy = "game")
    private final List<GamePlayer> players = new ArrayList<>();

    public enum Type {

        ROYALE,
        REDISTRIBUTE,
    }

    public enum State {

        PENDING_PLAYERS,
        PENDING_START,
        PENDING_RESULTS,
        COMPLETED,
    }

    public List<String> getPlayerEmails() {
        return players.stream().map(gamePlayer -> gamePlayer.getUser().getEmail()).collect(Collectors.toList());
    }
}
