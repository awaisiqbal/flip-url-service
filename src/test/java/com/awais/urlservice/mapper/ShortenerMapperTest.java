package com.awais.urlservice.mapper;

import com.awais.urlservice.dto.CreateUrlResponse;
import com.awais.urlservice.dto.GetUrlResponse;
import com.awais.urlservice.model.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.awais.urlservice.mapper.ShortenerMapper.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

class ShortenerMapperTest {
    private ShortenerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShortenerMapper();
    }

    @Test
    void shouldMapToCreatedDto_WhenURLIsProvided() {
        // Given
        Url url = new Url("longUrl", "shortUrl");

        // When
        CreateUrlResponse createdDto = mapper.toCreatedDto(url);

        // Then
        assertThat(createdDto.getShortUrl()).isEqualToIgnoringCase(BASE_URL + "/" + url.getEncodedUrl());
    }

    @Test
    void shouldMapToGetDto_WhenURLIsProvided() {
        // Given
        Url url = new Url("longUrl", "shortUrl");

        // When
        GetUrlResponse getDto = mapper.toGetDto(url);

        // Then
        assertThat(getDto.getLongUrl()).isEqualToIgnoringCase(url.getLongUrl());
    }
}