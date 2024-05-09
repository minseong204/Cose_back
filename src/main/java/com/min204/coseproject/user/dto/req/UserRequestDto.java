package com.min204.coseproject.user.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String email;
    private String password;
    private String nickname;
}
