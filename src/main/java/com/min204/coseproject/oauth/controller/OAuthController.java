package com.min204.coseproject.oauth.controller;

import com.min204.coseproject.oauth.dto.oAuthLoginParams.GoogleLoginParams;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.KakaoLoginParams;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.NaverLoginParams;
import com.min204.coseproject.oauth.jwt.AuthTokens;
import com.min204.coseproject.oauth.service.OAuthGoogleLoginService;
import com.min204.coseproject.oauth.service.googleOAuthService;
import com.min204.coseproject.oauth.service.OAuthLoginService;
import com.min204.coseproject.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {
    private final OAuthGoogleLoginService oAuthGoogleLoginService;
    private final OAuthLoginService oAuthLoginService;
    private final OAuthService oAuthService;
    private final googleOAuthService googleOAuthService;

    private String authUrl = "https://kauth.kakao.com/oauth/token";

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> signUpKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthService.handleOAuth(params));
    }

    @PostMapping("/naver")
    public ResponseEntity<AuthTokens> signUpNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthService.handleOAuth(params));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthTokens> signUpGoogle(@RequestBody GoogleLoginParams params) {
        return ResponseEntity.ok(googleOAuthService.handleOAuth(params));
    }
}
