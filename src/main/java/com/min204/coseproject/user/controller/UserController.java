package com.min204.coseproject.user.controller;

import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.response.SingleResponseDto;
import com.min204.coseproject.user.dto.req.PasswordResetDto;
import com.min204.coseproject.user.dto.req.PasswordResetRequestDto;
import com.min204.coseproject.user.dto.req.UserPhotoRequestDto;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import com.min204.coseproject.user.dto.res.ResponseUserInfoDto;
import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.mapper.UserMapper;
import com.min204.coseproject.user.mapper.UserPhotoMapper;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserPhotoMapper userPhotoMapper;

    /*
     * 단일 회원 로그인 조회
     * */
    @GetMapping("/login-view/{email}")
    public ResponseEntity<ResBodyModel> loginViewLoad(@PathVariable String email) throws IOException {
        User user = userService.find(email);
        List<UserPhoto> userPhoto = userService.findUserPhoto(email);
        List<Map<String, Object>> userPhotoMapping = userPhotoMapper.toResponse(userPhoto);
        ResponseUserInfoDto responseUserInfoDto = userMapper.toResponse(user, userPhotoMapping);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDto);
    }

    /*
     * 회원 단일 조회
     * */
    @GetMapping("/{email}")
    public ResponseEntity<ResBodyModel> load(@PathVariable String email) {
        User user = userService.find(email);
        ResponseUserInfoDto responseUserInfoDto = ResponseUserInfoDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDto);
    }

    /*
     * 회원 전체 조회
     * */
    @GetMapping
    public ResponseEntity<ResBodyModel> loadAll() {
        List<User> users = userService.findAll();
        List<ResponseUserInfoDto> responseUserInfoDtos = userMapper.toResponse(users);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDtos);
    }

    /*
     * 회원 정보 수정
     * */
    @PatchMapping
    public ResponseEntity<ResBodyModel> update(@RequestBody UserRequestDto requestUserSignUpDto) {
        User user = userService.update(requestUserSignUpDto);
        ResponseUserInfoDto responseUserInfoDto = userMapper.toResponse(user);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDto);
    }

    /*
     * 회원 정보 삭제 (이메일)
     * */
    @DeleteMapping("/{email}")
    public ResponseEntity<ResBodyModel> delete(@PathVariable String email) {
        userService.delete(email);
        return CoseResponse.toResponse(SuccessCode.SUCCESS);
    }

    /*
     * 회원 정보 삭제 (PK)
     * */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResBodyModel> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return CoseResponse.toResponse(SuccessCode.SUCCESS);
    }

    /*
     * 회원 프로필 사진 저장
     * */
    @PostMapping(value = "/file-save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResBodyModel> userPhotoSave(@RequestPart(required = false) List<MultipartFile> files,
                                                      @RequestPart(required = false) UserPhotoRequestDto userPhotoRequestDto) throws Exception {
        log.info("userPhotoRequest: {}", userPhotoRequestDto.getEmail());
        List<UserPhoto> userPhotos = userService.saveUserPhoto(userPhotoRequestDto, files);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, userPhotos);
    }

    /*
     * 회원 프로필 사진 수정
     * */
    @SneakyThrows
    @PostMapping(value = "/file-update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResBodyModel> userPhotoUpdate(@RequestPart(required = false) List<MultipartFile> files,
                                                        @RequestPart(required = false) UserPhotoRequestDto userPhotoRequestDto) {
        log.info("UserPhotoRequest: {}", userPhotoRequestDto.getEmail());
        User user = userService.find(userPhotoRequestDto.getEmail());
        List<UserPhoto> userPhoto = userService.findUserPhoto(user.getUserId());

        for (UserPhoto photo : userPhoto) {
            userService.userPhotoDelete(photo);
        }

        List<UserPhoto> userPhotos = userService.saveUserPhoto(userPhotoRequestDto, files);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, userPhotos);
    }

    /*
     * 비밀번호 재설정 이메일 요청
     * */
    @PostMapping("/password-reset-request")
    public ResponseEntity<ResBodyModel> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.sendPasswordResetEmail(email);
            return CoseResponse.toResponse(SuccessCode.SUCCESS);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * 비밀번호 재설정
     * */
    @PostMapping("/reset-password")
    public ResponseEntity<ResBodyModel> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        if (userService.resetPassword(email, token, newPassword)) {
            return CoseResponse.toResponse(SuccessCode.SUCCESS);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * 사용자 별 프로필 정보 반환
     * */
    @GetMapping("/user/profile")
    public ResponseEntity<ResBodyModel> getUserProfile(@RequestParam String email) {
        UserProfileResponseDto userProfile = userService.getUserProfile(email);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, userProfile);
    }
}
