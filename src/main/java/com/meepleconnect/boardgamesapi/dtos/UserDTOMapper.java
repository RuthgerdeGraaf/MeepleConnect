package com.meepleconnect.boardgamesapi.dtos;

import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.entities.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDTOMapper {

    public User mapToModel(UserRequestDTO userDTO) {
        var result = new User();
        result.setUserName(userDTO.getUsername());
        result.setPassword(userDTO.getPassword());

        // Set the first role from the roles array (assuming single role for now)
        if (userDTO.getRoles() != null && userDTO.getRoles().length > 0) {
            try {
                String roleName = userDTO.getRoles()[0].toUpperCase();
                Role role = new Role();
                role.setRoleName(roleName);
                role.setActive(true);
                role.setDescription("User role");

                List<Role> roles = new ArrayList<>();
                roles.add(role);
                result.setRoles(roles);
            } catch (IllegalArgumentException e) {
                // Default to USER role if invalid role provided
                Role role = new Role();
                role.setRoleName("USER");
                role.setActive(true);
                role.setDescription("Default user role");

                List<Role> roles = new ArrayList<>();
                roles.add(role);
                result.setRoles(roles);
            }
        } else {
            Role role = new Role();
            role.setRoleName("USER");
            role.setActive(true);
            role.setDescription("Default user role");

            List<Role> roles = new ArrayList<>();
            roles.add(role);
            result.setRoles(roles);
        }

        return result;
    }

    public User mapToModel(UserChangePasswordRequestDTO userDTO, Long id) {
        var result = new User(id);
        result.setPassword(userDTO.getPassword());
        return result;
    }
}