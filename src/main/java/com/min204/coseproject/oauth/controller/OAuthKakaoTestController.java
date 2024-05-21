package com.min204.coseproject.oauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OAuthKakaoTestController {
    @GetMapping("/oauth/callback/kakao")
    public String handler(@RequestParam(name = "code") String authorizationCode) {
        log.info("인가코드 : {}", authorizationCode);
        return authorizationCode;
    }
}
