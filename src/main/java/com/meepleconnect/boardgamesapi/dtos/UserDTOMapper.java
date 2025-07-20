package com.meepleconnect.boardgamesapi.dtos;

import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.services.RoleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserDTOMapper {

    private final RoleService roleService;

    public UserDTOMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public User mapToModel(UserRequestDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRoles(mapRoles(userDTO.getRoles()));
        return user;
    }

    public UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setRoles(user.getRoles().stream()
                .map(this::mapToRoleResponseDTO)
                .toList());
        return dto;
    }

    public RoleResponseDTO mapToRoleResponseDTO(Role role) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        dto.setActive(role.isActive());
        dto.setDescription(role.getDescription());
        return dto;
    }

    private List<Role> mapRoles(String[] roles) {
        if (roles == null || roles.length == 0) {
            return Arrays.asList(roleService.findDefaultUserRole());
        }

        try {
            String roleName = "ROLE_" + roles[0].toUpperCase();
            List<Role> foundRoles = roleService.findRolesByNames(roleName);
            if (!foundRoles.isEmpty()) {
                return Arrays.asList(foundRoles.get(0));
            }
            return Arrays.asList(roleService.findDefaultUserRole());
        } catch (Exception e) {
            return Arrays.asList(roleService.findDefaultUserRole());
        }
    }
}