package com.min204.coseproject.oauth.dto.authInfoResponse;

import com.min204.coseproject.oauth.entity.OAuthProvider;

public class GoogleInfoResponse implements OAuthInfoResponse {
    public String id;
    public String email;
    public String name;
    public String picture;

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

    @Override
    public String getPicture() {
        return picture;
    }
}
