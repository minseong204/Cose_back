package com.min204.coseproject.auth.logout.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutDto {
    @NotBlank
    private String accessToken;
    private String refreshToken;
}
