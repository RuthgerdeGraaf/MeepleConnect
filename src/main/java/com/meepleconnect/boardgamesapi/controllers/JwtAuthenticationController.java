package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.ConflictException;
import com.meepleconnect.boardgamesapi.security.JwtRequest;
import com.meepleconnect.boardgamesapi.security.JwtResponse;
import com.meepleconnect.boardgamesapi.security.JwtUtil;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import com.meepleconnect.boardgamesapi.repositories.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserDetailsService userDetailsService, UserRepository userRepository,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");

            if (username == null || username.trim().isEmpty()) {
                throw new BadRequestException("Username cannot be null or empty");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new BadRequestException("Password cannot be null or empty");
            }

            // Check if user already exists
            Optional<User> existingUser = userRepository.findByUserName(username);
            if (existingUser.isPresent()) {
                throw new ConflictException("Username already exists");
            }

            // Find or create USER role
            Role userRole = roleRepository.findByRoleNameIn(Arrays.asList("ROLE_USER"))
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleName("ROLE_USER");
                        role.setActive(true);
                        role.setDescription("Default user role");
                        return roleRepository.save(role);
                    });

            // Create new user
            User user = new User();
            user.setUserName(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            user.setExpired(false);
            user.setLocked(false);
            user.setAreCredentialsExpired(false);
            user.setRoles(Arrays.asList(userRole));

            User savedUser = userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id", savedUser.getId(),
                    "username", savedUser.getUserName(),
                    "message", "User registered successfully"));

        } catch (BadRequestException | ConflictException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
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
