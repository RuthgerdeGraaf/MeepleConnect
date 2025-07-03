package com.meepleconnect.boardgamesapi.dtos;

import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    @Mapping(target = "userName", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    User mapToModel(UserRequestDTO userDTO);

    @Named("mapRoles")
    default List<Role> mapRoles(String[] roles) {
        if (roles == null || roles.length == 0) {
            Role role = new Role();
            role.setRoleName("USER");
            role.setActive(true);
            role.setDescription("Default user role");
            List<Role> roleList = new ArrayList<>();
            roleList.add(role);
            return roleList;
        }

        try {
            String roleName = roles[0].toUpperCase();
            Role role = new Role();
            role.setRoleName(roleName);
            role.setActive(true);
            role.setDescription("User role");
            List<Role> roleList = new ArrayList<>();
            roleList.add(role);
            return roleList;
        } catch (IllegalArgumentException e) {
            Role role = new Role();
            role.setRoleName("USER");
            role.setActive(true);
            role.setDescription("Default user role");
            List<Role> roleList = new ArrayList<>();
            roleList.add(role);
            return roleList;
        }
    }
}