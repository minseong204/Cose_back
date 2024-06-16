package com.min204.coseproject.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenInfo {
    private Long userId;
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
