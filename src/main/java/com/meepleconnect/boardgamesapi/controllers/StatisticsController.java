package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.services.BoardgameService;
import com.meepleconnect.boardgamesapi.services.ReservationService;
import com.meepleconnect.boardgamesapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final BoardgameService boardgameService;
    private final ReservationService reservationService;
    private final UserService userService;

    public StatisticsController(BoardgameService boardgameService,
            ReservationService reservationService,
            UserService userService) {
        this.boardgameService = boardgameService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        statistics.put("totalBoardgames", boardgameService.getTotalBoardgamesCount());
        statistics.put("availableBoardgames", boardgameService.getAvailableBoardgamesCount());
        statistics.put("totalReservations", reservationService.getTotalReservationsCount());
        statistics.put("totalUsers", userService.getTotalUsersCount());
        statistics.put("activeReservations", reservationService.getActiveReservationsCount());

        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/boardgames/popular")
    public ResponseEntity<Map<String, Object>> getPopularBoardgames() {
        Map<String, Object> popularGames = boardgameService.getPopularBoardgames();
        return ResponseEntity.ok(popularGames);
    }

    @GetMapping("/reservations/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyReservations(@RequestParam int year, @RequestParam int month) {
        Map<String, Object> monthlyStats = reservationService.getMonthlyReservations(year, month);
        return ResponseEntity.ok(monthlyStats);
    }
}