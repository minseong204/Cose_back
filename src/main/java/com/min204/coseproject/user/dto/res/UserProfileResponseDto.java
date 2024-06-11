package com.min204.coseproject.user.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserProfileResponseDto {
    private String nickname;
    private int postCount;
    private List<ContentDto> posts;
    private int followerCount;
    private List<UserDto> followers;
    private int followingCount;
    private List<UserDto> following;
    private String profileImagePath;

    @Getter
    @Builder
    public static class ContentDto {
        private Long contentId;
        private List<CourseDto> courses;
    }

    @Getter
    @Builder
    public static class CourseDto {
        private Long courseId;
    }

    @Getter
    @Builder
    public static class UserDto {
        private Long userId;
        private String email;
    }
}