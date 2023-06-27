package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.database.repositories.UserRepository;
import com.snowtheghost.redistributor.utils.EncryptionUtils;
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

    public void createUser(String userId, String username, String email, String password) {
        User user = new User(userId, username, email, encryptionUtils.encryptPassword(password), encryptionUtils.encryptBalance(0));
        userRepository.save(user);
    }

    public User getUser(String userId) {
        return userRepository.getReferenceById(userId);
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

    public float getBalance(User user) {
        return (float) encryptionUtils.decryptBalance(user.getEncryptedBalance()) / 100;
    }
}
