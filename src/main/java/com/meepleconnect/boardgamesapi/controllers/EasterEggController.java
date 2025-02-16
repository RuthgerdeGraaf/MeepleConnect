package com.meepleconnect.boardgamesapi.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fun")
public class EasterEggController {

    @GetMapping("/teapot")
    public ResponseEntity<Map<String, String>> teapot() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "I'm a teapot! 🍵 This server refuses to brew coffee.");
        response.put("imageUrl", "https://httpcats.com/418.jpg");

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(response);
    }
}
