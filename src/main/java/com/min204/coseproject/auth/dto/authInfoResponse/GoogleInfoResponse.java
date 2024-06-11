package com.min204.coseproject.auth.dto.authInfoResponse;

import com.min204.coseproject.constant.LoginType;

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
    public LoginType getLoginType() {
        return LoginType.GOOGLE;
    }

    @Override
    public String getPicture() {
        return picture;
    }
}
