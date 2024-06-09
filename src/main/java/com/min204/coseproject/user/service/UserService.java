package com.min204.coseproject.user.service;

import com.min204.coseproject.user.dto.req.UserPhotoRequestDto;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User find(String email);

    UserProfileResponseDto getUserProfile(String email);

    User find(Long userId);

    List<User> findAll();

    User update(UserRequestDto userRequestDto);

    void delete(String email);

    void delete(Long userId);

    List<Object> saveUserPhoto(UserPhotoRequestDto userPhotoRequestDto, List<MultipartFile> files) throws Exception;

    List<UserPhoto> findUserPhoto(Long userId);

    List<UserPhoto> findUserPhoto(String email);

    void userPhotoDelete(UserPhoto userPhoto);

    User getLoginMember();

    void sendPasswordResetEmail(String email) throws Exception;

    boolean resetPassword(String email, String newPassword);

    String checkUserPlatform(String email);

    boolean checkEmailExists(String email);

    boolean isEmailExists(String email);
}