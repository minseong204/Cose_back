package com.min204.coseproject.user.service;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.user.dao.UserDao;
import com.min204.coseproject.user.dao.UserPhotoDao;
import com.min204.coseproject.user.dto.req.UserPhotoRequestDto;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.handler.UserFileHandler;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserFileHandler userFileHandler;
    private final UserPhotoDao userPhotoDao;
    private final UserRepository userRepository;

    @Override
    public User find(String email) {
        User user = userDao.findByEmail(email);
        return user;
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
        List<UserPhoto> userPhotos = userPhotoDao.findUserPhotosByUser(user);
        return userPhotos;
    }

    @Override
    public List<UserPhoto> findUserPhoto(String email) {
        User user = userDao.findByEmail(email);
        List<UserPhoto> userPhotos = userPhotoDao.findUserPhotosByUser(user);
        return userPhotos;
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
        User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return user;
    }
}
