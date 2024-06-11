package com.min204.coseproject.auth.dto.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class AuthSigUpRequestDto {
    @Email
    private String email;
    private String password;
    private String nickname;
}
