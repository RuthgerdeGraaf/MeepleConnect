package com.meepleconnect.boardgamesapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllReviews() {
        List<Map<String, Object>> reviews = new ArrayList<>();

        Map<String, Object> review1 = new HashMap<>();
        review1.put("id", 1L);
        review1.put("boardgameId", 1L);
        review1.put("boardgameName", "Catan");
        review1.put("userId", 1L);
        review1.put("username", "Ruthger");
        review1.put("rating", 5);
        review1.put("comment", "Geweldig spel! Zeer strategisch en leuk om te spelen.");
        review1.put("createdAt", LocalDateTime.now().minusDays(5));
        review1.put("helpful", 12);

        Map<String, Object> review2 = new HashMap<>();
        review2.put("id", 2L);
        review2.put("boardgameId", 1L);
        review2.put("boardgameName", "Catan");
        review2.put("userId", 2L);
        review2.put("username", "Edwin");
        review2.put("rating", 4);
        review2.put("comment", "Goed spel, maar kan soms lang duren.");
        review2.put("createdAt", LocalDateTime.now().minusDays(3));
        review2.put("helpful", 8);

        reviews.add(review1);
        reviews.add(review2);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReviewById(@PathVariable Long id) {
        Map<String, Object> review = new HashMap<>();
        review.put("id", id);
        review.put("boardgameId", 1L);
        review.put("boardgameName", "Catan");
        review.put("userId", 1L);
        review.put("username", "Ruthger");
        review.put("rating", 5);
        review.put("comment", "Geweldig spel! Zeer strategisch en leuk om te spelen.");
        review.put("createdAt", LocalDateTime.now().minusDays(5));
        review.put("helpful", 12);

        return ResponseEntity.ok(review);
    }

    @GetMapping("/boardgame/{boardgameId}")
    public ResponseEntity<List<Map<String, Object>>> getReviewsByBoardgame(@PathVariable Long boardgameId) {
        List<Map<String, Object>> reviews = new ArrayList<>();

        Map<String, Object> review = new HashMap<>();
        review.put("id", 1L);
        review.put("boardgameId", boardgameId);
        review.put("boardgameName", "Catan");
        review.put("userId", 1L);
        review.put("username", "Ruthger");
        review.put("rating", 5);
        review.put("comment", "Geweldig spel! Zeer strategisch en leuk om te spelen.");
        review.put("createdAt", LocalDateTime.now().minusDays(5));
        review.put("helpful", 12);

        reviews.add(review);

        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createReview(@RequestBody Map<String, Object> reviewRequest) {
        Long boardgameId = Long.valueOf(reviewRequest.get("boardgameId").toString());
        Long userId = Long.valueOf(reviewRequest.get("userId").toString());
        Integer rating = (Integer) reviewRequest.get("rating");
        String comment = (String) reviewRequest.get("comment");

        Map<String, Object> newReview = new HashMap<>();
        newReview.put("id", System.currentTimeMillis());
        newReview.put("boardgameId", boardgameId);
        newReview.put("boardgameName", "Catan");
        newReview.put("userId", userId);
        newReview.put("username", "User" + userId);
        newReview.put("rating", rating);
        newReview.put("comment", comment);
        newReview.put("createdAt", LocalDateTime.now());
        newReview.put("helpful", 0);

        return ResponseEntity.status(201).body(newReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateReview(@PathVariable Long id,
            @RequestBody Map<String, Object> reviewUpdate) {
        Integer rating = (Integer) reviewUpdate.get("rating");
        String comment = (String) reviewUpdate.get("comment");

        Map<String, Object> updatedReview = new HashMap<>();
        updatedReview.put("id", id);
        updatedReview.put("boardgameId", 1L);
        updatedReview.put("boardgameName", "Catan");
        updatedReview.put("userId", 1L);
        updatedReview.put("username", "Ruthger");
        updatedReview.put("rating", rating);
        updatedReview.put("comment", comment);
        updatedReview.put("createdAt", LocalDateTime.now().minusDays(5));
        updatedReview.put("updatedAt", LocalDateTime.now());
        updatedReview.put("helpful", 12);

        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/boardgame/{boardgameId}/average-rating")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long boardgameId) {
        Map<String, Object> rating = new HashMap<>();
        rating.put("boardgameId", boardgameId);
        rating.put("boardgameName", "Catan");
        rating.put("averageRating", 4.5);
        rating.put("totalReviews", 25);
        rating.put("ratingDistribution", Map.of(
                "5_stars", 12,
                "4_stars", 8,
                "3_stars", 3,
                "2_stars", 1,
                "1_star", 1));

        return ResponseEntity.ok(rating);
    }

    @PostMapping("/{id}/helpful")
    public ResponseEntity<Map<String, Object>> markAsHelpful(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("helpfulCount", 13);
        response.put("markedAt", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}