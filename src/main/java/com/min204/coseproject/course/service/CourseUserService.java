package com.min204.coseproject.course.service;
import com.min204.coseproject.course.dto.CourseUserResponseDto;
import com.min204.coseproject.course.entity.CourseUser;

import java.util.List;

public interface CourseUserService {

    // 유저 초대
    CourseUserResponseDto inviteUser(Long courseId, Long userId, CourseUser.EditPermission editPermission);

    // 유저 권한 수정
    CourseUserResponseDto updateUserAccess(Long courseId, Long userId, CourseUser.EditPermission editPermission);

    // 유저 강퇴
    void removeUserFromCourse(Long courseId, Long userId);

    // 코스의 전체 유저 정보 가져오기
    List<CourseUserResponseDto> getAllUsersInCourse(Long courseId);

    // 코스의 내 정보 가져오기
    CourseUserResponseDto getMyInfoInCourse(Long courseId, Long userId);
}