package com.min204.coseproject.oauth.service;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.oauth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.oauth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.oauth.entity.OAuthUser;
import com.min204.coseproject.oauth.jwt.AuthTokens;
import com.min204.coseproject.oauth.jwt.AuthTokensGenerator;
import com.min204.coseproject.oauth.repository.OAuthUserRepository;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final OAuthUserRepository oAuthUserRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final UserService userService;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info("oAuthInfoResponse.mail : {}",oAuthInfoResponse.getEmail());
        log.info("oAuthInfoResponse.nickname : {}",oAuthInfoResponse.getNickname());
        log.info("oAuthInfoResponse.oauth : {}",oAuthInfoResponse.getOAuthProvider());

        // 이메일 중복 체크
        if (userService.checkEmailExists(oAuthInfoResponse.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
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

    public static String uniqueString(int digit) throws Exception {
        if(digit < 1) throw new Exception("can't generate");
        String random = UUID.randomUUID().toString();
        random = random.replaceAll("-", "");
        random = random.substring(0, digit);
        return random;
    }
    private Long newUser(OAuthInfoResponse oAuthInfoResponse) throws Exception {

        String unique = uniqueString(6);

        String nickname = oAuthInfoResponse.getNickname();
        String uuidNickName = nickname + unique;

        OAuthUser user = OAuthUser.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(uuidNickName)
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return oAuthUserRepository.save(user).getId();
    }
}
