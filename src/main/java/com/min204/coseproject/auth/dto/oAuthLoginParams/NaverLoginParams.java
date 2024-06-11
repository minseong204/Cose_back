package com.min204.coseproject.auth.dto.oAuthLoginParams;

import com.min204.coseproject.constant.LoginType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class NaverLoginParams implements OAuthLoginParams {
    private String accessToken;


    @Override
    public LoginType loginType() {
        return LoginType.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        return null;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }
}