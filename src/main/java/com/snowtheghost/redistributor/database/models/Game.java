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

    public Game(String gameId, Integer capacity) {
        this.gameId = gameId;
        this.capacity = capacity;
    }

    @Id
    private String gameId;

    private Integer capacity;

    @OneToMany(mappedBy = "game")
    private List<GamePlayer> players = new ArrayList<>();
}