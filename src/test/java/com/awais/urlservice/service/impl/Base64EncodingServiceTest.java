package com.awais.urlservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class Base64EncodingServiceTest {

    private Base64EncodingService encodingService;

    @BeforeEach
    void setUp() {
        encodingService = new Base64EncodingService();
    }

    @Test
    void shouldThrowException_WhenUrlIsEmpty() {
        // Given
        String url = "";

        // When + Then
        assertThrows(RuntimeException.class, () -> encodingService.encode(url));
    }

    @Test
    void shouldReturnEncodedUrlOf8Characters_WhenUrlIsLongerThan8Chars() {
        // Given
        String url = "http://example.com";
        int numberOfCharactersInShortUrl = 8;

        // When
        String encodedUrl = encodingService.encode(url);

        // Then
        String expectedEncodedUrl = Base64.getUrlEncoder()
                .encodeToString(url.getBytes(StandardCharsets.UTF_8))
                .substring(0, numberOfCharactersInShortUrl);

        assertNotNull(encodedUrl);
        assertEquals(numberOfCharactersInShortUrl, encodedUrl.length());
        assertEquals(expectedEncodedUrl, encodedUrl);
    }


    @Test
    void shouldProvideShortUrl_WhenSpecialCharactersAreProvided() {
        // Given
        String url = "http://example.com/?query=test#fragment";
        int numberOfCharactersInShortUrl = 8;

        // When
        String encodedUrl = encodingService.encode(url);

        // Then
        String expectedEncodedUrl = Base64.getUrlEncoder()
                .encodeToString(url.getBytes(StandardCharsets.UTF_8))
                .substring(0, numberOfCharactersInShortUrl);

        assertNotNull(encodedUrl);
        assertEquals(numberOfCharactersInShortUrl, encodedUrl.length());
        assertEquals(expectedEncodedUrl, encodedUrl);
    }
}