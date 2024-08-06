package com.min204.coseproject.course.dto;

import com.min204.coseproject.course.entity.CourseUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CourseUserResponseDto {
    private Long courseUserId;

    private Long courseId;

    private String email;

    private String nickname;
    private String profileImagePath;

    private CourseUser.EditPermission editPermission;
}
