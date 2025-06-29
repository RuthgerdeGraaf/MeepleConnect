package com.meepleconnect.boardgamesapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    // private Authentication authentication;

    @GetMapping("/secure")
    public ResponseEntity<String> getSecureData() {
        return ResponseEntity.ok("This is secure data: ");
    }

    @GetMapping("/secure/admin")
    public ResponseEntity<String> getAdminData() {
        return ResponseEntity.ok("This is secure admin data: ");
    }

    @GetMapping("/secure/user")
    public ResponseEntity<String> getUserData() {
        return ResponseEntity.ok("This is secure user data: ");
    }
}