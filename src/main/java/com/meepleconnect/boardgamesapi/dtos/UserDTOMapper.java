package com.meepleconnect.boardgamesapi.dtos;

import com.meepleconnect.boardgamesapi.models.User;
import com.meepleconnect.boardgamesapi.models.Role;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public User mapToModel(UserRequestDTO userDTO) {
        var result = new User();
        result.setUsername(userDTO.getUsername());
        result.setPassword(userDTO.getPassword());

        // Set the first role from the roles array (assuming single role for now)
        if (userDTO.getRoles() != null && userDTO.getRoles().length > 0) {
            try {
                Role role = Role.valueOf(userDTO.getRoles()[0].toUpperCase());
                result.setRole(role);
            } catch (IllegalArgumentException e) {
                // Default to USER role if invalid role provided
                result.setRole(Role.USER);
            }
        } else {
            result.setRole(Role.USER);
        }

        return result;
    }

    public User mapToModel(UserChangePasswordRequestDTO userDTO, Long id) {
        var result = new User();
        result.setId(id);
        result.setPassword(userDTO.getPassword());
        return result;
    }
}