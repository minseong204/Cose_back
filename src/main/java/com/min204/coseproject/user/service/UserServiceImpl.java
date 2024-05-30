package com.min204.coseproject.user.service;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.oauth.repository.OAuthUserRepository;
import com.min204.coseproject.redis.RedisUtil;
import com.min204.coseproject.user.dao.UserDao;
import com.min204.coseproject.user.dao.UserPhotoDao;
import com.min204.coseproject.user.dto.req.UserPhotoRequestDto;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.handler.UserFileHandler;
import com.min204.coseproject.user.repository.UserRepository;
import com.min204.coseproject.auth.service.AuthEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserFileHandler userFileHandler;
    private final UserPhotoDao userPhotoDao;
    private final UserRepository userRepository;
    private final AuthEmailService authEmailService;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OAuthUserRepository oAuthUserRepository;

    @Override
    public User find(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User find(Long userId) {
        return userDao.find(userId);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User update(UserRequestDto userRequestDto) {
        User user = userDao.findByEmail(userRequestDto.getEmail());
        user.changeInfo(userRequestDto);
        return user;
    }

    @Override
    public void delete(String email) {
        userDao.delete(email);
    }

    @Override
    public void delete(Long userId) {
        userDao.delete(userId);
    }

    @Override
    public List<UserPhoto> saveUserPhoto(UserPhotoRequestDto userPhotoRequestDto, List<MultipartFile> files) throws Exception {
        List<UserPhoto> userPhotos = userFileHandler.parseFileInfo(files);
        User user = userDao.findByEmail(userPhotoRequestDto.getEmail());
        for (UserPhoto userPhoto : userPhotos) {
            userPhotoDao.saveUserPhoto(userPhoto);
            userPhoto.addUser(user);
        }
        return userPhotos;
    }

    @Override
    public List<UserPhoto> findUserPhoto(Long userId) {
        User user = userDao.find(userId);
        return userPhotoDao.findUserPhotosByUser(user);
    }

    @Override
    public List<UserPhoto> findUserPhoto(String email) {
        User user = userDao.findByEmail(email);
        return userPhotoDao.findUserPhotosByUser(user);
    }

    @Override
    public void userPhotoDelete(UserPhoto userPhoto) {
        userPhotoDao.userPhotoDelete(userPhoto);
    }

    @Override
    public User getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }

        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        return optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public void sendPasswordResetEmail(String email) throws Exception {
        User user = userDao.findByEmail(email);
        String token = UUID.randomUUID().toString();
        redisUtil.setDataExpire(email, token, 60 * 30L);  // 30분 동안 유효
        authEmailService.sendPasswordResetEmail(email, token);
    }

    @Override
    public boolean resetPassword(String email, String token, String newPassword) {
        String storedToken = redisUtil.getData(email);
        if (storedToken != null && storedToken.equals(token)) {
            User user = userDao.findByEmail(email);
            user.setPassword(passwordEncoder.encode(newPassword));
            userDao.save(user);
            redisUtil.deleteData(email);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email) || oAuthUserRepository.existsByEmail(email);
    }
}