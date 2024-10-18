package com.awais.urlservice.validator;

import org.springframework.stereotype.Component;

@Component
public class ShortenerValidator {

    public void validateUrl(String request) {
        if (request == null || request.isBlank()) {
            throw new IllegalArgumentException("LongUrl is required");
        }
    }

}
