package com.min204.coseproject.user.dao;

import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPhotoDao extends JpaRepository<UserPhoto, Long> {
    List<UserPhoto> findUserPhotosByUser(User user);
}