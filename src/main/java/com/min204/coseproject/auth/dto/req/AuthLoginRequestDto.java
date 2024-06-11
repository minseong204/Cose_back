package com.min204.coseproject.auth.dto.req;

import lombok.Data;

@Data
public class AuthLoginRequestDto {
    private String email;
    private String password;
}
