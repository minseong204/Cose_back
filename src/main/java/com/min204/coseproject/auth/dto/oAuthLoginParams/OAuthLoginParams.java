package com.min204.coseproject.auth.dto.oAuthLoginParams;

import com.min204.coseproject.constant.LoginType;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    LoginType loginType();

    MultiValueMap<String, String> makeBody();

    String getAccessToken();
}
