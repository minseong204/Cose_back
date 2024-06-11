package com.min204.coseproject.follow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto {
    private Long followerId;
    private String followerEmail;
    private String followerNickname;
    private Long followeeId;
    private String followeeEmail;
    private String followeeNickname;
}