package com.awais.urlservice.repository.impl;

import com.awais.urlservice.model.Url;
import com.awais.urlservice.repository.IUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HashMapUrlRepositoryTest {

    private IUrlRepository urlRepository;

    @BeforeEach
    void setUp() {
        urlRepository = new HashMapUrlRepository();
    }

    @Test
    void shouldReturnEmptyFindingByLongUrl_WhenURLIsNotSavedPreviously() {
        // When
        Optional<Url> foundUrl = urlRepository.findByLongUrl("http://nonexisting.com");

        // Then
        assertFalse(foundUrl.isPresent());
    }

    @Test
    void shouldSaveShortUrlCorrectly_WhenNewUrlIsProvided() {
        // Given
        Url url = new Url("http://example.com", "shortUrl123");

        // When
        Url savedUrl = urlRepository.save(url);

        // Then
        assertEquals(url, savedUrl);
        Optional<Url> retrievedUrl = urlRepository.findByEncodedUrl("shortUrl123");
        assertTrue(retrievedUrl.isPresent());
        assertEquals("http://example.com", retrievedUrl.get().getLongUrl());
    }

    @Test
    void shouldFindByLongUrl_WhenWeHaveTheURLStoredPrevious() {
        // Given
        Url url = new Url("http://example.com", "shortUrl123");
        urlRepository.save(url);

        // When
        Optional<Url> foundUrl = urlRepository.findByLongUrl("http://example.com");

        // Then
        assertTrue(foundUrl.isPresent());
        assertEquals("shortUrl123", foundUrl.get().getEncodedUrl());
        assertEquals("http://example.com", foundUrl.get().getLongUrl());
    }


    @Test
    void shouldReturnEmptyFindingByShortUrl_WhenURLIsNotSavedPreviously() {
        // When
        Optional<Url> foundUrl = urlRepository.findByEncodedUrl("nonExistingShortUrl");

        // Then
        assertFalse(foundUrl.isPresent());
    }

    @Test
    void shouldFindByEncodedUrl_WhenWeHaveTheURLStoredPrevious() {
        // Given
        Url url = new Url("http://example.com", "shortUrl123");
        urlRepository.save(url);

        // When
        Optional<Url> foundUrl = urlRepository.findByEncodedUrl("shortUrl123");

        // Then
        assertTrue(foundUrl.isPresent());
        assertEquals("http://example.com", foundUrl.get().getLongUrl());
        assertEquals("shortUrl123", foundUrl.get().getEncodedUrl());
    }


    @Test
    void shouldOverwriteUrl_WhenSameShortUrlIsMappedToAnotherLongUrl() {
        // Given
        Url url1 = new Url("http://example1.com", "shortUrl123");
        Url url2 = new Url("http://example2.com", "shortUrl123");
        urlRepository.save(url1);

        // When
        urlRepository.save(url2);

        // Then
        Optional<Url> foundUrl = urlRepository.findByEncodedUrl("shortUrl123");
        assertTrue(foundUrl.isPresent());
        assertEquals("http://example2.com", foundUrl.get().getLongUrl());
    }
}