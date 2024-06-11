package com.min204.coseproject.user.service;

import com.min204.coseproject.user.dto.req.UserPhotoRequestDto;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User find(Long userId);
    UserProfileResponseDto getUserProfile(Long userId);
    List<User> findAll();
    Optional<User> update(UserRequestDto userRequestDto);
//    void delete(Long userId);
    List<UserPhoto> findUserPhoto(Long userId);
    User getLoginMember();
    boolean resetPassword(Long userId, String newPassword);
    String checkUserPlatform(Long userId);

    Long getUserIdByEmail(String email);
}