package com.meepleconnect.boardgamesapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllNotifications() {
        List<Map<String, Object>> notifications = new ArrayList<>();

        Map<String, Object> notification1 = new HashMap<>();
        notification1.put("id", 1L);
        notification1.put("type", "RESERVATION_REMINDER");
        notification1.put("title", "Reservering herinnering");
        notification1.put("message", "Je hebt morgen een reservering voor Catan");
        notification1.put("timestamp", LocalDateTime.now().minusHours(2));
        notification1.put("read", false);
        notification1.put("priority", "MEDIUM");

        Map<String, Object> notification2 = new HashMap<>();
        notification2.put("id", 2L);
        notification2.put("type", "SYSTEM_UPDATE");
        notification2.put("title", "Systeem update");
        notification2.put("message", "Nieuwe bordspellen toegevoegd aan de collectie");
        notification2.put("timestamp", LocalDateTime.now().minusDays(1));
        notification2.put("read", true);
        notification2.put("priority", "LOW");

        notifications.add(notification1);
        notifications.add(notification2);

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getNotificationById(@PathVariable Long id) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("id", id);
        notification.put("type", "RESERVATION_CONFIRMATION");
        notification.put("title", "Reservering bevestigd");
        notification.put("message", "Je reservering voor Ticket to Ride is bevestigd");
        notification.put("timestamp", LocalDateTime.now());
        notification.put("read", false);
        notification.put("priority", "HIGH");

        return ResponseEntity.ok(notification);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createNotification(
            @RequestBody Map<String, Object> notificationRequest) {
        String type = (String) notificationRequest.get("type");
        String title = (String) notificationRequest.get("title");
        String message = (String) notificationRequest.get("message");
        String priority = (String) notificationRequest.get("priority");

        Map<String, Object> newNotification = new HashMap<>();
        newNotification.put("id", System.currentTimeMillis());
        newNotification.put("type", type);
        newNotification.put("title", title);
        newNotification.put("message", message);
        newNotification.put("timestamp", LocalDateTime.now());
        newNotification.put("read", false);
        newNotification.put("priority", priority != null ? priority : "MEDIUM");

        return ResponseEntity.status(201).body(newNotification);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("status", "MARKED_AS_READ");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Object>> getUnreadCount() {
        Map<String, Object> count = new HashMap<>();
        count.put("unreadCount", 5);
        count.put("totalCount", 12);

        return ResponseEntity.ok(count);
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, Object>> broadcastNotification(
            @RequestBody Map<String, Object> broadcastRequest) {
        String message = (String) broadcastRequest.get("message");
        String priority = (String) broadcastRequest.get("priority");

        Map<String, Object> response = new HashMap<>();
        response.put("broadcastId", System.currentTimeMillis());
        response.put("message", message);
        response.put("priority", priority);
        response.put("recipients", 150);
        response.put("sentAt", LocalDateTime.now());

        return ResponseEntity.status(201).body(response);
    }
}