package com.min204.coseproject.auth.dto.authInfoResponse;

import com.min204.coseproject.constant.LoginType;

public interface OAuthInfoResponse {
    String getEmail();

    String getNickname();

    LoginType getLoginType();

    String getPicture();
}
