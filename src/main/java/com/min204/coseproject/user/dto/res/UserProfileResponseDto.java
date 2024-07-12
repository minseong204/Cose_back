package com.min204.coseproject.user.dto.res;

import com.min204.coseproject.place.dto.PlaceDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class UserProfileResponseDto {
    private String nickname;
    private String email;

    @Data
    @Builder
    public static class ContentDto {
        private Long contentId;
        private List<CourseDto> courses;
    }

    @Data
    @Builder
    public static class CourseDto {
        private Long courseId;
        private String courseName;
        private List<PlaceDto> places;
    }

    @Data
    @Builder
    public static class UserDto {
        private Long userId;
        private String email;
    }
}