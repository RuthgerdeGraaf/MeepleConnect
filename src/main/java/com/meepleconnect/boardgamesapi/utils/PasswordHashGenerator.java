package com.meepleconnect.boardgamesapi.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String[] passwords = { "password123", "testpass", "admin123", "user123" };

        System.out.println("=== BCrypt Password Hashes ===");
        System.out.println("Use these hashes in your data.sql file:");
        System.out.println();

        for (String password : passwords) {
            String hash = encoder.encode(password);
            System.out.println("Password: '" + password + "'");
            System.out.println("Hash: '" + hash + "'");
            System.out.println();
        }

        System.out.println("=== Example data.sql INSERT ===");
        System.out.println("INSERT INTO users (username, password, role) VALUES");
        System.out.println("    ('admin', '" + encoder.encode("password123") + "', 'ADMIN'),");
        System.out.println("    ('user', '" + encoder.encode("testpass") + "', 'USER'),");
        System.out.println("    ('employee', '" + encoder.encode("password123") + "', 'EMPLOYEE');");
    }
}