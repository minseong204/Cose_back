package com.min204.coseproject.user.dto.res;

import com.min204.coseproject.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ResponseUserInfoDto {
    private String email;
    private String nickname;
    private List<Map<String, Object>> userPhoto;

    @Builder
    public ResponseUserInfoDto(String email, String nickname, List<Map<String, Object>> userPhoto) {
        this.email = email;
        this.nickname = nickname;
        this.userPhoto = userPhoto;
    }

    @Builder
    public ResponseUserInfoDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public static ResponseUserInfoDto buildDto(User user, List<Map<String, Object>> userPhoto) {
        return ResponseUserInfoDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userPhoto(userPhoto)
                .build();
    }
}
