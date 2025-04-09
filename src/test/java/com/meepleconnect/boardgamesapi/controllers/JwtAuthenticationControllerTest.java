package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.security.JwtRequest;
import com.meepleconnect.boardgamesapi.security.JwtResponse;
import com.meepleconnect.boardgamesapi.security.JwtUtil;
import com.meepleconnect.boardgamesapi.models.User;
import com.meepleconnect.boardgamesapi.models.Role;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.findByUsername("testuser").ifPresent(userRepository::delete);

        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole(Role.USER);
        userRepository.save(testUser);
    }

    @Test
    void createAuthenticationToken_ValidCredentials_ShouldReturnJwt() throws Exception {
        JwtRequest request = new JwtRequest("testuser", "password");
        String jwtToken = "test.jwt.token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testuser", "password", Collections.emptyList()));
        
        when(jwtUtil.generateToken("testuser")).thenReturn(jwtToken);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken));
    }

    @Test
    void createAuthenticationToken_InvalidCredentials_ShouldReturn401() throws Exception {
        JwtRequest request = new JwtRequest("testuser", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
} 