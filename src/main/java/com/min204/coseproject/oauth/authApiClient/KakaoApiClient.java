package com.min204.coseproject.oauth.authApiClient;

import com.min204.coseproject.oauth.dto.authInfoResponse.KakaoInfoResponse;
import com.min204.coseproject.oauth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.oauth.entity.OAuthProvider;
import com.min204.coseproject.oauth.tokens.KakaoTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        throw new UnsupportedOperationException("카카오는 이 메서드에서 인가코드를 통해 AccessToken 을 발급해주지 않습니다.");
    }

    /*
     * TODO : http 통신에서는 AccessToken 을 통신함에 있어 보안상 이슈 발생..
     *  반드시, 멀티커넥터를 이용한 http, https 동시 적용 필수
     * */
    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {

        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<KakaoInfoResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, KakaoInfoResponse.class);

        return response.getBody();
    }
}