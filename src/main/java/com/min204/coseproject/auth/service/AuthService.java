package com.min204.coseproject.auth.service;

import com.min204.coseproject.auth.dto.req.AuthSigUpRequestDto;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.user.dto.req.ReissueTokensRequestDto;

import java.util.Optional;

public interface AuthService {
    void save(AuthSigUpRequestDto request);
    Optional<TokenInfo> login(String email, String password);
    TokenInfo reissueTokens(String refreshToken, ReissueTokensRequestDto requestDto);
    void existsByEmail(String email);
}