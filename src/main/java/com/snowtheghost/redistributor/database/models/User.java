package com.snowtheghost.redistributor.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    public User(String userId, String email, String encryptedPassword) {
        this.userId = userId;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }

    @Id
    private String userId;

    @Column(unique = true)
    private String email;

    private String encryptedPassword;

    @OneToMany(mappedBy = "user")
    private List<GamePlayer> games = new ArrayList<>();
}
