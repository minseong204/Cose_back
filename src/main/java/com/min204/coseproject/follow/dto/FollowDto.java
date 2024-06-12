package com.min204.coseproject.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowDto {
    private Long userId;
    private String email;
    private String nickname;
}