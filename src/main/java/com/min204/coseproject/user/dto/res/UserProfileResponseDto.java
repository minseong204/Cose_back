package com.min204.coseproject.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    private String nickname;
    private int postCount;
    private List<Long> contentIds;
    private int followerCount;
    private int followingCount;
}
