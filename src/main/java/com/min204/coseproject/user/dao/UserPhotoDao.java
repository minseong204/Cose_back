package com.min204.coseproject.user.dao;

import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;

import java.util.List;

public interface UserPhotoDao {

    UserPhoto saveUserPhoto(UserPhoto userPhoto);
    List<UserPhoto> findUserPhotosByUser(User user);

    void userPhotoDelete(UserPhoto userPhoto);
}
