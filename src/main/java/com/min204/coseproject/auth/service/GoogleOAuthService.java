package com.min204.coseproject.auth.service;

import com.min204.coseproject.auth.dto.authInfoResponse.OAuthInfoResponse;
import com.min204.coseproject.auth.dto.oAuthLoginParams.OAuthLoginParams;
import com.min204.coseproject.constant.UserRoles;
import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.jwt.JwtTokenProvider;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RequestOAuthGoogleInfoService requestOAuthInfoService;
    private final UserRepository userRepository;
    private final String DEFAULT_IMAGE_PATH = "../defaultImage.svg";

    public TokenInfo handleOAuth(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        log.info("OAuth Info: email={}, nickname={}, provider={}",
                oAuthInfoResponse.getEmail(), oAuthInfoResponse.getNickname(), oAuthInfoResponse.getLoginType());

        String email = oAuthInfoResponse.getEmail();
        try {
            Optional<User> userEmail = userRepository.findByEmail(email);

            if (userEmail.isPresent()) {
                User user = userEmail.get();
                if (user.getLoginType() != null && user.getLoginType() != oAuthInfoResponse.getLoginType()) {
                    throw new EmailAlreadyExistsException(email, user.getLoginType());
                }
                TokenInfo tokenInfo = loginUser(user);
                log.info("Generated Tokens: accessToken={}, refreshToken={}", tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
                return tokenInfo;
            }

            Long userId = findOrCreateUser(oAuthInfoResponse);
            TokenInfo tokenInfo = generateTokenForUser(userId);
            log.info("Generated Tokens: accessToken={}, refreshToken={}", tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
            return tokenInfo;
        } catch (EmailAlreadyExistsException e) {
            throw e;
        }
    }

    @Transactional
    public Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getUserId)
                .orElseGet(() -> {
                    try {
                        return newUser(oAuthInfoResponse);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) throws Exception {
        User user = User.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .loginType(oAuthInfoResponse.getLoginType())
                .roles(new HashSet<>(Collections.singletonList(UserRoles.USER.getRole()))) // roles 추가
                .build();

        UserPhoto defaultPhoto = new UserPhoto("defaultImage", DEFAULT_IMAGE_PATH, 0L);
        user.setUserPhoto(defaultPhoto);

        return userRepository.save(user).getUserId();
    }

    private TokenInfo generateTokenForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    public TokenInfo loginUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        user.setRefreshToken(tokenInfo.getRefreshToken());
        userRepository.save(user);
        return tokenInfo;
    }
}