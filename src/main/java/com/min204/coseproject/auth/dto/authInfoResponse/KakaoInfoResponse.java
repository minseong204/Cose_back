package com.min204.coseproject.auth.dto.authInfoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.min204.coseproject.constant.LoginType;
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
    public LoginType getLoginType() {
        return LoginType.KAKAO;
    }

    @Override
    public String getPicture() {
        return kakaoAccount.getProfile().getProfileImageUrl();
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

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
}