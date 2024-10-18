package com.awais.urlservice.service.impl;

import com.awais.urlservice.exception.NotFoundException;
import com.awais.urlservice.model.Url;
import com.awais.urlservice.repository.IUrlRepository;
import com.awais.urlservice.service.IEncodingService;
import com.awais.urlservice.service.IShortenerService;
import com.awais.urlservice.validator.ShortenerValidator;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class ShortenerService implements IShortenerService {

    private final IUrlRepository repository;
    private final IEncodingService encodingService;
    private final ShortenerValidator shortenerValidator;

    public ShortenerService(IUrlRepository repository, IEncodingService encodingService, ShortenerValidator shortenerValidator) {
        this.repository = repository;
        this.encodingService = encodingService;
        this.shortenerValidator = shortenerValidator;
    }

    @Override
    public Url createEncodedUrl(String longUrl) {
        shortenerValidator.validateUrl(longUrl);
        return repository
                .findByLongUrl(longUrl)
                .orElseGet(() -> {
                    String encodedUrl = encodingService.encode(longUrl);
                    Url newUrl = new Url(longUrl, encodedUrl);
                    return repository.save(newUrl);
                });
    }

    @Override
    public Url getLongUrl(String encodedURL) {
        return repository.findByEncodedUrl(encodedURL)
                .orElseThrow(() -> new NotFoundException(format("EncodedURL %s not found", encodedURL)));
    }
}
