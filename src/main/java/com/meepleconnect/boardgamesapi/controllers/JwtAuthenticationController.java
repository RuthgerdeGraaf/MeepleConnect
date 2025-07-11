package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.security.JwtRequest;
import com.meepleconnect.boardgamesapi.security.JwtResponse;
import com.meepleconnect.boardgamesapi.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        // Input validation
        if (authenticationRequest == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        if (authenticationRequest.getUsername() == null || authenticationRequest.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username cannot be null or empty");
        }

        if (authenticationRequest.getPassword() == null || authenticationRequest.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password cannot be null or empty");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

}
