package com.awais.urlservice.service.impl;

import com.awais.urlservice.service.IEncodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class Base64EncodingService implements IEncodingService {

    @Override
    public String encode(String url) {
        int numberOfCharactersRetrieveFromEncoding = 8; // Could be moved as a loaded property
        // Copied online from StackOverFlow
        return Base64.getUrlEncoder()
                .encodeToString(url.getBytes(StandardCharsets.UTF_8))
                .substring(0, numberOfCharactersRetrieveFromEncoding);
    }
}
