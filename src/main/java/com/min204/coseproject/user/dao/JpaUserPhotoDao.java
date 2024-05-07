package com.min204.coseproject.user.dao;

import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.repository.UserPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaUserPhotoDao implements UserPhotoDao {
    private final UserPhotoRepository userPhotoRepository;

    @Override
    public UserPhoto saveUserPhoto(UserPhoto userPhoto) {
        return userPhotoRepository.save(userPhoto);
    }

    @Override
    public List<UserPhoto> findUserPhotosByUser(User user) {
        return userPhotoRepository.findUserPhotosByUser(user);
    }

    @Override
    public void userPhotoDelete(UserPhoto userPhoto) {
        userPhotoRepository.delete(userPhoto);
    }
}
