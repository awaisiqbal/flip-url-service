package com.awais.urlservice.controller;

import com.awais.urlservice.model.Url;
import com.awais.urlservice.dto.CreateUrlRequest;
import com.awais.urlservice.dto.CreateUrlResponse;
import com.awais.urlservice.dto.GetUrlResponse;
import com.awais.urlservice.mapper.ShortenerMapper;
import com.awais.urlservice.service.impl.ShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/url")
public class ShortenerController {

    private final ShortenerService service;
    private final ShortenerMapper mapper;

    public ShortenerController(ShortenerService service, ShortenerMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CreateUrlResponse> createShortUrl(@RequestBody CreateUrlRequest request) {
        Url shortenedUrl = service.createEncodedUrl(request.getLongUrl());
        CreateUrlResponse responseDto = mapper.toCreatedDto(shortenedUrl);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{encodedUrl}")
    public ResponseEntity<GetUrlResponse> getLongUrl(@PathVariable String encodedUrl) {
        Url url = service.getLongUrl(encodedUrl);
        GetUrlResponse dto = mapper.toGetDto(url);
        return ResponseEntity.ok(dto);
    }
}
