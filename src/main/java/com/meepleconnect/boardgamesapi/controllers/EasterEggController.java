package com.meepleconnect.boardgamesapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meepleconnect.boardgamesapi.exceptions.TeapotException;

@RestController
@RequestMapping("/api/fun")
public class EasterEggController {

    @GetMapping("/teapot")
    public ResponseEntity<String> getTeapot() {
        throw new TeapotException("I'm a teapot! 🍵 This server refuses to brew coffee.");
    }
}
