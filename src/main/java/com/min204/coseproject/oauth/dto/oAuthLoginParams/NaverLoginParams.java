package com.min204.coseproject.oauth.dto.oAuthLoginParams;

import com.min204.coseproject.oauth.entity.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class NaverLoginParams implements OAuthLoginParams {
    private String accessToken;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
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
