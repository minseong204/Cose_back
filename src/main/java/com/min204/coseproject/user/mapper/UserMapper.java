package com.min204.coseproject.user.mapper;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseUserResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.place.entity.Place;
import com.min204.coseproject.user.dto.res.ResponseUserInfoDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final UserService userService;

    /*
     * 로그인 응답객체
     * */
    public ResponseUserInfoDto toResponse(User user, List<Map<String, Object>> userPhoto) {
        return ResponseUserInfoDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userPhoto(userPhoto)
                .build();
    }

    /*
     * 사진 없이 로그인 응답객체
     * */
    public ResponseUserInfoDto toResponse(User user) {
        return ResponseUserInfoDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    /*
     * 회원 전체 조회 응답객체
     * */
    public List<ResponseUserInfoDto> toResponse(List<User> users) {
        List<ResponseUserInfoDto> responseUserInfoDtos = new ArrayList<>();
        for (User user : users) {
            ResponseUserInfoDto responseUserInfoDto = ResponseUserInfoDto.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
            responseUserInfoDtos.add(responseUserInfoDto);
        }
        return responseUserInfoDtos;
    }
}
