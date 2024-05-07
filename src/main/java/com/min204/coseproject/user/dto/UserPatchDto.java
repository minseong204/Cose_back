package com.min204.coseproject.user.dto;

import com.min204.coseproject.constant.UserStatus;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPatchDto {
    private Long userId;

    @Email
    private String email;

    private String nickname;
    private String password;
    private UserStatus userStatus;
    private String image;
}
