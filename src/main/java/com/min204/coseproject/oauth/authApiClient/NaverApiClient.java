package com.min204.coseproject.oauth.authApiClient;

import com.min204.coseproject.oauth.dto.authInfoResponse.NaverInfoResponse;
import com.min204.coseproject.oauth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.oauth.entity.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverApiClient implements OAuthApiClient {

    @Value("${oauth.naver.url.api}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }
    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        throw new UnsupportedOperationException("네이버는 이 메서드에서 인가코드를 통해 AccessToken 을 발급해주지 않습니다.");
    }


    /*
     * TODO : http 통신에서는 AccessToken 을 통신함에 있어 보안상 이슈 발생..
     *  반드시, 멀티커넥터를 이용한 http, https 동시 적용 필수
     * */
    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v1/nid/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<NaverInfoResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, NaverInfoResponse.class);

        return response.getBody();
    }
}
