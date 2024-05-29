package com.min204.coseproject.user.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseFollowerDto {
    private Long userId;
    private String email;
    private String nickname;

    @Builder
    public ResponseFollowerDto(Long userId, String email, String nickname) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
    }
}
