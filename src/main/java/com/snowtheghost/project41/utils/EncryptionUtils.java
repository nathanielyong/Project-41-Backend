package com.snowtheghost.project41.utils;

import com.snowtheghost.project41.configuration.EncryptionConfiguration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptionUtils {

    private final int balanceEncryptionKey;

    public EncryptionUtils(EncryptionConfiguration encryptionConfiguration) {
        this.balanceEncryptionKey = encryptionConfiguration.getBalanceEncryptionKey();
    }

    public String encryptBalance(int value) {
        int encryptedValue = value ^ balanceEncryptionKey;
        return String.valueOf(encryptedValue);
    }

    public Integer decryptBalance(String encryptedValue) {
        int intValue = Integer.parseInt(encryptedValue);
        return intValue ^ balanceEncryptionKey;
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
