package com.snowtheghost.redistributor.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Game {

    public Game(String gameId, Integer capacity, Integer cost, Type type, State state) {
        this.gameId = gameId;
        this.capacity = capacity;
        this.cost = cost;
        this.type = type;
        this.state = state;
    }

    @Id
    private final String gameId;

    private final Integer capacity;

    @OneToMany(mappedBy = "game")
    private final List<GamePlayer> players = new ArrayList<>();

    private final Integer cost;

    private final Type type;

    private final State state;

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
}
