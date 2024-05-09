package com.min204.coseproject.oauth.dto.authInfoResponse;

import com.min204.coseproject.oauth.entity.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
