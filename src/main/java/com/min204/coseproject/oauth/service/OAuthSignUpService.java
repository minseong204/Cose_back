package com.min204.coseproject.oauth.service;

import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.oauth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.oauth.entity.OAuthUser;
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
public class OAuthSignUpService {
    private final OAuthUserRepository oAuthUserRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final UserRepository userRepository;

    public AuthTokens signUp(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info("mail : {}", oAuthInfoResponse.getEmail());
        log.info("nickname : {}", oAuthInfoResponse.getNickname());
        log.info("Type : {}", oAuthInfoResponse.getOAuthProvider());

        // 이메일 중복 체크
        String email = oAuthInfoResponse.getEmail();
        Optional<OAuthUser> existingOAuthUser = oAuthUserRepository.findByEmail(email);
        Optional<User> existingLocalUser = userRepository.findByEmail(email);

        if (existingOAuthUser.isPresent()) {
            throw new EmailAlreadyExistsException(email, existingOAuthUser.get().getOAuthProvider().name());
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
                .map(OAuthUser::getId)
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

        return oAuthUserRepository.save(user).getId();
    }
}
