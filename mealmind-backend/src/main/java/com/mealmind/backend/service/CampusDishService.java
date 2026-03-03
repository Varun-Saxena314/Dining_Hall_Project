package com.mealmind.backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CampusDishService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchMenuJson(String periodId, String date) {

        String url = UriComponentsBuilder
                .fromUriString("https://virginia.campusdish.com/api/menu/GetMenus")
                .queryParam("locationId", "695")
                .queryParam("storeIds", "")
                .queryParam("mode", "Daily")
                .queryParam("date", date)
                .queryParam("time", "")
                .queryParam("periodId", periodId)
                .queryParam("fulfillmentMethod", "")
                .toUriString();

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }
}