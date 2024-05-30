package com.min204.coseproject.map.controller;

import com.min204.coseproject.map.client.KakaoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final KakaoClient kakaoClient;

    @GetMapping("/location/search")
    public String searchLocation(@RequestParam String address) {
        return kakaoClient.searchAddress(address);
    }

    @GetMapping("/location/keyword")
    public String searchKeyword(@RequestParam String address) {
        return kakaoClient.searchKeyword(address);
    }

}
