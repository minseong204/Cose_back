package com.min204.coseproject.auth.service;

import com.min204.coseproject.auth.dto.req.AuthSigUpRequestDto;
import com.min204.coseproject.jwt.TokenInfo;
import com.min204.coseproject.user.dto.req.ReissueTokensRequestDto;
import com.min204.coseproject.user.entity.User;

import java.util.Optional;

public interface AuthService {
    User save(AuthSigUpRequestDto authSigUpRequestDto);

    Optional<TokenInfo> login(String email, String password);

    TokenInfo reissueTokens(String refreshToken, ReissueTokensRequestDto reissueTokensRequestDto);

    void existsByEmail(String email);
}
