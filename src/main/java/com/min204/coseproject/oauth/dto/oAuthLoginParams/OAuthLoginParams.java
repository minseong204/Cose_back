package com.min204.coseproject.oauth.dto.oAuthLoginParams;

import com.min204.coseproject.oauth.entity.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
