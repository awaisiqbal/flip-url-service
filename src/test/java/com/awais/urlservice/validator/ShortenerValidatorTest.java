package com.awais.urlservice.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShortenerValidatorTest {
    private ShortenerValidator shortenerValidator;

    @BeforeEach
    void setUp() {
        shortenerValidator = new ShortenerValidator();
    }

    @Test
    void shouldThrowExceptionWhenUrlIsNull() {
        // Given
        String url = null;

        // Then
        assertThrows(IllegalArgumentException.class, () -> shortenerValidator.validateUrl(url));
    }

    @Test
    void shouldThrowExceptionWhenUrlIsEmpty() {
        // Given
        String url = "";

        // Then
        assertThrows(IllegalArgumentException.class, () -> shortenerValidator.validateUrl(url));
    }

    @Test
    void shouldNotThrowExceptionWhenUrlIsValid() {
        // Given
        String url = "https://www.google.com";

        // Then
        assertDoesNotThrow(() -> shortenerValidator.validateUrl(url));
    }


}