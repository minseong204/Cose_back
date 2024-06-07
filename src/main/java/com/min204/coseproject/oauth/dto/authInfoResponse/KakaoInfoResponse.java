package com.min204.coseproject.oauth.dto.authInfoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.min204.coseproject.oauth.entity.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Override
    public String getNickname() {
        return kakaoAccount.getProfile().getNickname();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class KakaoAccount {
        @JsonProperty("email")
        private String email;

        @JsonProperty("profile")
        private Profile profile;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Profile {
            @JsonProperty("nickname")
            private String nickname;
        }
    }
}