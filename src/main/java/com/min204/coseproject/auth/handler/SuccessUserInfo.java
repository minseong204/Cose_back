package com.min204.coseproject.auth.handler;

import com.min204.coseproject.constant.UserStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SuccessUserInfo {
    private int httpStatus;
    private Long userId;
    private String email;
    private String nickname;
    private UserStatus userStatus;
}
