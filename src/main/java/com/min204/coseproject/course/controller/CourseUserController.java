package com.min204.coseproject.course.controller;

import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.course.dto.CourseUserResponseDto;
import com.min204.coseproject.course.entity.CourseUser;
import com.min204.coseproject.course.service.CourseUserService;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/course-member")
@RequiredArgsConstructor
public class CourseUserController {

    private final CourseUserService courseUserService;

    // 유저 가입 (초대)
    @PostMapping("/invite")
    public ResponseEntity<ResBodyModel> inviteUser(
            @RequestParam @NotNull Long courseId,
            @RequestParam @NotNull Long userId,
            @RequestParam @NotNull CourseUser.EditPermission editPermission) {
        CourseUserResponseDto response = courseUserService.inviteUser(courseId, userId, editPermission);
        return CoseResponse.toResponse(SuccessCode.COURSE_CREATED, response, HttpStatus.CREATED.value());
    }

    // 유저 강퇴
    @DeleteMapping("/{courseId}/users/{userId}")
    public ResponseEntity<?> removeUserFromCourse(
            @PathVariable @NotNull Long courseId,
            @PathVariable @NotNull Long userId) {
        courseUserService.removeUserFromCourse(courseId, userId);
        return CoseResponse.toResponse(SuccessCode.COURSE_DELETED,  userId.toString(), HttpStatus.OK.value());
    }

    // 유저 권한 수정
    @PutMapping("/{courseId}/users/{userId}/access")
    public ResponseEntity<CourseUserResponseDto> updateUserAccess(
            @PathVariable @NotNull Long courseId,
            @PathVariable @NotNull Long userId,
            @RequestParam @NotNull CourseUser.EditPermission editPermission) {
        CourseUserResponseDto response = courseUserService.updateUserAccess(courseId, userId, editPermission);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 코스 내 유저 그룹 전체 조회
    @GetMapping("/{courseId}/users")
    public ResponseEntity<?> getAllUsersInCourse(@PathVariable @NotNull Long courseId) {
        List<CourseUserResponseDto> users = courseUserService.getAllUsersInCourse(courseId);

        return CoseResponse.toResponse(SuccessCode.FETCH_SUCCESS, users, HttpStatus.PARTIAL_CONTENT.value());
    }

    // 코스 내 내 정보 조회 (권한 등)
    @GetMapping("/{courseId}/my-info")
    public ResponseEntity<CourseUserResponseDto> getMyInfoInCourse(
            @PathVariable @NotNull Long courseId,
            @RequestParam @NotNull Long userId) {  // 실제로는 보안 컨텍스트에서 현재 로그인한 사용자의 ID를 가져와야 합니다.
        CourseUserResponseDto myInfo = courseUserService.getMyInfoInCourse(courseId, userId);
        return new ResponseEntity<>(myInfo, HttpStatus.OK);
    }
}