package com.min204.coseproject.oauth.service;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.oauth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.oauth.jwt.AuthTokens;
import com.min204.coseproject.oauth.jwt.AuthTokensGenerator;
import com.min204.coseproject.oauth.repository.OAuthUserRepository;
import com.min204.coseproject.user.repository.UserRepository;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final OAuthUserRepository oAuthUserRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info("oAuthInfoResponse.mail : {}", oAuthInfoResponse.getEmail());
        log.info("oAuthInfoResponse.nickname : {}", oAuthInfoResponse.getNickname());
        log.info("oAuthInfoResponse.oauth : {}", oAuthInfoResponse.getOAuthProvider());

        // 이메일 중복 체크 및 로그인 처리
        if (!userService.checkEmailExists(oAuthInfoResponse.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_EMAIL);
        }

        Long userId = userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVALID_EMAIL))
                .getUserId();

        return authTokensGenerator.generate(userId);
    }
}