package com.snowtheghost.redistributor.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    public User(String userId, String username, String email, String encryptedPassword) {
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
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

    @OneToMany(mappedBy = "user")
    private List<GamePlayer> games = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<GameWinner> winners = new ArrayList<>();
}
