package com.min204.coseproject.user.service;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.follow.repository.FollowRepository;
import com.min204.coseproject.oauth.entity.OAuthUser;
import com.min204.coseproject.oauth.entity.OAuthUserPhoto;
import com.min204.coseproject.oauth.repository.OAuthUserRepository;
import com.min204.coseproject.redis.RedisUtil;
import com.min204.coseproject.user.dao.UserDao;
import com.min204.coseproject.user.dao.UserPhotoDao;
import com.min204.coseproject.user.dto.req.UserPhotoRequestDto;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final ContentRepository contentRepository;
    private final FollowRepository followRepository;

    @Override
    public User find(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public UserProfileResponseDto getUserProfile(String email) {
        User user = find(email);
        int postCount = contentRepository.countByUser(user);
        int followerCount = followRepository.countByFollowee(user);
        int followingCount = followRepository.countByFollower(user);
        List<Long> contentIds = contentRepository.findAllByUser(user).stream()
                .map(Content::getContentId)
                .collect(Collectors.toList());

        return new UserProfileResponseDto(user.getNickname(), postCount, contentIds, followerCount, followingCount);
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
    public List<Object> saveUserPhoto(UserPhotoRequestDto userPhotoRequestDto, List<MultipartFile> files) throws Exception {
        String email = userPhotoRequestDto.getEmail();
        String userPlatform = checkUserPlatform(email);
        List<Object> userPhotos = new ArrayList<>();

        switch (userPlatform) {
            case "LOCAL":
                User user = userDao.findByEmail(email);
                for (MultipartFile file : files) {
                    UserPhoto userPhoto = userFileHandler.parseFileInfo(file, user);
                    userPhotoDao.saveUserPhoto(userPhoto);
                    userPhoto.addUser(user);
                    userPhotos.add(userPhoto);
                }
                break;
            case "GOOGLE":
            case "KAKAO":
            case "NAVER":
                Optional<OAuthUser> oAuthUserOpt = oAuthUserRepository.findByEmail(email);
                if (!oAuthUserOpt.isPresent()) {
                    throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
                }

                OAuthUser oAuthUser = oAuthUserOpt.get();
                for (MultipartFile file : files) {
                    OAuthUserPhoto oAuthUserPhoto = userFileHandler.parseOAuthFileInfo(file, oAuthUser);
                    oAuthUser.setOAuthUserPhoto(oAuthUserPhoto);
                    oAuthUserRepository.save(oAuthUser);
                    userPhotos.add(oAuthUserPhoto);
                }
                break;
            default:
                throw new BusinessLogicException(ExceptionCode.INVALID_PLATFORM);
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
        // User user = userDao.findByEmail(email);
        String token = UUID.randomUUID().toString();
        redisUtil.setDataExpire(email, token, 60 * 30L);  // 30분 동안 유효
        authEmailService.sendPasswordResetEmail(email, token);
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        User user = userDao.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
        redisUtil.deleteData(email);
        return true;
    }

    @Override
    public String checkUserPlatform(String email) {
        Optional<User> localUser = userRepository.findByEmail(email);
        Optional<OAuthUser> oauthUser = oAuthUserRepository.findByEmail(email);

        if (localUser.isPresent()) {
            return "LOCAL";
        }

        if (oauthUser.isPresent()) {
            return oauthUser.get().getOAuthProvider().name();
        }

        throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
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