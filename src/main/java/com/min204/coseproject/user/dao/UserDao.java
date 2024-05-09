package com.min204.coseproject.user.dao;

import com.min204.coseproject.user.entity.User;

import java.util.List;

public interface UserDao {
    User save(User user);

    User findByEmail(String email);
    List<User> findAll();

    void delete(String email);

    void delete(Long userId);

    User find(Long userId);

    Boolean existsByEmail(String email);
}
