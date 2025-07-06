package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.PublisherNotFoundException;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException("Publisher with ID " + id + " not found."));
    }

    public List<Publisher> getPublishersByCountry(String country) {
        return publisherRepository.findByCountryOfOrigin(country);
    }

    public Publisher addPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Publisher updatePublisher(Long id, Publisher updatedPublisher) {
        return publisherRepository.findById(id).map(existingPublisher -> {
            existingPublisher.setName(updatedPublisher.getName());
            existingPublisher.setCountryOfOrigin(updatedPublisher.getCountryOfOrigin());
            existingPublisher.setFounded(updatedPublisher.getFounded());
            existingPublisher.setIndie(updatedPublisher.isIndie());

            return publisherRepository.save(existingPublisher);
        }).orElseThrow(() -> new PublisherNotFoundException("Publisher with ID " + id + " not found."));
    }

    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new PublisherNotFoundException("Publisher with ID " + id + " not found.");
        }
        publisherRepository.deleteById(id);
    }
}