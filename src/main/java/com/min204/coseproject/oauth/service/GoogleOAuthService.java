package com.min204.coseproject.oauth.service;

import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.oauth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.oauth.entity.OAuthUser;
import com.min204.coseproject.oauth.entity.OAuthUserPhoto;
import com.min204.coseproject.oauth.jwt.AuthTokens;
import com.min204.coseproject.oauth.jwt.AuthTokensGenerator;
import com.min204.coseproject.oauth.repository.OAuthUserRepository;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    private final OAuthUserRepository oAuthUserRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthGoogleInfoService requestOAuthInfoService;
    private final UserRepository userRepository;
    private final String DEFAULT_IMAGE_PATH = "classpath:img/defaultImage.svg";

    public AuthTokens handleOAuth(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info("oAuthInfoResponse.mail : {}", oAuthInfoResponse.getEmail());
        log.info("oAuthInfoResponse.nickname : {}", oAuthInfoResponse.getNickname());
        log.info("oAuthInfoResponse.oauth : {}", oAuthInfoResponse.getOAuthProvider());

        String email = oAuthInfoResponse.getEmail();
        Optional<OAuthUser> existingOAuthUser = oAuthUserRepository.findByEmail(email);
        Optional<User> existingLocalUser = userRepository.findByEmail(email);

        if (existingOAuthUser.isPresent()) {
            if (existingOAuthUser.get().getEmail().equals(email) &&
                    existingOAuthUser.get().getOAuthProvider() != oAuthInfoResponse.getOAuthProvider()) {
                throw new EmailAlreadyExistsException(email, existingOAuthUser.get().getOAuthProvider().name());
            }

            if (existingOAuthUser.get().getEmail().equals(email) &&
                    existingOAuthUser.get().getOAuthProvider() == oAuthInfoResponse.getOAuthProvider()) {
                return loginUser(existingOAuthUser.get().getOAuthUserId());
            }
        }

        if (existingLocalUser.isPresent()) {
            throw new EmailAlreadyExistsException(email, "Local");
        }

        Long userId = findOrCreateUser(oAuthInfoResponse);
        return authTokensGenerator.generate(userId);
    }

    @Transactional
    public Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return oAuthUserRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(OAuthUser::getOAuthUserId)
                .orElseGet(() -> {
                    try {
                        return newUser(oAuthInfoResponse);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) throws Exception {
        OAuthUser user = OAuthUser.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        OAuthUserPhoto defaultPhoto = new OAuthUserPhoto("defaultImage", DEFAULT_IMAGE_PATH, 0L);
        user.setOAuthUserPhoto(defaultPhoto);

        return oAuthUserRepository.save(user).getOAuthUserId();
    }

    private AuthTokens loginUser(Long userId) {
        return authTokensGenerator.generate(userId);
    }
}