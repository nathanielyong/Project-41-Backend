package com.snowtheghost.project41.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    public User(@NotNull String userId, @NotNull String username, @NotNull String email, @NotNull String encryptedPassword, @NotNull String encryptedBalance, @NotNull String connectedAccountId, @NotNull String currentGameId) {
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.encryptedBalance = encryptedBalance;
        this.connectedAccountId = connectedAccountId;
        this.currentGameId = currentGameId;
    }

    @Id
    @NotNull(message = "User ID is required.")
    private String userId;

    @Column(unique = true)
    @NotNull(message = "Username is required.")
    private String username;

    @Column(unique = true)
    @NotNull(message = "Email is required.")
    private String email;

    @NotNull(message = "Encrypted password is required.")
    private String encryptedPassword;

    @NotNull
    private String currentGameId;

    @OneToMany(mappedBy = "user")
    private List<GamePlayer> games = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<GameWinner> winners = new ArrayList<>();

    @NotNull(message = "Balance is required")
    private String encryptedBalance;

    @NotNull(message = "Connected account ID is required")
    private String connectedAccountId;
}
