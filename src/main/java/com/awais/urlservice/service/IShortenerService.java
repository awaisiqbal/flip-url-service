package com.awais.urlservice.service;

import com.awais.urlservice.model.Url;

public interface IShortenerService {
    Url createEncodedUrl(String longUrl);

    Url getLongUrl(String encodedURL);
}
