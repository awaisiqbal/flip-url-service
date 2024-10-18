package com.awais.urlservice.mapper;


import com.awais.urlservice.dto.CreateUrlResponse;
import com.awais.urlservice.dto.GetUrlResponse;
import com.awais.urlservice.model.Url;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class ShortenerMapper {

    // FIXME: Load dinamically the baseurl for the shortener path, we should load from property file depending on the env
    public static final String BASE_URL = "http://localhost:8080/url";

    public CreateUrlResponse toCreatedDto(Url url) {
        return new CreateUrlResponse(format("%s/%s", BASE_URL, url.getEncodedUrl()));
    }

    public GetUrlResponse toGetDto(Url url) {
        return new GetUrlResponse(url.getLongUrl());
    }
}
