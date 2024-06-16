package com.min204.coseproject.auth.service;

import com.min204.coseproject.auth.dto.req.AuthSigUpRequestDto;
import com.min204.coseproject.constant.ErrorCode;
import com.min204.coseproject.constant.LoginType;
import com.min204.coseproject.constant.UserRoles;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.EmailAlreadyExistsException;
import com.min204.coseproject.jwt.JwtTokenProvider;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.user.dto.req.ReissueTokensRequestDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public void save(AuthSigUpRequestDto authSigUpRequestDto) {
        String encode = passwordEncoder.encode(authSigUpRequestDto.getPassword());

        User user = User.builder()
                .email(authSigUpRequestDto.getEmail())
                .password(encode)
                .nickname(authSigUpRequestDto.getNickname())
                .roles(new HashSet<>(Collections.singletonList(UserRoles.USER.getRole())))
                .loginType(LoginType.LOCAL)
                .build();

        UserPhoto defaultPhoto = new UserPhoto("defaultImage", "classpath:img/defaultImage.svg", 0L);
        user.setUserPhoto(defaultPhoto);

        existsByEmail(user.getEmail());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<TokenInfo> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FAILED_LOGIN));

        if (passwordEncoder.matches(password, user.getPassword())) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authenticationToken);

            user.setRefreshToken(tokenInfo.getRefreshToken());
            userRepository.save(user);
            return Optional.of(tokenInfo);
        } else {
            throw new BusinessLogicException(ErrorCode.FAILED_LOGIN);
        }
    }

    @Override
    @Transactional
    public TokenInfo reissueTokens(String refreshToken, ReissueTokensRequestDto reissueTokensRequestDto) {
        log.info("Reissuing tokens for email: {}", reissueTokensRequestDto.getEmail());
        User user = userRepository.findByEmail(reissueTokensRequestDto.getEmail())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        log.info("User found: {}", user.getEmail());

        if (jwtTokenProvider.validateToken(refreshToken) && refreshToken.equals(user.getRefreshToken())) {
            log.info("Refresh token is valid");
            TokenInfo tokenInfo = reissueTokensFromUser(user);
            user.setRefreshToken(tokenInfo.getRefreshToken());
            userRepository.save(user);
            return tokenInfo;
        }

        throw new BusinessLogicException(ErrorCode.UNAUTHORIZED);
    }

    @Override
    public void existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            throw new EmailAlreadyExistsException(email, user.getLoginType());
        }
    }

    private TokenInfo reissueTokensFromUser(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}