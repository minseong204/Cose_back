package com.min204.coseproject.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    private String nickname;
    private int postCount;
    private int followerCount;
    private int followingCount;
}
