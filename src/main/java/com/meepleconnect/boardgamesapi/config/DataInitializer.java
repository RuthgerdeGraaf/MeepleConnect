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
        if (userRepository.count() == 0) {
            initializeDefaultData();
        }

        if (publisherRepository.count() == 0) {
            initializePublishers();
        }

        if (boardgameRepository.count() == 0) {
            initializeBoardgames();
        }
    }

    private void initializeDefaultData() {
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

        System.out.println("✅ Standard users created: Ruthger & Edwin (password: password123)");
    }

    private void initializePublishers() {
        Publisher mayfair = new Publisher();
        mayfair.setName("Mayfair Games");
        mayfair.setCountryOfOrigin("USA");
        mayfair.setFounded(1980);
        mayfair.setIndie(false);
        publisherRepository.save(mayfair);

        Publisher daysOfWonder = new Publisher();
        daysOfWonder.setName("Days of Wonder");
        daysOfWonder.setCountryOfOrigin("France");
        daysOfWonder.setFounded(2002);
        daysOfWonder.setIndie(false);
        publisherRepository.save(daysOfWonder);

        Publisher zManGames = new Publisher();
        zManGames.setName("Z-Man Games");
        zManGames.setCountryOfOrigin("USA");
        zManGames.setFounded(1999);
        zManGames.setIndie(false);
        publisherRepository.save(zManGames);

        Publisher hasbro = new Publisher();
        hasbro.setName("Hasbro");
        hasbro.setCountryOfOrigin("USA");
        hasbro.setFounded(1923);
        hasbro.setIndie(false);
        publisherRepository.save(hasbro);

        Publisher chessHouse = new Publisher();
        chessHouse.setName("Chess House");
        chessHouse.setCountryOfOrigin("Netherlands");
        chessHouse.setFounded(1995);
        chessHouse.setIndie(true);
        publisherRepository.save(chessHouse);

        Publisher czechGames = new Publisher();
        czechGames.setName("Czech Games Edition");
        czechGames.setCountryOfOrigin("Czech Republic");
        czechGames.setFounded(2007);
        czechGames.setIndie(true);
        publisherRepository.save(czechGames);

        Publisher fantasyFlight = new Publisher();
        fantasyFlight.setName("Fantasy Flight Games");
        fantasyFlight.setCountryOfOrigin("USA");
        fantasyFlight.setFounded(1995);
        fantasyFlight.setIndie(false);
        publisherRepository.save(fantasyFlight);

        System.out.println(
                "✅ Publishers created: Mayfair Games, Days of Wonder, Z-Man Games, Hasbro, Chess House, Czech Games Edition, Fantasy Flight Games");
    }

    private void initializeBoardgames() {
        List<Publisher> publishers = publisherRepository.findAll();
        if (publishers.isEmpty()) {
            System.out.println("⚠️ No publishers found. Create publishers first.");
            return;
        }

        Publisher mayfair = publishers.stream().filter(p -> p.getName().equals("Mayfair Games")).findFirst()
                .orElse(publishers.get(0));
        Publisher daysOfWonder = publishers.stream().filter(p -> p.getName().equals("Days of Wonder")).findFirst()
                .orElse(publishers.get(1));
        Publisher zManGames = publishers.stream().filter(p -> p.getName().equals("Z-Man Games")).findFirst()
                .orElse(publishers.get(2));
        Publisher hasbro = publishers.stream().filter(p -> p.getName().equals("Hasbro")).findFirst()
                .orElse(publishers.get(3));
        Publisher chessHouse = publishers.stream().filter(p -> p.getName().equals("Chess House")).findFirst()
                .orElse(publishers.get(4));
        Publisher czechGames = publishers.stream().filter(p -> p.getName().equals("Czech Games Edition")).findFirst()
                .orElse(publishers.get(5));
        Publisher fantasyFlight = publishers.stream().filter(p -> p.getName().equals("Fantasy Flight Games"))
                .findFirst().orElse(publishers.get(6));

        Boardgame catan = new Boardgame();
        catan.setName("Catan");
        catan.setPrice(new BigDecimal("39.99"));
        catan.setAvailable(true);
        catan.setMinPlayers(3);
        catan.setMaxPlayers(4);
        catan.setGenre("Strategy");
        catan.setPublisher(mayfair);
        boardgameRepository.save(catan);

        Boardgame ticketToRide = new Boardgame();
        ticketToRide.setName("Ticket to Ride");
        ticketToRide.setPrice(new BigDecimal("49.99"));
        ticketToRide.setAvailable(true);
        ticketToRide.setMinPlayers(2);
        ticketToRide.setMaxPlayers(5);
        ticketToRide.setGenre("Family");
        ticketToRide.setPublisher(daysOfWonder);
        boardgameRepository.save(ticketToRide);

        Boardgame pandemic = new Boardgame();
        pandemic.setName("Pandemic");
        pandemic.setPrice(new BigDecimal("44.99"));
        pandemic.setAvailable(true);
        pandemic.setMinPlayers(2);
        pandemic.setMaxPlayers(4);
        pandemic.setGenre("Cooperative");
        pandemic.setPublisher(zManGames);
        boardgameRepository.save(pandemic);

        Boardgame monopoly = new Boardgame();
        monopoly.setName("Monopoly");
        monopoly.setPrice(new BigDecimal("29.99"));
        monopoly.setAvailable(false);
        monopoly.setMinPlayers(2);
        monopoly.setMaxPlayers(8);
        monopoly.setGenre("Family");
        monopoly.setPublisher(hasbro);
        boardgameRepository.save(monopoly);

        Boardgame chess = new Boardgame();
        chess.setName("Chess");
        chess.setPrice(new BigDecimal("19.99"));
        chess.setAvailable(true);
        chess.setMinPlayers(2);
        chess.setMaxPlayers(2);
        chess.setGenre("Strategy");
        chess.setPublisher(chessHouse);
        boardgameRepository.save(chess);

        Boardgame codenames = new Boardgame();
        codenames.setName("Codenames");
        codenames.setPrice(new BigDecimal("24.99"));
        codenames.setAvailable(true);
        codenames.setMinPlayers(2);
        codenames.setMaxPlayers(8);
        codenames.setGenre("Party");
        codenames.setPublisher(czechGames);
        boardgameRepository.save(codenames);

        Boardgame starWars = new Boardgame();
        starWars.setName("Star Wars: Rebellion");
        starWars.setPrice(new BigDecimal("89.99"));
        starWars.setAvailable(true);
        starWars.setMinPlayers(2);
        starWars.setMaxPlayers(4);
        starWars.setGenre("Strategy");
        starWars.setPublisher(fantasyFlight);
        boardgameRepository.save(starWars);

        Boardgame settlers = new Boardgame();
        settlers.setName("The Settlers of Catan");
        settlers.setPrice(new BigDecimal("42.99"));
        settlers.setAvailable(true);
        settlers.setMinPlayers(3);
        settlers.setMaxPlayers(4);
        settlers.setGenre("Strategy");
        settlers.setPublisher(mayfair);
        boardgameRepository.save(settlers);

        System.out.println(
                "✅ Standard boardgames created: Catan, Ticket to Ride, Pandemic, Monopoly, Chess, Codenames, Star Wars: Rebellion, The Settlers of Catan");
    }
}