package com.meepleconnect.boardgamesapi.config;

import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.repositories.RoleRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Controleer of er al gebruikers bestaan
        if (userRepository.count() == 0) {
            initializeDefaultData();
        }
    }

    private void initializeDefaultData() {
        // Maak rollen aan
        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        adminRole.setActive(true);
        adminRole.setDescription("administrator roles");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        userRole.setActive(true);
        userRole.setDescription("user roles");
        roleRepository.save(userRole);

        // Maak standaard gebruikers aan
        User ruthger = new User();
        ruthger.setUserName("Ruthger");
        ruthger.setPassword(passwordEncoder.encode("password123"));
        ruthger.setEnabled(true);
        ruthger.setExpired(false);
        ruthger.setLocked(false);
        ruthger.setAreCredentialsExpired(false);
        ruthger.setRoles(Arrays.asList(adminRole, userRole));
        userRepository.save(ruthger);

        User edwin = new User();
        edwin.setUserName("Edwin");
        edwin.setPassword(passwordEncoder.encode("password123"));
        edwin.setEnabled(true);
        edwin.setExpired(false);
        edwin.setLocked(false);
        edwin.setAreCredentialsExpired(false);
        edwin.setRoles(Arrays.asList(userRole));
        userRepository.save(edwin);

        System.out.println("✅ Standaard gebruikers aangemaakt: Ruthger & Edwin (wachtwoord: password123)");
    }
}