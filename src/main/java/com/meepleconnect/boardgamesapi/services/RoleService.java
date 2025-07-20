package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findRolesByNames(String... roleNames) {
        List<String> names = Arrays.asList(roleNames);
        return roleRepository.findByRoleNameIn(names);
    }

    public Role findDefaultUserRole() {
        List<Role> roles = roleRepository.findByRoleNameIn(Arrays.asList("ROLE_USER"));
        if (roles.isEmpty()) {
            Role defaultRole = new Role();
            defaultRole.setRoleName("ROLE_USER");
            defaultRole.setActive(true);
            defaultRole.setDescription("Default user role");
            return roleRepository.save(defaultRole);
        }
        return roles.get(0);
    }
} 