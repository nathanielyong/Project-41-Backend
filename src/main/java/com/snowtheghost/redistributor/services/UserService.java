package com.snowtheghost.redistributor.services;

import com.snowtheghost.redistributor.database.models.User;
import com.snowtheghost.redistributor.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUser(String userId) {
        return userRepository.getReferenceById(userId);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isValidCredentials(User user, String email, String password) {
        return user.getEmail().equals(email) && user.getEncryptedPassword().equals(encryptPassword(password));
    }

    public String encryptPassword(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException();
        }

        final byte[] bytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(bytes);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
