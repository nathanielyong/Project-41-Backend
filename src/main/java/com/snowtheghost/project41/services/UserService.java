package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.database.repositories.UserRepository;
import com.snowtheghost.project41.utils.EncryptionUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EncryptionUtils encryptionUtils;

    @Autowired
    public UserService(UserRepository userRepository, EncryptionUtils encryptionUtils) {
        this.userRepository = userRepository;
        this.encryptionUtils = encryptionUtils;
    }

    public void createUser(String userId, String username, String email, String password, String connectedAccountId) {
        User user = new User(userId, username, email, encryptionUtils.encryptPassword(password), encryptionUtils.encryptBalance(0), connectedAccountId, "");
        userRepository.save(user);
    }

    public User getUser(String userId) throws EntityNotFoundException {
        return userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    public boolean hasSufficientFunds(User user, Integer amount) {
        int balance = encryptionUtils.decryptBalance(user.getEncryptedBalance());
        return balance > 0 && balance - amount >= 0;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isValidCredentials(User user, String email, String password) {
        return user.getEmail().equals(email) && user.getEncryptedPassword().equals(encryptionUtils.encryptPassword(password));
    }

    public void addFunds(String userId, int amount) {
        User user = getUser(userId);
        user.setEncryptedBalance(encryptionUtils.encryptBalance(encryptionUtils.decryptBalance(user.getEncryptedBalance()) + amount));
        userRepository.save(user);
    }

    public void subtractFunds(User user, int amount) {
        user.setEncryptedBalance(encryptionUtils.encryptBalance(encryptionUtils.decryptBalance(user.getEncryptedBalance()) - amount));
        userRepository.save(user);
    }

    public void updateId(User user, String gameId) {
        user.setCurrentGameId(gameId);
        userRepository.save(user);
    }

    public float getBalance(User user) {
        return (float) encryptionUtils.decryptBalance(user.getEncryptedBalance()) / 100;
    }
}
