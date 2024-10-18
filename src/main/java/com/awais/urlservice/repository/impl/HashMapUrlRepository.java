package com.awais.urlservice.repository.impl;

import com.awais.urlservice.model.Url;
import com.awais.urlservice.repository.IUrlRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Repository
public class HashMapUrlRepository implements IUrlRepository {

    // Encoded Url is used as a Key and Long Url is Value
    // (EncodedUrl, LongUrl)
    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    @Override
    public Optional<Url> findByLongUrl(String longUrl) {
        return map.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(longUrl))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(encodedUrl -> new Url(longUrl, encodedUrl));
    }

    @Override
    public Optional<Url> findByEncodedUrl(String encodedUrl) {
        return ofNullable(map.get(encodedUrl))
                .map(longUrl -> new Url(longUrl, encodedUrl));
    }

    @Override
    public Url save(Url newUrl) {
        map.put(newUrl.getEncodedUrl(), newUrl.getLongUrl());
        return newUrl;
    }
}
