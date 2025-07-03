package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Optional<Publisher> getPublisherById(Long id) {
        return publisherRepository.findById(id);
    }

    public List<Publisher> getPublishersByCountry(String country) {
        return publisherRepository.findByCountryOfOrigin(country);
    }

    public Publisher addPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Optional<Publisher> updatePublisher(Long id, Publisher updatedPublisher) {
        return publisherRepository.findById(id).map(existingPublisher -> {
            existingPublisher.setName(updatedPublisher.getName());
            existingPublisher.setCountryOfOrigin(updatedPublisher.getCountryOfOrigin());
            existingPublisher.setFounded(updatedPublisher.getFounded());
            existingPublisher.setIndie(updatedPublisher.isIndie());

            return publisherRepository.save(existingPublisher);
        });
    }

    public boolean deletePublisher(Long id) {
        if (publisherRepository.existsById(id)) {
            publisherRepository.deleteById(id);
            return true;
        }
        return false;
    }
}