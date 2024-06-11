package com.min204.coseproject.user.controller;

import com.min204.coseproject.auth.service.AuthEmailService;
import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import com.min204.coseproject.user.dto.res.ResponseUserInfoDto;
import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.handler.UserFileHandler;
import com.min204.coseproject.user.mapper.UserMapper;
import com.min204.coseproject.user.mapper.UserPhotoMapper;
import com.min204.coseproject.user.repository.UserRepository;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    private final UserFileHandler userFileHandler;
    private final AuthEmailService authEmailService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return Long.valueOf(authentication.getName());
        }
        throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }

    @GetMapping("/login-view/{userId}")
    public ResponseEntity<ResBodyModel> loginViewLoad(@PathVariable Long userId) throws IOException {
        User user = userService.find(userId);
        List<UserPhoto> userPhoto = userService.findUserPhoto(userId);
        List<Map<String, Object>> userPhotoMapping = userPhotoMapper.toResponse(userPhoto);
        ResponseUserInfoDto responseUserInfoDto = userMapper.toResponse(user, userPhotoMapping);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResBodyModel> load(@PathVariable Long userId) {
        User user = userService.find(userId);
        ResponseUserInfoDto responseUserInfoDto = ResponseUserInfoDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDto);
    }

    @GetMapping
    public ResponseEntity<ResBodyModel> loadAll() {
        List<User> users = userService.findAll();
        List<ResponseUserInfoDto> responseUserInfoDtos = userMapper.toResponse(users);
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDtos);
    }

    @PatchMapping
    public ResponseEntity<ResBodyModel> update(@RequestBody UserRequestDto requestUserSignUpDto) {
        Optional<User> user = userService.update(requestUserSignUpDto);
        ResponseUserInfoDto responseUserInfoDto = userMapper.toResponse(user.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)));
        return CoseResponse.toResponse(SuccessCode.SUCCESS, responseUserInfoDto);
    }

//    @DeleteMapping("/{userId}")
//    public ResponseEntity<ResBodyModel> delete(@PathVariable Long userId) {
//        userService.delete(userId);
//        return CoseResponse.toResponse(SuccessCode.SUCCESS);
//    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<ResBodyModel> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            Long userId = userService.getUserIdByEmail(email);
            String platform = userService.checkUserPlatform(userId);
            if (!platform.equals("LOCAL")) {
                return CoseResponse.toErrorResponse("회원님은 " + platform + "에서 회원가입하셨습니다.", HttpStatus.BAD_REQUEST.value());
            }
            authEmailService.sendEmail(email);
            return CoseResponse.toResponse("이메일을 확인하세요", SuccessCode.SUCCESS);
        } catch (Exception e) {
            return CoseResponse.toErrorResponse("이메일 형식이 유효하지 않거나, 없는 회원정보입니다.", HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ResBodyModel> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (authEmailService.verifyEmailCode(email, code)) {
            return CoseResponse.toResponse("인증성공", SuccessCode.SUCCESS);
        } else {
            return CoseResponse.toErrorResponse("인증코드가 올바르지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResBodyModel> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        Long userId = userService.getUserIdByEmail(email);
        if (userService.resetPassword(userId, newPassword)) {
            return CoseResponse.toResponse("비밀번호 변경 성공", SuccessCode.SUCCESS);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ResBodyModel> getUserProfile(@RequestParam Long userId) {
        try {
            UserProfileResponseDto userProfile = userService.getUserProfile(userId);
            return CoseResponse.toResponse(SuccessCode.SUCCESS, userProfile);
        } catch (BusinessLogicException e) {
            return CoseResponse.toErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            log.error("사용자 프로필을 검색하는 중 오류가 발생했습니다. ", e);
            return CoseResponse.toErrorResponse("사용자 프로필을 조회하는 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @PostMapping("/profile-photo")
    public ResponseEntity<ResBodyModel> updateProfilePhoto(@RequestParam("userId") Long userId,
                                                           @RequestParam("file") MultipartFile file) {
        try {
            User localUser = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

            UserPhoto userPhoto = userFileHandler.parseFileInfo(file, localUser);
            localUser.setUserPhoto(userPhoto);
            userRepository.save(localUser);
            return CoseResponse.toResponse(SuccessCode.SUCCESS, "로컬 사용자의 프로필 사진이 업데이트되었습니다.");
        } catch (Exception e) {
            log.error("Error updating profile photo: ", e);
            return CoseResponse.toErrorResponse("프로필 사진을 업데이트하는 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}