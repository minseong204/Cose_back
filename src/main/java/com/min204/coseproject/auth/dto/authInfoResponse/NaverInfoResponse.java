package com.min204.coseproject.auth.dto.authInfoResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.min204.coseproject.constant.LoginType;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {
    @JsonProperty("response")
    private Response response;

    public String picture;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String email;
        private String nickname;
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }

    @Override
    public String getNickname() {
        return response.getNickname();
    }

    @Override
    public LoginType getLoginType() {
        return LoginType.NAVER;
    }

    @Override
    public String getPicture() {
        return picture;
    }
}