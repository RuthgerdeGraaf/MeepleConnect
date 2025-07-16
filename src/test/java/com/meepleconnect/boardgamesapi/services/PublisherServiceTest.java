package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.exceptions.PublisherNotFoundException;
import com.meepleconnect.boardgamesapi.models.Publisher;
import com.meepleconnect.boardgamesapi.repositories.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private Publisher testPublisher;
    private Publisher testPublisher2;

    @BeforeEach
    void setUp() {
        testPublisher = new Publisher();
        testPublisher.setName("Test Publisher");
        testPublisher.setCountryOfOrigin("Netherlands");
        testPublisher.setFounded(2020);
        testPublisher.setIndie(true);

        testPublisher2 = new Publisher();
        testPublisher2.setName("Second Publisher");
        testPublisher2.setCountryOfOrigin("Germany");
        testPublisher2.setFounded(2015);
        testPublisher2.setIndie(false);
    }

    @Test
    void getAllPublishers_ShouldReturnAllPublishers() {
        List<Publisher> expectedPublishers = List.of(testPublisher, testPublisher2);
        when(publisherRepository.findAll()).thenReturn(expectedPublishers);

        List<Publisher> result = publisherService.getAllPublishers();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testPublisher, testPublisher2);
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    void getPublisherById_WithValidId_ShouldReturnPublisher() {
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(testPublisher));

        Publisher result = publisherService.getPublisherById(1L);

        assertThat(result).isEqualTo(testPublisher);
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    void getPublisherById_WithInvalidId_ShouldThrowPublisherNotFoundException() {
        when(publisherRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> publisherService.getPublisherById(999L))
                .isInstanceOf(PublisherNotFoundException.class)
                .hasMessage("Publisher with ID 999 not found.");

        verify(publisherRepository, times(1)).findById(999L);
    }

    @Test
    void getPublishersByCountry_ShouldReturnPublishersFromSpecificCountry() {
        List<Publisher> dutchPublishers = List.of(testPublisher);
        when(publisherRepository.findByCountryOfOrigin("Netherlands")).thenReturn(dutchPublishers);

        List<Publisher> result = publisherService.getPublishersByCountry("Netherlands");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCountryOfOrigin()).isEqualTo("Netherlands");
        verify(publisherRepository, times(1)).findByCountryOfOrigin("Netherlands");
    }

    @Test
    void getPublishersByCountry_WithNoMatchingCountry_ShouldReturnEmptyList() {
        when(publisherRepository.findByCountryOfOrigin("France")).thenReturn(List.of());

        List<Publisher> result = publisherService.getPublishersByCountry("France");

        assertThat(result).isEmpty();
        verify(publisherRepository, times(1)).findByCountryOfOrigin("France");
    }

    @Test
    void addPublisher_WithValidPublisher_ShouldSavePublisher() {
        Publisher newPublisher = new Publisher();
        newPublisher.setName("New Publisher");
        newPublisher.setCountryOfOrigin("Belgium");
        newPublisher.setFounded(2022);
        newPublisher.setIndie(true);

        when(publisherRepository.save(any(Publisher.class))).thenReturn(testPublisher);

        Publisher result = publisherService.addPublisher(newPublisher);

        assertThat(result).isEqualTo(testPublisher);
        verify(publisherRepository, times(1)).save(newPublisher);
    }

    @Test
    void addPublisher_WithNullPublisher_ShouldStillCallSave() {
        when(publisherRepository.save(null)).thenReturn(null);

        Publisher result = publisherService.addPublisher(null);

        assertThat(result).isNull();
        verify(publisherRepository, times(1)).save(null);
    }

    @Test
    void updatePublisher_WithValidData_ShouldUpdatePublisher() {
        Publisher updatedData = new Publisher();
        updatedData.setName("Updated Publisher");
        updatedData.setCountryOfOrigin("France");
        updatedData.setFounded(2018);
        updatedData.setIndie(false);

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(testPublisher));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(testPublisher);

        Publisher result = publisherService.updatePublisher(1L, updatedData);

        assertThat(result).isEqualTo(testPublisher);
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).save(testPublisher);
    }

    @Test
    void updatePublisher_WithInvalidId_ShouldThrowPublisherNotFoundException() {
        Publisher updatedData = new Publisher();
        updatedData.setName("Updated Publisher");

        when(publisherRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> publisherService.updatePublisher(999L, updatedData))
                .isInstanceOf(PublisherNotFoundException.class)
                .hasMessage("Publisher with ID 999 not found.");

        verify(publisherRepository, times(1)).findById(999L);
        verify(publisherRepository, never()).save(any());
    }

    @Test
    void deletePublisher_WithValidId_ShouldDeletePublisher() {
        when(publisherRepository.existsById(1L)).thenReturn(true);

        publisherService.deletePublisher(1L);

        verify(publisherRepository, times(1)).existsById(1L);
        verify(publisherRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePublisher_WithInvalidId_ShouldThrowPublisherNotFoundException() {
        when(publisherRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> publisherService.deletePublisher(999L))
                .isInstanceOf(PublisherNotFoundException.class)
                .hasMessage("Publisher with ID 999 not found.");

        verify(publisherRepository, times(1)).existsById(999L);
        verify(publisherRepository, never()).deleteById(anyLong());
    }
}