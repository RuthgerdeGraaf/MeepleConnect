package com.meepleconnect.boardgamesapi.config;

import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.models.Boardgame;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.BoardgameRepository;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
import com.meepleconnect.boardgamesapi.repositories.RoleRepository;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PublisherRepository publisherRepository;
    private final BoardgameRepository boardgameRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
            PublisherRepository publisherRepository, BoardgameRepository boardgameRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.publisherRepository = publisherRepository;
        this.boardgameRepository = boardgameRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Controleer of er al gebruikers bestaan
        if (userRepository.count() == 0) {
            initializeDefaultData();
        }

        // Controleer of er al boardgames bestaan
        if (boardgameRepository.count() == 0) {
            initializeBoardgames();
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

    private void initializeBoardgames() {
        // Haal bestaande publishers op
        List<Publisher> publishers = publisherRepository.findAll();
        if (publishers.isEmpty()) {
            System.out.println("⚠️ Geen publishers gevonden. Maak eerst publishers aan.");
            return;
        }

        // Maak enkele populaire boardgames aan
        Boardgame catan = new Boardgame();
        catan.setName("Catan");
        catan.setPrice(new BigDecimal("39.99"));
        catan.setAvailable(true);
        catan.setMinPlayers(3);
        catan.setMaxPlayers(4);
        catan.setGenre("Strategy");
        catan.setPublisher(publishers.get(0)); // Eerste publisher
        boardgameRepository.save(catan);

        Boardgame ticketToRide = new Boardgame();
        ticketToRide.setName("Ticket to Ride");
        ticketToRide.setPrice(new BigDecimal("49.99"));
        ticketToRide.setAvailable(true);
        ticketToRide.setMinPlayers(2);
        ticketToRide.setMaxPlayers(5);
        ticketToRide.setGenre("Family");
        ticketToRide.setPublisher(publishers.get(1)); // Tweede publisher
        boardgameRepository.save(ticketToRide);

        Boardgame pandemic = new Boardgame();
        pandemic.setName("Pandemic");
        pandemic.setPrice(new BigDecimal("44.99"));
        pandemic.setAvailable(true);
        pandemic.setMinPlayers(2);
        pandemic.setMaxPlayers(4);
        pandemic.setGenre("Cooperative");
        pandemic.setPublisher(publishers.get(2)); // Derde publisher
        boardgameRepository.save(pandemic);

        Boardgame monopoly = new Boardgame();
        monopoly.setName("Monopoly");
        monopoly.setPrice(new BigDecimal("29.99"));
        monopoly.setAvailable(false);
        monopoly.setMinPlayers(2);
        monopoly.setMaxPlayers(8);
        monopoly.setGenre("Family");
        monopoly.setPublisher(publishers.get(3)); // Vierde publisher
        boardgameRepository.save(monopoly);

        Boardgame chess = new Boardgame();
        chess.setName("Chess");
        chess.setPrice(new BigDecimal("19.99"));
        chess.setAvailable(true);
        chess.setMinPlayers(2);
        chess.setMaxPlayers(2);
        chess.setGenre("Strategy");
        chess.setPublisher(publishers.get(4)); // Vijfde publisher
        boardgameRepository.save(chess);

        System.out.println("✅ Standaard boardgames aangemaakt: Catan, Ticket to Ride, Pandemic, Monopoly, Chess");
    }
}