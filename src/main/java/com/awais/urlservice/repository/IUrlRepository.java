package com.awais.urlservice.repository;

import com.awais.urlservice.model.Url;

import java.util.Optional;

public interface IUrlRepository {
    Optional<Url> findByLongUrl(String url);

    Optional<Url> findByEncodedUrl(String url);

    Url save(Url newUrl);
}
