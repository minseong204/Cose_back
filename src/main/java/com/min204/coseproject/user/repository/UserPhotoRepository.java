package com.min204.coseproject.user.repository;

import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    @Query("select mp from UserPhoto mp where mp.user = :user")
    List<UserPhoto> findUserPhotosByUser(@Param("user") User user);
}
