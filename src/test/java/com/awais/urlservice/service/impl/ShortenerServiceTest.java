package com.awais.urlservice.service.impl;

import com.awais.urlservice.exception.NotFoundException;
import com.awais.urlservice.model.Url;
import com.awais.urlservice.repository.IUrlRepository;
import com.awais.urlservice.service.IEncodingService;
import com.awais.urlservice.validator.ShortenerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ShortenerServiceTest {

    @Mock
    private IUrlRepository repository;

    @Mock
    private IEncodingService encodingService;

    @Mock
    private ShortenerValidator shortenerValidator;

    @InjectMocks
    private ShortenerService shortenerService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldReturnExistingShortUrl_WhenPreviousShortAlreadyExists() {
        // Given
        String longUrl = "http://www.google.es";
        Url existingUrl = new Url(longUrl, "shortUrl123");
        when(repository.findByLongUrl(longUrl)).thenReturn(Optional.of(existingUrl));

        // When
        Url result = shortenerService.createEncodedUrl(longUrl);

        // Then
        assertEquals(existingUrl, result);
        verify(shortenerValidator, times(1)).validateUrl(longUrl);
        verify(repository, times(1)).findByLongUrl(longUrl);
        verify(repository, never()).save(any());
        verify(encodingService, never()).encode(any());
    }

    @Test
    void shouldCreateNewShortUrlAndSaveIt_WhenIsTheFirstTimeGeneratingShortUrl() {
        // Given
        String longUrl = "http://example.com";
        String encodedUrl = "shortUrl123";
        when(repository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(encodingService.encode(longUrl)).thenReturn(encodedUrl);
        Url newUrl = new Url(longUrl, encodedUrl);
        when(repository.save(any(Url.class))).thenReturn(newUrl);

        // When
        Url result = shortenerService.createEncodedUrl(longUrl);

        // Then
        assertEquals(newUrl, result);
        verify(shortenerValidator, times(1)).validateUrl(longUrl);
        verify(repository, times(1)).findByLongUrl(longUrl);
        verify(encodingService, times(1)).encode(longUrl);
        verify(repository, times(1)).save(any(Url.class));
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenProvidedUrlIsInvalid() {
        // Given
        String invalidUrl = "";
        doThrow(new IllegalArgumentException("LongUrl is required")).when(shortenerValidator).validateUrl(invalidUrl);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> shortenerService.createEncodedUrl(invalidUrl));
        assertEquals("LongUrl is required", exception.getMessage());
        verify(shortenerValidator, times(1)).validateUrl(invalidUrl);
        verify(repository, never()).findByLongUrl(anyString());
    }

    @Test
    void shouldReturnStoredLongUrl_WhenTheShortUrlDOesExists() {
        // Given
        String encodedUrl = "shortUrl123";
        Url existingUrl = new Url("http://www.google.es", encodedUrl);
        when(repository.findByEncodedUrl(encodedUrl)).thenReturn(Optional.of(existingUrl));

        // When
        Url result = shortenerService.getLongUrl(encodedUrl);

        // Then
        assertEquals(existingUrl, result);
        verify(repository, times(1)).findByEncodedUrl(encodedUrl);
    }

    @Test
    void shouldThrowNotFoundException_WhenShortVersionOfThatUrlIsNotAvailable() {
        // Given
        String encodedUrl = "shortUrl123";
        when(repository.findByEncodedUrl(encodedUrl)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> shortenerService.getLongUrl(encodedUrl));
        assertEquals("EncodedUrl shortUrl123 not found", exception.getMessage());
        verify(repository, times(1)).findByEncodedUrl(encodedUrl);
    }

}