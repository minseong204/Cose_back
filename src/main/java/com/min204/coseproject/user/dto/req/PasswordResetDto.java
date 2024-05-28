package com.min204.coseproject.user.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDto {
    private String email;
    private String newPassword;
    private String token;
}