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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "data.initialization.enabled", havingValue = "true", matchIfMissing = true)
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
                // Always ensure correct users exist (regardless of existing data)
                initializeDefaultData();

                if (publisherRepository.count() == 0) {
                        initializePublishers();
                }

                if (boardgameRepository.count() == 0) {
                        initializeBoardgames();
                }
        }

        @Transactional
        private void initializeDefaultData() {
                // Only initialize if no users exist yet
                if (userRepository.count() > 0) {
                        System.out.println("✅ Users already exist, skipping user initialization...");
                        return;
                }
                System.out.println("🔄 Creating default users...");

                Role adminRole = roleRepository.findAll().stream()
                                .filter(r -> "ROLE_ADMIN".equals(r.getRoleName()))
                                .findFirst()
                                .orElseGet(() -> {
                                        Role role = new Role();
                                        role.setRoleName("ROLE_ADMIN");
                                        role.setActive(true);
                                        role.setDescription("administrator roles");
                                        return roleRepository.save(role);
                                });
                Role userRole = roleRepository.findAll().stream()
                                .filter(r -> "ROLE_USER".equals(r.getRoleName()))
                                .findFirst()
                                .orElseGet(() -> {
                                        Role role = new Role();
                                        role.setRoleName("ROLE_USER");
                                        role.setActive(true);
                                        role.setDescription("user roles");
                                        return roleRepository.save(role);
                                });

                adminRole = roleRepository.findById(adminRole.getId()).orElseThrow();
                userRole = roleRepository.findById(userRole.getId()).orElseThrow();

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
                Publisher daysOfWonder = publishers.stream().filter(p -> p.getName().equals("Days of Wonder"))
                                .findFirst()
                                .orElse(publishers.get(1));
                Publisher zManGames = publishers.stream().filter(p -> p.getName().equals("Z-Man Games")).findFirst()
                                .orElse(publishers.get(2));
                Publisher hasbro = publishers.stream().filter(p -> p.getName().equals("Hasbro")).findFirst()
                                .orElse(publishers.get(3));
                Publisher chessHouse = publishers.stream().filter(p -> p.getName().equals("Chess House")).findFirst()
                                .orElse(publishers.get(4));
                Publisher czechGames = publishers.stream().filter(p -> p.getName().equals("Czech Games Edition"))
                                .findFirst()
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

                Boardgame alchemists = new Boardgame();
                alchemists.setName("Alchemists");
                alchemists.setPrice(new BigDecimal("59.99"));
                alchemists.setAvailable(true);
                alchemists.setMinPlayers(2);
                alchemists.setMaxPlayers(4);
                alchemists.setGenre("Strategy");
                alchemists.setPublisher(czechGames);
                boardgameRepository.save(alchemists);

                Boardgame arkhamHorror = new Boardgame();
                arkhamHorror.setName("Arkham Horror");
                arkhamHorror.setPrice(new BigDecimal("69.99"));
                arkhamHorror.setAvailable(true);
                arkhamHorror.setMinPlayers(1);
                arkhamHorror.setMaxPlayers(8);
                arkhamHorror.setGenre("Adventure");
                arkhamHorror.setPublisher(fantasyFlight);
                boardgameRepository.save(arkhamHorror);

                System.out.println(
                                "✅ Boardgames created: Catan, Ticket to Ride, Pandemic, Monopoly, Chess, Alchemists, Arkham Horror");
        }
}