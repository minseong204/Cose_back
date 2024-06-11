package com.min204.coseproject.auth.authApiClient;


import com.min204.coseproject.auth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.auth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.constant.LoginType;

public interface OAuthApiClient {
    LoginType loginType();

    String requestAccessToken(OAuthLoginParams params);

    OAuthInfoResponse requestOauthInfo(String accessToken);
}
