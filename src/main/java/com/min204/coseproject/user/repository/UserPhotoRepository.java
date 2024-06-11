package com.min204.coseproject.user.repository;

import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
    List<UserPhoto> findByUser(User user);
}