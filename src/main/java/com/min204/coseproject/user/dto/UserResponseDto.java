package com.min204.coseproject.user.dto;

import com.min204.coseproject.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String password;
    private UserStatus userStatus;
    private String image;
}
