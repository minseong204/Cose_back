package com.min204.coseproject.auth.service;

import com.min204.coseproject.auth.dto.AuthSigUpRequestDto;
import com.min204.coseproject.constant.UserRoles;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.jwt.JwtTokenProvider;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.oauth.entity.OAuthUser;
import com.min204.coseproject.oauth.repository.OAuthUserRepository;
import com.min204.coseproject.user.dao.UserDao;
import com.min204.coseproject.user.dto.req.ReissueTokensRequestDto;
import com.min204.coseproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final BCryptPasswordEncoder encoder;
    private final UserDao userDao;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthUserRepository oAuthUserRepository;

    @Override
    public User save(AuthSigUpRequestDto authSigUpRequestDto) {
        String encode = encoder.encode(authSigUpRequestDto.getPassword());

        User user = User.createUser(
                authSigUpRequestDto.getEmail(),
                encode,
                authSigUpRequestDto.getNickname(),
                Collections.singletonList(UserRoles.USER.getRole()),
                "src/main/resources/img/defaultImage.svg" // 기본 이미지 경로 설정
        );
        existsByEmail(user.getEmail());

        return userDao.save(user);
    }

    @Transactional
    public Optional<TokenInfo> login(String email, String password) {
        User user = userDao.findByEmail(email);

        if (encoder.matches(password, user.getPassword())) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, user.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            user.setRefreshToken(tokenInfo.getRefreshToken());
            return Optional.ofNullable(tokenInfo);
        } else {
            throw new BusinessLogicException(ExceptionCode.USER_NOT_LOGIN);
        }
    }

    @Override
    @Transactional
    public TokenInfo reissueTokens(String refreshToken, ReissueTokensRequestDto reissueTokensRequestDto) {
        User user = userDao.findByEmail(reissueTokensRequestDto.getEmail());
        if (jwtTokenProvider.validateToken(refreshToken) && refreshToken.equals(user.getRefreshToken())) {
            TokenInfo tokenInfo = reissueTokensFromUser(user);
            user.setRefreshToken(tokenInfo.getRefreshToken());
            return tokenInfo;
        }

        throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS_TOKEN);
    }

    @Override
    public void existsByEmail(String email) {
        boolean existsInLocal = userDao.existsByEmail(email);
        boolean existsInOAuth = oAuthUserRepository.existsByEmail(email);

        if (existsInLocal) {
            throw new EmailAlreadyExistsException(email, "Local");
        }

        if (existsInOAuth) {
            Optional<OAuthUser> oauthUser = oAuthUserRepository.findByEmail(email);
            throw new EmailAlreadyExistsException(email, oauthUser.get().getOAuthProvider().name());
        }
    }

    private TokenInfo reissueTokensFromUser(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}