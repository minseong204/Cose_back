package com.min204.coseproject.auth.service;

import com.min204.coseproject.auth.dto.AuthSigUpRequestDto;
import com.min204.coseproject.constant.UserRoles;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.jwt.JwtTokenProvider;
import com.min204.coseproject.jwt.TokenInfo;
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

    @Override
    public User save(AuthSigUpRequestDto authSigUpRequestDto) {
        String encode = encoder.encode(authSigUpRequestDto.getPassword());

        User user = User.builder()
                .email(authSigUpRequestDto.getEmail())
                .password(encode)
                .nickname(authSigUpRequestDto.getNickname())
                .roles(Collections.singletonList(UserRoles.USER.getRole()))
                .build();
        existsByEmail(user.getEmail());

        return userDao.save(user);
    }

    @Transactional
    public Optional<TokenInfo> login(String email, String password) {
        User user = userDao.findByEmail(email);

        if (encoder.matches(password, user.getPassword())==true) {
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

    /*
     * 이매일 중복 검사
     * */
    public void existsByEmail(String email) {
        Boolean result = userDao.existsByEmail(email);
        if (result) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }
    }

    private TokenInfo reissueTokensFromUser(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}
