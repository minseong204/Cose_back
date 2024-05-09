package com.min204.coseproject.oauth.dto.authInfoResponse;

import com.min204.coseproject.oauth.entity.OAuthProvider;

public class GoogleInfoResponse implements OAuthInfoResponse {
    public String id;
    public String email;
    public Boolean verifiedEmail;
    public String name;
    public String givenName;
    public String familyName;
    public String picture;
    public String locale;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getNickname() {
        return name;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.GOOGLE;
    }
}
