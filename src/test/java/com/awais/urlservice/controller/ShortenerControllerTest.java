package com.awais.urlservice.controller;

import com.awais.urlservice.dto.CreateUrlRequest;
import com.awais.urlservice.dto.CreateUrlResponse;
import com.awais.urlservice.dto.GetUrlResponse;
import com.awais.urlservice.exception.NotFoundException;
import com.awais.urlservice.mapper.ShortenerMapper;
import com.awais.urlservice.model.Url;
import com.awais.urlservice.service.impl.ShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatusCode.valueOf;

class ShortenerControllerTest {

    @Mock
    private ShortenerService service;

    @Mock
    private ShortenerMapper mapper;

    @InjectMocks
    private ShortenerController controller;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createShortUrl_ReturnsCreatedResponse() {
        // Given
        CreateUrlRequest request = new CreateUrlRequest();
        request.setLongUrl("http://example.com");

        Url url = new Url("http://example.com", "shortUrl123");
        when(service.createEncodedUrl(request.getLongUrl())).thenReturn(url);

        CreateUrlResponse response = new CreateUrlResponse();
        response.setShortUrl("shortUrl123");
        when(mapper.toCreatedDto(url)).thenReturn(response);

        // When
        ResponseEntity<CreateUrlResponse> result = controller.createShortUrl(request);

        // Then
        assertNotNull(result);
        assertEquals(valueOf(200), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("shortUrl123", result.getBody().getShortUrl());
        verify(service, times(1)).createEncodedUrl(request.getLongUrl());
        verify(mapper, times(1)).toCreatedDto(url);
    }

    @Test
    void getLongUrl_ReturnsLongUrlResponse() {
        // Given
        String shortUrl = "shortUrl123";
        Url url = new Url("http://example.com", shortUrl);
        when(service.getLongUrl(shortUrl)).thenReturn(url);

        GetUrlResponse response = new GetUrlResponse("http://example.com");
        when(mapper.toGetDto(url)).thenReturn(response);

        // When
        ResponseEntity<GetUrlResponse> result = controller.getLongUrl(shortUrl);

        // Then
        assertNotNull(result);
        assertEquals(valueOf(200), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("http://example.com", result.getBody().getLongUrl());
        verify(service, times(1)).getLongUrl(shortUrl);
        verify(mapper, times(1)).toGetDto(url);
    }

    // TODO Need to improve this to handle 400 mapping
    @Test
    void getLongUrl_NonExistingShortUrl_ThrowsNotFoundException() {
        // Given
        String invalidLongUrl = "";
        CreateUrlRequest invalidRequest = new CreateUrlRequest(invalidLongUrl);
        when(service.createEncodedUrl(invalidLongUrl)).thenThrow(new NotFoundException("Invalid URL"));

        // When + Then
        assertThrows(NotFoundException.class, () -> controller.createShortUrl(invalidRequest));
    }

    // TODO Need to improve this to handle 404 mapping
    @Test
    void getLongUrl_NonExistingShortUrl_ReturnsNotFoundResponse() {
        // Given
        String shortUrl = "nonExistingShortUrl";
        when(service.getLongUrl(shortUrl)).thenThrow(new NotFoundException("ShortUrl nonExistingShortUrl not found"));

        // When Then
        assertThrows(NotFoundException.class, () -> controller.getLongUrl(shortUrl));
        verify(service, times(1)).getLongUrl(shortUrl);
    }
}