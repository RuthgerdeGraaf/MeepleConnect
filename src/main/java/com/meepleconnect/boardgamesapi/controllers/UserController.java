package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.dtos.UserDTOMapper;
import com.meepleconnect.boardgamesapi.dtos.UserRequestDTO;
import com.meepleconnect.boardgamesapi.dtos.UserResponseDTO;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    public UserController(UserService userService, UserDTOMapper userDTOMapper) {
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOs = users.stream()
                .map(userDTOMapper::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponseDTO userResponseDTO = userDTOMapper.mapToResponseDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = userDTOMapper.mapToModel(userRequestDTO);
        User registeredUser = userService.registerUser(user);
        UserResponseDTO userResponseDTO = userDTOMapper.mapToResponseDTO(registeredUser);
        URI location = URI.create("/api/users/" + registeredUser.getId());
        return ResponseEntity.created(location).body(userResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = userDTOMapper.mapToModel(userRequestDTO);
        User updatedUser = userService.updateUser(id, user);
        UserResponseDTO userResponseDTO = userDTOMapper.mapToResponseDTO(updatedUser);
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
