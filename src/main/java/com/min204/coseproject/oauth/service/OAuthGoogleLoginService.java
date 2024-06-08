package com.min204.coseproject.oauth.service;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.exception.ExceptionCode;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthGoogleLoginService {
    private final OAuthUserRepository oAuthUserRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthGoogleInfoService requestOAuthInfoService;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info("oAuthInfoResponse.mail : {}",oAuthInfoResponse.getEmail());
        log.info("oAuthInfoResponse.nickname : {}",oAuthInfoResponse.getNickname());
        log.info("oAuthInfoResponse.oauth : {}",oAuthInfoResponse.getOAuthProvider());

        // 이메일 중복 체크
        String email = oAuthInfoResponse.getEmail();
        Optional<OAuthUser> existingOAuthUser = oAuthUserRepository.findByEmail(email);

        if (!existingOAuthUser.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.INVALID_EMAIL);
        }


        Long userId = oAuthUserRepository.findByEmail(oAuthInfoResponse.getEmail())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVALID_EMAIL)).getId();

        return authTokensGenerator.generate(userId);
    }

}
