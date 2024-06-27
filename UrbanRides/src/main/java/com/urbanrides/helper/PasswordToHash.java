package com.urbanrides.helper;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordToHash {

    private static final int SALT_LENGTH = 16;

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String plainPassword, String salt) {
        return BCrypt.hashpw(plainPassword + salt, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String salt, String hashedPassword) {
        return BCrypt.checkpw(plainPassword + salt, hashedPassword);
    }
}