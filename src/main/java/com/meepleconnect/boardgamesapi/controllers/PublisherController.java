package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.services.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<Publisher> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        Optional<Publisher> publisher = publisherService.getPublisherById(id);
        return publisher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/country/{country}")
    public List<Publisher> getPublishersByCountry(@PathVariable String country) {
        return publisherService.getPublishersByCountry(country);
    }

    @PostMapping
    public ResponseEntity<Publisher> addPublisher(@RequestBody Publisher publisher) {
        Publisher savedPublisher = publisherService.addPublisher(publisher);
        return ResponseEntity.ok(savedPublisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @RequestBody Publisher updatedPublisher) {
        Optional<Publisher> updated = publisherService.updatePublisher(id, updatedPublisher);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        if (publisherService.deletePublisher(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
