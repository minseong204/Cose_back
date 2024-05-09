package com.min204.coseproject.user.dao;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaUserDao implements UserDao {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_EXISTS));
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public void delete(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_EXISTS));
        userRepository.delete(user);
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_EXISTS));
        userRepository.delete(user);
    }

    @Override
    public User find(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return user;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
