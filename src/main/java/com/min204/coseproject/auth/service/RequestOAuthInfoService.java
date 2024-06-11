package com.min204.coseproject.auth.service;

import com.min204.coseproject.auth.authApiClient.OAuthApiClient;
import com.min204.coseproject.auth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.auth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.constant.LoginType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestOAuthInfoService {
    private final Map<LoginType, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::loginType, Function.identity())
        );
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.loginType());
        return client.requestOauthInfo(params.getAccessToken());
    }
}
