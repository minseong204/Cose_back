package com.min204.coseproject.map.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoClient {

    private final RestTemplate restTemplate;

    @Value("${oauth.kakao.client-id}")
    private String apiKey;

    public KakaoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchAddress(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
