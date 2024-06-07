package com.min204.coseproject.oauth.dto.oAuthLoginParams;

import com.min204.coseproject.oauth.entity.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLoginParams implements OAuthLoginParams {
    private String accessToken;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
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
