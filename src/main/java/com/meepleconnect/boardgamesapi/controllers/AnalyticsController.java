package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.services.BoardgameService;
import com.meepleconnect.boardgamesapi.services.ReservationService;
import com.meepleconnect.boardgamesapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final BoardgameService boardgameService;
    private final ReservationService reservationService;
    private final UserService userService;

    public AnalyticsController(BoardgameService boardgameService,
            ReservationService reservationService,
            UserService userService) {
        this.boardgameService = boardgameService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping("/revenue/forecast")
    public ResponseEntity<Map<String, Object>> getRevenueForecast(@RequestParam int months) {
        Map<String, Object> forecast = new HashMap<>();

        double baseRevenue = 50.0;
        double growthRate = 1.1;

        Map<String, Double> monthlyForecast = new HashMap<>();
        for (int i = 1; i <= months; i++) {
            double revenue = baseRevenue * Math.pow(growthRate, i);
            monthlyForecast.put("Month " + i, Math.round(revenue * 100.0) / 100.0);
        }

        forecast.put("forecastMonths", months);
        forecast.put("monthlyRevenue", monthlyForecast);
        forecast.put("totalProjectedRevenue", monthlyForecast.values().stream().mapToDouble(Double::doubleValue).sum());

        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/boardgames/performance")
    public ResponseEntity<Map<String, Object>> getBoardgamePerformance() {
        Map<String, Object> performance = new HashMap<>();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("mostReservedGame", "Catan");
        metrics.put("leastReservedGame", "Chess");
        metrics.put("averageReservationsPerGame", 12.5);
        metrics.put("topPerformingGenre", "Strategy");
        metrics.put("utilizationRate", 78.3);

        performance.put("performanceMetrics", metrics);
        performance.put("analysisDate", LocalDate.now());

        return ResponseEntity.ok(performance);
    }

    @GetMapping("/customer/insights")
    public ResponseEntity<Map<String, Object>> getCustomerInsights() {
        Map<String, Object> insights = new HashMap<>();

        Map<String, Object> customerData = new HashMap<>();
        customerData.put("totalCustomers", userService.getTotalUsersCount());
        customerData.put("activeCustomers", userService.getTotalUsersCount() * 0.75);
        customerData.put("averageReservationsPerCustomer", 3.2);
        customerData.put("customerRetentionRate", 85.7);
        customerData.put("newCustomersThisMonth", 12);

        insights.put("customerInsights", customerData);
        insights.put("generatedAt", LocalDate.now());

        return ResponseEntity.ok(insights);
    }

    @GetMapping("/trends/seasonal")
    public ResponseEntity<Map<String, Object>> getSeasonalTrends(@RequestParam int year) {
        Map<String, Object> trends = new HashMap<>();

        Map<String, Integer> seasonalData = new HashMap<>();
        seasonalData.put("Winter", 45);
        seasonalData.put("Spring", 38);
        seasonalData.put("Summer", 52);
        seasonalData.put("Autumn", 41);

        trends.put("year", year);
        trends.put("seasonalReservations", seasonalData);
        trends.put("peakSeason", "Summer");
        trends.put("lowSeason", "Spring");

        return ResponseEntity.ok(trends);
    }
}